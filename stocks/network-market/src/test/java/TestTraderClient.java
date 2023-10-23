import networkMarket.MarketSerializer;
import networkMarket.TraderClient.TraderClient;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;
import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yaml.snakeyaml.error.Mark;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestTraderClient {
    private TraderClient traderClient;
    private static final int PORT = 6400;

    private BufferedReader serverIn;
    private PrintWriter serverOut;

    private Stock mockStock1;

    private boolean serverStarted;

    private void startTempServer() {
        new Thread( ()-> {
            try {
                ServerSocket serverSocket = new ServerSocket(getPort());
                serverStarted = true;
                Socket socket = serverSocket.accept();
                log.info("connected");
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverOut = new PrintWriter(socket.getOutputStream(), true);
                assertTrue(socket.isConnected());
            } catch (IOException e) {
                log.error("Could not start test server", e);
            }
        }).start();

        await().atMost(Duration.ofSeconds(1)).until(() -> serverStarted);
        log.info("Server started at port: " + getPort());
    }

    private int getPort() {
        try {
            return Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (Exception e) {
            log.info("could not find environment variable for port: ", e);
            return PORT;
        }
    }

    @BeforeEach
    public void setUp() {
        startTempServer();
        Map<Stock, Integer> stockMap = new HashMap<>();
        mockStock1 = Mockito.mock(Stock.class);
        Stock tempStock = new Stock("2", 100, "yee", 100);
        Mockito.when(mockStock1.getName()).thenReturn("mockStock1");
        stockMap.put(tempStock, 10);
        this.traderClient = new TraderClient("1", "Arnaud", 100, stockMap);
    }

    @Test
    public void testStartTraderClient() {
        this.traderClient.start();
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());
        traderClient.terminate();
    }

    @Test
    public void testTerminateTraderClient() {
        traderClient.start();
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
        assertEquals(networkedAsk.getTrader(), traderClient.getTrader());
        assertEquals(networkedAsk.getStock(), tempStock);
        assertEquals(networkedAsk.getPrice(), 100);
    }

    @Test
    public void testPlacingBid() throws IOException, ClassNotFoundException {
        traderClient.start();
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
        assertEquals(networkedBid.getTrader(), traderClient.getTrader());
    }

}
