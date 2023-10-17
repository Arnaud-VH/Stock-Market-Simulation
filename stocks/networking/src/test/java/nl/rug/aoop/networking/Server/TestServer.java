package nl.rug.aoop.networking.Server;

import nl.rug.aoop.networking.Handlers.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.function.Predicate;

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
    public void testServerTermination() {
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, mockHandler);
        Thread thread = new Thread(server);
        thread.start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        server.terminate();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning, Predicate.isEqual(false));
        assertFalse(server.isRunning() && thread.isAlive());
    }

}
