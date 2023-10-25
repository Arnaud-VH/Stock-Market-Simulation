package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.handlers.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

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
                log.info("connected");
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverOut = new PrintWriter(socket.getOutputStream(), true);
                assertTrue(socket.isConnected());
            } catch (IOException e) {
                log.error("Could not start test server", e);
            }
        }).start();

        await().atMost(Duration.ofSeconds(1)).until(() -> serverStarted);
    }

    @Test
    public void testTerminate() {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, messageHandler);
        Thread thread = new Thread(client);
        thread.start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());
        client.terminate();
        await().atMost(Duration.ofSeconds(2)).until(() -> !thread.isAlive());
        assertTrue(!client.isRunning() && !thread.isAlive());
    }

    @Test
    public void testConstructorWithRunningServer() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockHandler);
        assertTrue(client.isConnected());
    }

    @Test
    public void testConstructorNoServer() {
        InetSocketAddress address = new InetSocketAddress("10.255.255.1",0);  // hostname is not routable so connection to server can't be made
        Client client = new Client(address, null);
        assertFalse(client.isConnected());
    }

    @Test
    public void testRun() {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());
        client.terminate();
    }

    @Test
    public void testHandleMessage() {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address,mockHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isConnected());
        assertTrue(client.isRunning());
        String message = "hello";
        serverOut.println(message);
        Mockito.verify(mockHandler).handleMessage("hello");
        client.terminate();
    }

    @Test
    public void testSendMessage() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, messageHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());
        client.sendMessage("hello");
        assertEquals("hello", serverIn.readLine());
        client.terminate();
    }
}
