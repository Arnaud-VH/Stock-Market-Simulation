package nl.rug.aoop.networkmarket;

import nl.rug.aoop.networkmarket.traderclient.TraderClient;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.networkmarket.exchangeserver.ExchangeServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.awaitility.Awaitility.await;


public class IntegrationTest {
    private ExchangeServer exchangeServer;
    private Stock stock1;
    private Stock stock2;
    private ArrayList<Stock> stocks;
    private BufferedReader in;
    private PrintWriter out;
    private TraderClient traderArnaud;
    private TraderClient traderClement;
    private Map<Stock,Long> arnaudsPortfolio;
    private volatile String inString = null;
    Socket socketToClose;

    @BeforeEach
    public void setupExchange() {
        stock1 = new Stock("MS1",50,"MockStock1", 1000);
        stock2 = new Stock("MS2",60,"MockStock2", 1000);
        stocks = new ArrayList<>();
        stocks.add(stock1);
        stocks.add(stock2);
        arnaudsPortfolio = new HashMap<>();
        arnaudsPortfolio.put(stock1, 100L);
        arnaudsPortfolio.put(stock2, 10L);
        exchangeServer = new ExchangeServer(stocks);
    }

    @Test
    public void integrationTest() {
        exchangeServer.start();
        traderArnaud = new TraderClient("T-RN0","TraderArnaud",10000, arnaudsPortfolio);
        traderClement = new TraderClient("T-CLMN7", "TraderClement", 50000, arnaudsPortfolio);
        await().atMost(Duration.ofSeconds(1)).until(exchangeServer::isRunning);
        traderClement.start();
        traderArnaud.start();
        await().atMost(Duration.ofSeconds(1)).until(() -> exchangeServer.isRegistered(traderClement));
        await().atMost(Duration.ofSeconds(1)).until(() -> exchangeServer.isRegistered(traderArnaud));
    }
}
