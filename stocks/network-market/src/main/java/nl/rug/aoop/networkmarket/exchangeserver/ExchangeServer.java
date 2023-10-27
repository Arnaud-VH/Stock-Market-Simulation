package nl.rug.aoop.networkmarket.exchangeserver;

import lombok.Getter;
import nl.rug.aoop.networkmarket.exchangeserver.clientUpdater.ClientUpdater;
import nl.rug.aoop.networkmarket.exchangeserver.exchangeCommandHandler.ExchangeCommandHandlerFactory;
import nl.rug.aoop.networkmarket.exchangeserver.exchangeMessageHandler.ExchangeMessageHandler;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.messagequeue.commandhandler.QueueCommandHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.CommandHandler;
import nl.rug.aoop.market.exchange.Exchange;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.messagequeue.commandhandler.QueueCommandHandler;
import nl.rug.aoop.messagequeue.consumers.Consumer;
import nl.rug.aoop.messagequeue.messagehandlers.CommandMessageHandler;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedBlockingQueue;
import nl.rug.aoop.networking.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * ExchangeServer is an exchange that has network capabilities.
 * When it's network capabilities are running it works by spawning 3.
 * threads (server, messageHandler, clientNotifier) that receive messages from clients, handle.
 * said messages to update an exchange and update clients on latest exchange information.
 * When it's network capabilities are not running it behaves like a regular exchange.
 */
@Slf4j
public class ExchangeServer extends Exchange {
    private static final int PORT = 6400;
    private final Server server;
    private ScheduledFuture<?> notifier;
    private final MessageQueue messageQueue = new OrderedBlockingQueue();
    private final Consumer consumer = new Consumer(messageQueue);
    private final CommandHandler commandHandler = new ExchangeCommandHandlerFactory(this).createCommandHandler();
    private final ExchangeMessageHandler messageHandler = new ExchangeMessageHandler(consumer, commandHandler);
    @Getter
    private final Map<Trader, Integer> traderRegister = new HashMap<>();

    /**
     * Constructor for ExchangeServer.
     * @param stocks Stocks to add to the exchange.
     */
    public ExchangeServer(ArrayList<Stock> stocks) {
        super(stocks);
        new QueueCommandHandlerFactory(messageQueue).createCommandHandler();
        server = new Server(getPort(), new CommandMessageHandler(QueueCommandHandler.getInstance()));
    }

    /**
     * Constructor for the Exchange Server.
     * @param stocks The stocks that are listed in the Exchange.
     * @param traders The traders that are trading on the exchange.
     */
    public ExchangeServer(ArrayList<Stock> stocks, ArrayList<Trader> traders) {
        super(stocks, traders);
        new QueueCommandHandlerFactory(messageQueue).createCommandHandler();
        server = new Server(getPort(), new CommandMessageHandler(QueueCommandHandler.getInstance()));
        log.info("Exchange Server started at port: " + getPort());
    }

    /**
     * Method to start ExchangeServer - spawns the 3 threads.
     */
    public void start() {
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread messageHandlerThread = new Thread(messageHandler);
        messageHandlerThread.start();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        notifier = executor.scheduleAtFixedRate(new ClientUpdater(this,server), 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Method to terminate ExchangeServer - terminates the 3 threads.
     */
    public void terminate() {
        server.terminate();
        messageHandler.terminate();
        notifier.cancel(false);
    }

    /**
     * Private method that gets port to connect server to from environment variable.
     * @return Port
     */
    private int getPort() {
        try {
            return Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (NullPointerException | NumberFormatException e) {
            log.error("Could not find environment variable for port. Using default port " + PORT);
            return PORT;
        }
    }

    /**
     * Returns if ExchangeServer (3 threads) is running.
     * @return Whether ExchangeServer is running.
     */
    public boolean isRunning() {
        return (server.isRunning() && messageHandler.isRunning());
    }

    /**
     * Registers the trader.
     * @param traderID The trader ID.
     * @param clientID The clientID.
     */
    public void registerTrader(Trader traderID, int clientID) {
        if (!traderRegister.containsKey(traderID)) {
            traderRegister.put(traderID,clientID);
            return;
        }
        log.info("Trader " + traderID + " already registered");
    }

    /**
     * Checks if the trader is registered.
     * @param trader The trader that is being checked if it is registered.
     * @return If the clientID is registered or not.
     */
    public boolean isRegistered(Trader trader) {
        return traderRegister.containsKey(trader);
    }

}
