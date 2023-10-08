package nl.rug.aoop.networking.Server;

import nl.rug.aoop.networking.Handlers.CommandMessageHandler;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestServer {

    @Test
    public void testConstructor() throws IOException {
        MessageHandler mockHandler = Mockito.mock(CommandMessageHandler.class);
        Server server = new Server(0, mockHandler);
        assertTrue(server.isStarted());
    }



}
