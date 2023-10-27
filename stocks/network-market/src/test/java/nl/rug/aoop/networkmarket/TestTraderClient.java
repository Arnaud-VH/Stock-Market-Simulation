package nl.rug.aoop.networkmarket;

import nl.rug.aoop.networkmarket.traderclient.TraderClient;
import nl.rug.aoop.networkmarket.serialiser.MarketSerializer;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.market.transaction.Ask;
import nl.rug.aoop.market.transaction.Bid;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestTraderClient {
    private volatile TraderClient traderClient;
    private static final int PORT = 6400;

    private BufferedReader serverIn;
    private PrintWriter serverOut;
    private Thread serverThread;
    private ServerSocket serverSocket;
    private Stock mockStock1;

    private volatile boolean serverStarted;

    private void startTempServer() {
        this.serverThread = new Thread( ()-> {
            try {
                this.serverSocket = new ServerSocket(getPort());
                serverStarted = true;
                Socket socket = serverSocket.accept();
                log.info("connected");
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverOut = new PrintWriter(socket.getOutputStream(), true);
                assertTrue(socket.isConnected());
            } catch (IOException e) {
                log.error("Could not start test server", e);
            }
        });
        this.serverThread.start();
        await().atMost(Duration.ofSeconds(1)).until(() -> serverStarted);
        log.info("Server started at port: " + getPort());
    }

    private int getPort() {
        try {
            return Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (Exception e) {
            log.info("could not find environment variable for port: ");
            return PORT;
        }
    }

    @BeforeEach
    public void setUp() {
        startTempServer();
        Map<Stock, Long> stockMap = new HashMap<>();
        mockStock1 = Mockito.mock(Stock.class);
        Stock tempStock = new Stock("2", 100, "yee", 100);
        Mockito.when(mockStock1.getName()).thenReturn("mockStock1");
        stockMap.put(tempStock, 10L);
        this.traderClient = new TraderClient("1", "Arnaud", 100, stockMap);
    }

    @AfterEach
    public void cleanUp() throws IOException {
        this.serverSocket.close();
        this.serverThread.interrupt();
    }

    @Test
    public void testStartTraderClient() {
        this.traderClient.start();
        //serverOut.println("0");
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());
        traderClient.terminate();
    }

    @Test
    public void testTerminateTraderClient() {
        traderClient.start();
        //serverOut.println("0");
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());
        traderClient.terminate();
        await().atMost(Duration.ofSeconds(2)).until(() -> !traderClient.isRunning());
        assertFalse(traderClient.isRunning());
        traderClient.terminate();
    }

    @Test
    public void testPlacingAsk() throws IOException, ClassNotFoundException {
        traderClient.start();
        //serverOut.println("0");
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());

        Stock tempStock = new Stock("1", 100, "yee", 100);

        traderClient.placeAsk(tempStock, 10, 100);

        log.info("Trader sent message");

        String receivedServer = serverIn.readLine();
        log.info("The server received: " + receivedServer);
        NetworkMessage ntwMessage = NetworkMessage.fromJson(receivedServer);
        assertNotNull(ntwMessage);
        Message msg = Message.fromJson(ntwMessage.getBody());
        assertNotNull(msg);


        Ask networkedAsk = MarketSerializer.fromString(msg.getBody(), Ask.class);
        assertEquals(networkedAsk.getShares(), 10);
        assertEquals(networkedAsk.getTrader(), traderClient);
        assertEquals(networkedAsk.getStock(), tempStock);
        assertEquals(networkedAsk.getPrice(), 100);
    }

    @Test
    public void testPlacingBid() throws IOException, ClassNotFoundException {
        traderClient.start();
        //serverOut.println("0");
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());

        Stock tempStock = new Stock("1", 100, "yes", 100);
        traderClient.placeBid(tempStock, 50, 150);

        String receivedServer = serverIn.readLine();

        NetworkMessage networkMessage = NetworkMessage.fromJson(receivedServer);
        assertNotNull(networkMessage);
        Message message = Message.fromJson(networkMessage.getBody());
        assertNotNull(message);

        Bid networkedBid = MarketSerializer.fromString(message.getBody(), Bid.class);

        assertEquals(networkedBid.getShares(), 50);
        assertEquals(networkedBid.getStock(), tempStock);
        assertEquals(networkedBid.getPrice(), 150);
        assertEquals(networkedBid.getTrader(), traderClient);
    }

    @Test
    public void testRegister() throws IOException, ClassNotFoundException {
        traderClient.start();
        await().atMost(Duration.ofSeconds(1)).until(traderClient::isRunning);
        serverOut.println(new NetworkMessage("client_id",String.valueOf(0)).toJson());

        String receivedServer = serverIn.readLine();
        Message msg = Message.fromJson(NetworkMessage.fromJson(receivedServer).getBody());
        assertEquals("register",msg.getHeader());
        ArrayList<Serializable> list = MarketSerializer.fromString(msg.getBody(),ArrayList.class);
        assertEquals(traderClient, (Trader)list.get(0));
        assertEquals(0, list.get(1));
    }

    @Test
    public void testUpdate() throws IOException, InterruptedException {
        traderClient.start();

        Trader trader = new Trader("4", "Arnaud", 1500, new HashMap<>());
        ArrayList<Serializable> info = new ArrayList<>();
        info.add(new ArrayList<>());
        info.add(trader);
        NetworkMessage message = new NetworkMessage("update",MarketSerializer.toString(info));
        serverOut.println(message.toJson());

        await().atMost(Duration.ofSeconds(1)).until(() -> traderClient.getFunds() == 1500);
        log.info("Trader client funds is: " + traderClient.getFunds());
    }
}
