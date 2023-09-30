package nl.rug.aoop.networking;

import nl.rug.aoop.networking.Client.Client;
import nl.rug.aoop.networking.Client.MessageHandler;
import nl.rug.aoop.networking.Command.CommandHandler;
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
        CommandHandler commandHandler = Mockito.mock(CommandHandler.class);
        Server server = new Server(0, commandHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(5)).until(server::isRunning);

        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Client client = new Client(new InetSocketAddress("localhost", server.getPort()), messageHandler);

        new Thread(client).start();

        await().atMost(Duration.ofSeconds(5)).until(client::isRunning);
        client.sendMessage("move.left");

        Mockito.verify(commandHandler).execute("move.left");
    }
}
