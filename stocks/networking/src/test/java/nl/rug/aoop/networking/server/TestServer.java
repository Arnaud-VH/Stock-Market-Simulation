package nl.rug.aoop.networking.server;

import nl.rug.aoop.networking.handlers.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class TestServer {

    @Test
    public void testConstructor(){
        Server server = new Server(0, null);
        assertNotNull(server);
    }

    @Test
    public void testConstructorInvalidPort(){
        assertThrows(IllegalArgumentException.class, () -> {
            Server server = new Server(680000, null);
            assertNull(server);
        });
    }

    @Test
    public void testServerRun(){
        Server server = new Server(0, null);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        assertTrue(server.isRunning());
        server.terminate();
    }

    @Test
    public void testServerTermination() {
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, mockHandler);
        Thread thread = new Thread(server);
        thread.start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        server.terminate();
        await().atMost(Duration.ofSeconds(1)).until(() -> !thread.isAlive());
        assertTrue(!server.isRunning() && !thread.isAlive());
    }

    @Test
    public void testServerClientConnects() throws IOException {
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, mockHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        InetSocketAddress address = new InetSocketAddress("localhost", server.getPort());
        Socket socket = new Socket();
        socket.connect(address,1000);
        await().atMost(Duration.ofSeconds(1)).until(socket::isConnected);
        assertEquals(1, server.getClientHandlers().size());
        server.terminate();
    }

    @Test
    public void sendMessageValidID() throws IOException {
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, mockHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);

        InetSocketAddress address = new InetSocketAddress("localhost", server.getPort());
        Socket socket = new Socket();
        socket.connect(address,1000);
        await().atMost(Duration.ofSeconds(1)).until(socket::isConnected);

        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        fromServer.readLine(); // consume id
        server.sendMessage(0,"hello world");
        assertEquals("hello world", fromServer.readLine());
    }

    @Test
    public void sendMessageInvalidID() throws IOException {
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, mockHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);

        InetSocketAddress address = new InetSocketAddress("localhost", server.getPort());
        Socket socket = new Socket();
        socket.connect(address,1000);
        await().atMost(Duration.ofSeconds(1)).until(socket::isConnected);

        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        fromServer.readLine(); // consume id
        server.sendMessage(5,"invalid");
        server.sendMessage(0,"valid");
        assertEquals("valid", fromServer.readLine());
    }

}
