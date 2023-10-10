package nl.rug.aoop.networking.Server;

import nl.rug.aoop.networking.Client.Client;
import nl.rug.aoop.networking.Handlers.CommandMessageHandler;
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
        MessageHandler mockHandler = Mockito.mock(CommandMessageHandler.class);
        Server server = new Server(0, mockHandler);
        assertTrue(server.isStarted());
    }

    @Test
    public void testConstructorInvalidPort(){
        assertThrows(IllegalArgumentException.class, () -> {
            MessageHandler mockHandler = Mockito.mock(CommandMessageHandler.class);
            Server server = new Server(680000, mockHandler);
            assertFalse(server.isStarted());
        });
    }

    @Test
    public void testServerRun(){
        MessageHandler mockHandler = Mockito.mock(CommandMessageHandler.class);
        Server server = new Server(0, mockHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        assertTrue(server.isRunning());
    }

    @Test
    public void testServerClientConnects() throws IOException {
        MessageHandler mockCommandHandler = Mockito.mock(CommandMessageHandler.class);
        Server server = new Server(0, mockCommandHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        InetSocketAddress address = new InetSocketAddress("localhost", server.getPort());
        MessageHandler mockMessageHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(address, mockMessageHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(1)).until(server::isRunning);
        assertEquals(1, server.getClientHandlerMap().size());
    }

}
