package nl.rug.aoop.messagequeue.NetworkingTests;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.Client.Client;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

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
    public void testSendMessage() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        Client client = new Client(address, null);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());
        client.sendMessage("hello");
        assertEquals("hello", serverIn.readLine());
    }

    @Test
    public void testSendNullMessage() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        Client client = new Client(address, null);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertTrue(client.isRunning());

        assertThrows(IllegalArgumentException.class, () -> {
            client.sendMessage(null);
        });
    }

    @Test
    public void testSendEmptyMessage() throws IOException {
        startTempServer();
        InetSocketAddress address = new InetSocketAddress("localhost", serverPort);
        Client client = new Client(address, null);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);

        assertThrows(IllegalArgumentException.class, () -> {
            assertTrue(client.isRunning());
            client.sendMessage("");
        });
    }

}
