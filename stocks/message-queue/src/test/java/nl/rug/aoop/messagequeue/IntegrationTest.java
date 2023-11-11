package nl.rug.aoop.messagequeue;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.commandhandler.QueueCommandHandlerFactory;
import nl.rug.aoop.messagequeue.consumers.Consumer;
import nl.rug.aoop.messagequeue.producers.MQProducer;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.messagehandlers.CommandMessageHandler;
import nl.rug.aoop.messagequeue.messagehandlers.LogMessageHandler;
import nl.rug.aoop.messagequeue.producers.NetworkProducer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.OrderedBlockingQueue;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class IntegrationTest {

    @Test
    public void testIntegration() throws IOException {
        MessageQueue messageQueue = new OrderedBlockingQueue();
        QueueCommandHandlerFactory factory = new QueueCommandHandlerFactory(messageQueue);
        Server server = new Server(6400, new CommandMessageHandler(factory.createCommandHandler()));
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(5)).until(server::isRunning);

        InetSocketAddress address = new InetSocketAddress("localhost",6400);
        Client client = new Client(address,new LogMessageHandler());
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(5)).until(client::isRunning);

        MQProducer producer = new NetworkProducer(client);
        producer.put(new Message("Test Header", "Test Body"));

        Consumer consumer = new Consumer(messageQueue);

        await().atMost(Duration.ofSeconds(2)).until(() -> messageQueue.getSize() != 0);

        Message message = consumer.poll();
        assertEquals("Test Header", message.getHeader());
        assertEquals("Test Body", message.getBody());
    }

}
