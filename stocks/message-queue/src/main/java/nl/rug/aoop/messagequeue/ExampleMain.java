package nl.rug.aoop.messagequeue;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.Commands.Factories.QueueCommandHandlerFactory;
import nl.rug.aoop.messagequeue.Commands.QueueCommandHandler;
import nl.rug.aoop.messagequeue.Interfaces.MQProducer;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;
import nl.rug.aoop.messagequeue.Queues.OrderedBlockingQueue;
import nl.rug.aoop.networking.Client.Client;
import nl.rug.aoop.networking.Server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExampleMain {
    public static void main(String[] args) throws IOException {
        // server
        MessageQueue messageQueue = new OrderedBlockingQueue();
        QueueCommandHandlerFactory factory = new QueueCommandHandlerFactory(messageQueue);
        Server server = new Server(6400, new CommandMessageHandler(factory.createCommandHandler()));
        new Thread(server).start();

        // client(s)
        InetSocketAddress address = new InetSocketAddress("localhost",6400);
        Client client = new Client(address,new LogMessageHandler());
        new Thread(client).start();
        MQProducer producer = new NetworkProducer(client);
        producer.put(new Message("Test Header", "Test Body"));



        Consumer consumer = new Consumer(messageQueue);
        log.info("Queue length: " + messageQueue.getSize());

        while (true) {
            if (messageQueue.getSize() != 0) {
                Message message = consumer.poll();
                log.info("header: " + message.getHeader() + "\n" + "body: " + message.getBody());
            }
        }
    }
}
