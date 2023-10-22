import lombok.extern.slf4j.Slf4j;
import networkmarket.ExchangeServer;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import serverExchange.commandHandler.MarketSerializer;

import static org.awaitility.Awaitility.await;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TestExchangeServer {
    private ExchangeServer exchangeServer;
    private Stock stock1;
    private Stock stock2;
    private List<Stock> stocks;
    @BeforeEach
    public void setup() {
        stock1 = new Stock("MS1",50,"MockStock1", 1000);
        stock2 = new Stock("MS2",60,"MockStock2", 1000);
        stocks = new ArrayList<>();
        stocks.add(stock1);
        stocks.add(stock2);
        exchangeServer = new ExchangeServer(stocks);
    }

    /**
     * Dear Arnaud,
     *
     * As I am doing some testing on the exchangeServer class right now, I realize I find it very hard to
     * test whether an exchangeServer is actually running.
     *
     * This is because the exchangeServer spawns three threads (a server, a custom message handler and a trader notifier),
     * who all work together to receive messages over the network, apply their commands to the exchange and notify traders.
     * Unlike server this means that the exchangeServer doesn't actually contain any loop, it just spawns those threads and is done.
     * So it's futile to put it in a new thread, hence we can't test if it's running by checking if a hypothetical new thread is
     * still open. Ideally we'd want to check if the three threads it spawns are running.
     *
     * This made me think however, if I try to test whether the three threads are running then I am most definitely making
     * my test, implementation specific.
     *
     * So for now I think it's ok to just test if exchangeServer.isRunning() is set to true. I know this is contradictory
     * to what I told you about tests in the past. But I believe that because as of now I am not 100% certain of how
     * I want the exchangeServer to behave (either spawning it in a new thread -> implying it contains a loop OR for it
     * to be started through a simple method call) there is no way for me to do a test that is not implementation specific,
     * apart from this vague and poor test.
     *
     * Ideally I would already have planned out EXACTLY how exchangeServer should behave, but I find that quite challenging to do
     * because it's honestly a pretty irrelevant decision and should be decided when we have a clearer idea of the code
     * calling the exchangeServer.
     *
     * Hope this message finds you well,
     * Yours sincerely,
     * Clement
     */
    @Test
    public void testStartServer() {
        exchangeServer.start();
        await().atMost(Duration.ofSeconds(1)).until(exchangeServer::isRunning);
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
    public void placeBidOverNetwork() throws IOException, InterruptedException {
        exchangeServer.start();
        InetSocketAddress address = new InetSocketAddress("localhost",6400);
        Socket socket =  new Socket();
        socket.connect(address, 1000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Map<Stock,Integer> arnaudsPortefolio = new HashMap<>();


        arnaudsPortefolio.put(stock1,10);
        arnaudsPortefolio.put(stock2,10);
        Trader traderArnaud = new Trader("T-RN0","TraderArnaud",10000,arnaudsPortefolio);

        Trader mockTraderArnaud = Mockito.mock(Trader.class);
        Mockito.when(mockTraderArnaud.getId()).thenReturn("T-RN0");
        Mockito.when(mockTraderArnaud.getFunds()).thenReturn(10000);
        Mockito.when(mockTraderArnaud.getShares(Mockito.any(Stock.class))).thenReturn(100);


        Ask ask = new Ask(traderArnaud, stock1, 50,100);
        String msg = new Message("PlaceAsk", MarketSerializer.toString(ask)).toJson();
        String ntwMsg = new NetworkMessage("MQPut",msg).toJson();
        out.println(ntwMsg);

        await().atMost(Duration.ofSeconds(1)).until(() -> !exchangeServer.getAsks(ask.getStock()).isEmpty());
    }
}
