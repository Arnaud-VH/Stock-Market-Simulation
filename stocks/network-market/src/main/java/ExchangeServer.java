import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.CommandHandler;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandler;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandlerFactory;
import nl.rug.aoop.messagequeue.Consumers.Consumer;
import nl.rug.aoop.messagequeue.MessageHandlers.CommandMessageHandler;
import nl.rug.aoop.messagequeue.Queues.MessageQueue;
import nl.rug.aoop.messagequeue.Queues.OrderedBlockingQueue;
import nl.rug.aoop.networking.Server.Server;

import java.util.List;

/**
 * ExchangeServer is a class that hosts an exchange on a server
 */
@Slf4j
public class ExchangeServer extends Exchange implements Runnable{
    Server server;
    private final MessageQueue messageQueue = new OrderedBlockingQueue();
    private final Consumer consumer = new Consumer(messageQueue);

    /**
     * Constructor for ExchangeServer.
     * @param stocks Stocks to add to the exchange
     */
    public ExchangeServer(List<Stock> stocks) {
        super(stocks);
        int port = 69;
        try {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (Exception e) {
            log.info("could not find environment variable for port: ", e);
        }
        CommandHandler commandHandler = new QueueCommandHandlerFactory(messageQueue).createCommandHandler();
        this.server = new Server(port, new CommandMessageHandler(QueueCommandHandler.getInstance()));
    }

    @Override
    public void run() {
        server.run();
        while (true) {
            // poll message queue
            // once per second update clients
        }
    }
}
