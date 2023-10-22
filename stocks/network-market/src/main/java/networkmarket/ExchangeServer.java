import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandlerFactory;
import serverExchange.commandHandler.ExchangeCommandHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.CommandHandler;
import nl.rug.aoop.market.Exchange.Exchange;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandler;
import nl.rug.aoop.messagequeue.Consumers.Consumer;
import nl.rug.aoop.messagequeue.MessageHandlers.CommandMessageHandler;
import nl.rug.aoop.messagequeue.Queues.MessageQueue;
import nl.rug.aoop.messagequeue.Queues.OrderedBlockingQueue;
import nl.rug.aoop.networking.Server.Server;

import java.util.List;

/**
 * ExchangeServer is an exchange that has network capabilities.
 * When it's network capabilities are running it works by spawning 3
 * threads (server, messageHandler, clientNotifier) that receive messages from clients, handle
 * said messages to update an exchange and update clients on latest exchange information.
 * When it's network capabilities are not running it behaves like a regular exchange.
 */
@Slf4j
public class ExchangeServer extends Exchange {
    private static final int PORT = 6400;
    private final Server server;
    private final MessageQueue messageQueue = new OrderedBlockingQueue();
    private final Consumer consumer = new Consumer(messageQueue);
    private final CommandHandler commandHandler = new ExchangeCommandHandlerFactory(this).createCommandHandler();
    private final ExchangeMessageHandler messageHandler = new ExchangeMessageHandler(consumer, commandHandler);

    /**
     * Constructor for ExchangeServer.
     * @param stocks Stocks to add to the exchange
     */
    public ExchangeServer(List<Stock> stocks) {
        super(stocks);
        new QueueCommandHandlerFactory(messageQueue).createCommandHandler();
        server = new Server(getPort(), new CommandMessageHandler(QueueCommandHandler.getInstance()));
    }

    /**
     * Method to start ExchangeServer - spawns the 3 threads.
     */
    public void start() {
        Thread serverThread = new Thread(server); // server handles incoming networkMessages and puts them in MQ
        serverThread.start();

        Thread messageHandlerThread = new Thread(messageHandler); // handles Messages in MQ (exchange add on)
        messageHandlerThread.start();
    }

    /**
     * Method to terminate ExchangeServer - terminates the 3 threads
     */
    public void terminate() {
        server.terminate();
        messageHandler.terminate();
    }

    /**
     * Private method that gets port to connect server to from environment variable.
     * @return Port
     */
    private int getPort() {
        try {
            return Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (Exception e) {
            log.info("could not find environment variable for port: ", e);
            return PORT;
        }
    }

    /**
     * Returns if ExchangeServer (3 threads) is running
     * @return Whether ExchangeServer is running
     */
    public boolean isRunning() {
        return (server.isRunning() && messageHandler.isRunning());
    }

    // TODO client updater
}
