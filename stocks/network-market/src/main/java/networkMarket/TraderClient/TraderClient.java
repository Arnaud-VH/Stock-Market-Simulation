package networkMarket.TraderClient;

import lombok.extern.slf4j.Slf4j;
import networkMarket.MarketSerializer;
import networkMarket.TraderClient.TraderCommandHandler.TraderCommandHandler;
import networkMarket.TraderClient.TraderCommandHandler.TraderCommandHandlerFactory;
import nl.rug.aoop.market.stock.Stock;
import nl.rug.aoop.market.trader.Trader;
import nl.rug.aoop.market.transaction.Ask;
import nl.rug.aoop.market.transaction.Bid;
import nl.rug.aoop.messagequeue.messagehandlers.CommandMessageHandler;
import nl.rug.aoop.messagequeue.producers.MQProducer;
import nl.rug.aoop.messagequeue.producers.NetworkProducer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.client.Client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;

/**
 * The TraderClient is a trader that can communicate over the network.
 */
@Slf4j
public class TraderClient extends Trader{
    //TODO: Command message handler that can handle an update command.
    private static final transient int PORT = 6400; //TODO Make sure the port is the same as the exchangeServer port.
    private final transient Client client;
    private final transient MQProducer producer;
    private final transient TraderCommandHandler commandHandler;

    /**
     * Constructor for the trader client.
     * @param id The ID of the trader.
     * @param name The name of the trader.
     * @param funds The funds of the trader.
     * @param ownedStocks The stocks the trader owns.
     */
    public TraderClient(String id, String name, int funds, Map<Stock, Integer> ownedStocks) {
        super(id, name, funds, ownedStocks);
        InetSocketAddress address = new InetSocketAddress("localhost", getPort());
        this.commandHandler = new TraderCommandHandlerFactory(this).createCommandHandler();
        this.client = new Client(address, new CommandMessageHandler(commandHandler));
        this.producer = new NetworkProducer(client);
    }

    /**
     * Starts the traderClient's connection with the network.
     */
    public void start() {
        Thread clientThread = new Thread(this.client);
        clientThread.start();
    }

    /**
     * Terminates the trader client's connection with the network.
     */
    public void terminate() {
        this.client.terminate();
    }

    private int getPort() {
        try {
            return Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } catch (NullPointerException | NumberFormatException e) {
            log.info("Could not find environment variable for port");
            return PORT;
        }
    }

    public boolean isRunning() {
        return client.isRunning();
    }

    /**
     * Method to place an ask over the network.
     * @param stock The stock that the trader wants to buy.
     * @param shares The amount of shares the trader wants to buy.
     * @param price The price of at which they want to buy the stock.
     */
    public void placeAsk(Stock stock, int shares, int price) {
        try {
            Ask ask = new Ask(this, stock, shares, price);
            producer.put(new Message("PlaceAsk", MarketSerializer.toString(ask)));
        } catch (IOException e) {
            log.error("Trader: " +  this.getId() + " could not send PlaceAsk command", e);
        }
    }

    /**
     * Method to place a bid over the network.
     * @param stock The stock that the trader wants to sell.
     * @param shares The amount of shares the trader wants to sell.
     * @param price The price at which the trader wants to sell.
     */
    public void placeBid(Stock stock, int shares, int price) {
        try {
            Bid bid = new Bid(this, stock, shares, price);
            producer.put(new Message("PlaceBid", MarketSerializer.toString(bid)));
        } catch (IOException e) {
            log.error("Trader: " + this.getId() + " could not send PlaceBid command", e);
        }
    }

    /**
     * Registers the trader with the server.
     * @param id The ID of the trader.
     */
    public void register(int id) {
        ArrayList<Serializable> list = new ArrayList<>();
        list.add(this);
        list.add(id);
        try {
            producer.put(new Message("register", MarketSerializer.toString(list)));
        } catch (IOException e) {
            log.error("Cannot serialise in Client register", e);
        }
    }

    /**
     * Updates the traders information.
     * @param funds The new funds of the trader.
     * @param newStocks The new stocks that the trader has.
     */
    public void updateTrader(int funds, Map<Stock, Integer> newStocks) {
        setFunds(funds);
        setOwnedStocks(newStocks);
    }

}
