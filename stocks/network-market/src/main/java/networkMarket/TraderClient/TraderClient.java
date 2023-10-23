package networkMarket.TraderClient;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import networkMarket.MarketSerializer;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;
import nl.rug.aoop.messagequeue.MessageHandlers.LogMessageHandler;
import nl.rug.aoop.messagequeue.Producers.MQProducer;
import nl.rug.aoop.messagequeue.Producers.NetworkProducer;
import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.networking.Client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * The TraderClient is a trader that can communicate over the network.
 */
@Slf4j
public class TraderClient {
    private static final int PORT = 6400;
    private final Client client;
    private final MQProducer producer;
    @Getter
    private final Trader trader;

    /**
     * Constructor for the trader client.
     * @param id The ID of the trader.
     * @param name The name of the trader.
     * @param funds The funds of the trader.
     * @param ownedStocks The stocks the trader owns.
     */
    public TraderClient(String id, String name, int funds, Map<Stock, Integer> ownedStocks) {
        //super(id, name, funds, ownedStocks);
        this.trader = new Trader(id, name, funds, ownedStocks);
        InetSocketAddress address = new InetSocketAddress("localhost", getPort());
        this.client = new Client(address, new LogMessageHandler());
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
            log.info("Could not find environment variable for port: ", e);
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
            Ask ask = new Ask(this.getTrader(), stock, shares, price);
            producer.put(new Message("PlaceAsk", MarketSerializer.toString(ask)));
        } catch (IOException e) {
            log.error("Trader: " +  trader.getId() + " could not send PlaceAsk command", e);
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
            Bid bid = new Bid(this.getTrader(), stock, shares, price);
            producer.put(new Message("PlaceBid", MarketSerializer.toString(bid)));
        } catch (IOException e) {
            log.error("Trader: " + trader.getId() + " could not send PlaceBid command", e);
        }
    }
}
