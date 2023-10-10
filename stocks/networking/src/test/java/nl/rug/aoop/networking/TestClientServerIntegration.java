package nl.rug.aoop.networking;

import nl.rug.aoop.networking.Client.Client;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import nl.rug.aoop.networking.Server.Server;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class TestClientServerIntegration {

    @Test
    public void testIntegration() throws IOException {
        MessageHandler commandMessageHandler = Mockito.mock(CommandMessageHandler.class);
        Server server = new Server(0, commandMessageHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(5)).until(server::isRunning);

        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(new InetSocketAddress("localhost", server.getPort()), messageHandler);

        new Thread(client).start();

        await().atMost(Duration.ofSeconds(5)).until(client::isRunning);

        //TODO: Update this once the command works fully.
        client.sendMessage("move.left");

        Mockito.verify(commandMessageHandler).handleMessage("move.left");
    }
}
