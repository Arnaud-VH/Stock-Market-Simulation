import TraderClient.TraderClient;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.Stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class TestTraderClient {
    private TraderClient traderClient;
    private static final int PORT = 6400;

    private boolean serverStarted;

    private void startTempServer() {
        new Thread( ()-> {
            try {
                ServerSocket serverSocket = new ServerSocket(getPort());
                serverStarted = true;
                Socket socket = serverSocket.accept();
                log.info("connected");
                assertTrue(socket.isConnected());
            } catch (IOException e) {
                log.error("Could not start test server", e);
            }
        }).start();

        await().atMost(Duration.ofSeconds(1)).until(() -> serverStarted);
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
        Stock mockStock1 = Mockito.mock(Stock.class);
        Mockito.when(mockStock1.getName()).thenReturn("mockStock1");
        stockMap.put(mockStock1, 10);
        this.traderClient = new TraderClient("1", "Arnaud", 100, stockMap);
    }

    @Test
    public void testStartTraderClient() {
        this.traderClient.start();
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());
    }

    @Test
    public void testTerminateTraderClient() {
        traderClient.start();
        await().atMost(Duration.ofSeconds(2)).until(traderClient::isRunning);
        assertTrue(traderClient.isRunning());
        traderClient.terminate();
        await().atMost(Duration.ofSeconds(2)).until(() -> !traderClient.isRunning());
        assertFalse(traderClient.isRunning());
    }

    @Test
    public void testPlacingAsk() {
        //TODO complete placing ask test
    }

    @Test
    public void testPlacingBid() {
        //TODO complete placing bid test
    }

}
