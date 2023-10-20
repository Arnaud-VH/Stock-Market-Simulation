package nl.rug.aoop.networking.Server;

import nl.rug.aoop.networking.Client.Client;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class TestServer {

    @Test
    public void testConstructor(){
        Server server = new Server(0, null);
        assertTrue(server.isStarted());
    }

    @Test
    public void testConstructorInvalidPort(){
        assertThrows(IllegalArgumentException.class, () -> {
            Server server = new Server(680000, null);
            assertFalse(server.isStarted());
        });
    }

    @Test
    public void testServerRun(){
        Server server = new Server(0, null);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        assertTrue(server.isRunning());
    }

    @Test
    public void testServerClientConnects() throws IOException {
        MessageHandler mockHandler = Mockito.mock(MessageHandler.class);
        Server server = new Server(0, mockHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        InetSocketAddress address = new InetSocketAddress("localhost", server.getPort());
        Client client = new Client(address, mockHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(client::isRunning);
        assertEquals(1, server.getClientHandlerMap().size());
    }

}
