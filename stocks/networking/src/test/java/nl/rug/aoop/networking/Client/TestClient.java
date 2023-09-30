package nl.rug.aoop.networking.Client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class TestClient {

    private BufferedReader serverIn;
    private PrintWriter serverOut;
    private boolean serverStarted;
    private int serverPort;

    private void startTempServer() {
        new Thread( ()-> {
            try {
                ServerSocket serverSocket = new ServerSocket(0);
                serverPort = serverSocket.getLocalPort();
                serverStarted = true;
                Socket socket = serverSocket.accept();
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverOut = new PrintWriter(socket.getOutputStream(), true);
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
    }

    @Test
    public void testConstructorWithoutServer() throws IOException {
        //Having some trouble knowing what exactly I should test here.
        //The client should not connect, hence the assertFalse(isConnected) but without the server
        //I get NullPointerExceptions and other errors. How should I fix this?
        InetSocketAddress address = Mockito.mock(InetSocketAddress.class);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockHandler);
        assertFalse(client.isConnected());
    }

    @Test
    public void testConstructorInvalidPort() {

    }

    @Test
    public void testRun() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());
    }

    @Test
    public void testSendMessage() {

    }

    @Test
    public void testSendNullMessage() {

    }

    @Test
    public void testSendEmptyMessage() {

    }

    @Test
    public void testRunReadSingleMessage() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isConnected());
        assertTrue(client.isRunning());
        //From server side send message to client
        String message = "hello";
        serverOut.println(message);
        Mockito.verify(mockHandler).handleMessage(message);
    }

}
