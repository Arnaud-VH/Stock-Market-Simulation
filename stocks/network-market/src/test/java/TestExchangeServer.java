import lombok.extern.slf4j.Slf4j;
import networkMarket.exchangeServer.clientUpdater.ExchangeServer;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;
import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import networkMarket.MarketSerializer;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestExchangeServer {
    private ExchangeServer exchangeServer;
    private Stock stock1;
    private Stock stock2;
    private ArrayList<Stock> stocks;
    private BufferedReader in;
    private PrintWriter out;
    private Trader traderArnaud;
    private volatile String inString = null;
    Socket socketToClose;
    @BeforeEach
    public void setupExchange() {
        // we use real stocks and traders because Mockito mocks are not serializable
        stock1 = new Stock("MS1",50,"MockStock1", 1000);
        stock2 = new Stock("MS2",60,"MockStock2", 1000);
        stocks = new ArrayList<>();
        stocks.add(stock1);
        stocks.add(stock2);
        Map<Stock,Integer> arnaudsPortfolio = new HashMap<>();
        arnaudsPortfolio.put(stock1,100);
        arnaudsPortfolio.put(stock2,10);
        traderArnaud = new Trader("T-RN0","TraderArnaud",10000,arnaudsPortfolio);
        exchangeServer = new ExchangeServer(stocks);
    }

    private void setupTempClient() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost",6400);
        socketToClose =  new Socket();
        socketToClose.connect(address, 1000);
        in = new BufferedReader(new InputStreamReader(socketToClose.getInputStream()));
        out = new PrintWriter(socketToClose.getOutputStream(), true);
    }

    @Test
    public void testStartServer() {
        exchangeServer.start();
        await().atMost(Duration.ofSeconds(1)).until(exchangeServer::isRunning);
        exchangeServer.terminate();
    }

    @Test
    public void testTerminateServer() throws InterruptedException {
        log.info("Amount of threads before server start: " + String.valueOf(Thread.getAllStackTraces().size()));

        exchangeServer.start();
        await().atMost(Duration.ofSeconds(1)).until(exchangeServer::isRunning);
        log.info("Amount of threads after server start: " + String.valueOf(Thread.getAllStackTraces().size()));


        exchangeServer.terminate();
        await().atMost(Duration.ofSeconds(1)).until(() -> !exchangeServer.isRunning());
        log.info(String.valueOf(Thread.getAllStackTraces().size()));
    }

    @Test
    public void placeAskOverNetwork() throws IOException, InterruptedException {
        exchangeServer.start();
        setupTempClient();

        Ask ask = new Ask(traderArnaud, stock1, 50,100);
        String msg = new Message("PlaceAsk", MarketSerializer.toString(ask)).toJson();
        String ntwMsg = new NetworkMessage("MQPut",msg).toJson();
        out.println(ntwMsg);

        await().atMost(Duration.ofSeconds(1)).until(() -> !exchangeServer.getAsks(ask.getStock()).isEmpty());
        assertEquals(exchangeServer.getAsks(ask.getStock()).first().getPrice(),ask.getPrice());
        assertEquals(exchangeServer.getAsks(ask.getStock()).first().getShares(),ask.getShares());
        exchangeServer.terminate();
        socketToClose.close();
    }

    @Test
    public void placeBidOverNetwork() throws IOException, InterruptedException {
        exchangeServer.start();
        setupTempClient();

        Bid bid = new Bid(traderArnaud, stock1, 10,100);
        String msg = new Message("PlaceBid", MarketSerializer.toString(bid)).toJson();
        String ntwMsg = new NetworkMessage("MQPut",msg).toJson();
        out.println(ntwMsg);

        await().atMost(Duration.ofSeconds(1)).until(() -> !exchangeServer.getBids(bid.getStock()).isEmpty());
        assertEquals(exchangeServer.getBids(bid.getStock()).first().getPrice(),bid.getPrice());
        assertEquals(exchangeServer.getBids(bid.getStock()).first().getShares(),bid.getShares());
        exchangeServer.terminate();
        socketToClose.close();
    }

    @Test
    public void clientUpdated() throws IOException, ClassNotFoundException {
        exchangeServer.start();
        setupTempClient();

        // second message so we skip the server sending us our id
        Runnable runnable = () -> {
            int count = 0;
            String s = null;
            while (count != 2) {
                try {
                    s = in.readLine();
                    count++;
                } catch (IOException e) {
                    log.error("Exception when reading line: ", e);
                }
            }
            inString = s;
        };

        Thread thread = new Thread(runnable);
        thread.start();

        await().atMost(Duration.ofSeconds(3)).until(() -> inString != null);

        ArrayList list = MarketSerializer.fromString(inString, ArrayList.class);
        assertEquals(list.get(0),exchangeServer.getStocks());
        assertEquals(list.get(1),exchangeServer.getTraders());
        exchangeServer.terminate();
        socketToClose.close();
    }
}
