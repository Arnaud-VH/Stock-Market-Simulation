package nl.rug.aoop.networking.Server;

import nl.rug.aoop.networking.Client.MessageHandler;
import nl.rug.aoop.networking.Command.CommandHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestServer {

    @Test
    public void testConstructor() throws IOException {
        CommandHandler mockHandler = Mockito.mock(CommandHandler.class);
        Server server = new Server(0, mockHandler);
        assertTrue(server.isStarted());
    }


}
