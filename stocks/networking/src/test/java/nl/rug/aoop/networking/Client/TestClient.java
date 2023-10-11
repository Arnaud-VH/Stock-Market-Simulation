package nl.rug.aoop.networking.Client;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestClient {

    private boolean serverStarted;
    private int serverPort;
    private BufferedReader serverIn;
    private PrintWriter serverOut;

    private void startTempServer() {
        new Thread( ()-> {
            try {
                ServerSocket serverSocket = new ServerSocket(0);
                serverPort = serverSocket.getLocalPort();
                serverStarted = true;
                Socket socket = serverSocket.accept();
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverOut = new PrintWriter(socket.getOutputStream(), true);
                assertTrue(socket.isConnected());
                log.info("server started");
            } catch (IOException e) {
                log.error("Could not start server", e);
            }
        }).start();

        await().atMost(Duration.ofSeconds(1)).until(() -> serverStarted);
    }

    @Test
    public void testConstructorWithRunningServer() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockHandler);
        assertTrue(client.isConnected());
        log.info("Client connected to the server correctly");
    }

    @Test
    public void testConstructorWithoutServer(){
        assertThrows(NullPointerException.class, ()-> {
            InetSocketAddress address = Mockito.mock(InetSocketAddress.class);
            Client client = new Client(address, null);
            assertFalse(client.isConnected());
        });
    }

    @Test
    public void testConstructorInvalidPort(){
        assertThrows(IllegalArgumentException.class, ()-> {
            ServerSocket serverSocket = new ServerSocket(680000);
            serverPort = serverSocket.getLocalPort();
            InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
            Client client = new Client(address, null);
            assertFalse(client.isConnected());
        });
    }

    @Test
    public void testRun() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        Client client = new Client(address, null);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());
    }

    @Test
    public void testReadSingleMessage() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        AtomicReference<String> test = new AtomicReference<>();

        Client client = new Client(address,(test::set));
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);

        assertTrue(client.isConnected());
        assertTrue(client.isRunning());
        String message = "hello";
        serverOut.println(message);
        await().atMost(Duration.ofSeconds(1)).until(() -> test.get() != null);

        assertEquals("hello", test.get());
    }

    @Test
    public void testTerminate() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, messageHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());

        String message = "Stop Connection";
        serverOut.println(message);
        await().atMost(Duration.ofSeconds(2)).until(() -> !client.isRunning());
        assertFalse(client.isRunning());
    }
}
