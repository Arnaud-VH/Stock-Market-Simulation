package networkMarket.TraderClient;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.market.Stock.Stock;
import nl.rug.aoop.market.Trader.Trader;
import nl.rug.aoop.market.Transaction.Ask;
import nl.rug.aoop.market.Transaction.Bid;
import nl.rug.aoop.messagequeue.MessageHandlers.LogMessageHandler;
import nl.rug.aoop.messagequeue.Producers.MQProducer;
import nl.rug.aoop.messagequeue.Producers.NetworkProducer;
import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.networking.Client.Client;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * The TraderClient is a trader that can communicate over the network.
 */
@Slf4j
public class TraderClient extends Trader {
    private static final int PORT = 6400;
    private final Client client;
    private final MQProducer producer;

    /**
     * Constructor for the trader client.
     * @param id The ID of the trader.
     * @param name The name of the trader.
     * @param funds The funds of the trader.
     * @param ownedStocks The stocks the trader owns.
     */
    public TraderClient(String id, String name, int funds, Map<Stock, Integer> ownedStocks) {
        super(id, name, funds, ownedStocks);
        InetSocketAddress address = new InetSocketAddress("localhost",getPort());
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
        } catch (NullPointerException e) {
            log.info("could not find environment variable for port: ", e);
            return PORT;
        }
    }

    public boolean isRunning() {
        return client.isRunning();
    }

    /**
     * Places an ask over the network by putting a command into the MQ.
     * @param ask The ask that is placed into the MQ.
     */
    public void placeAsk(Ask ask) {
        // TODO: The ask object needs to be able to be converted
        producer.put(new Message("placeAsk", ask.toString()));
    }

    /**
     * Places a bid over the network by putting the command into the MQ.
     * @param bid The big that is placed into the MQ.
     */
    public void placeBid(Bid bid) {
        //TODO: Big object needs to be converted appropriately so it can be sent across the network.
        producer.put(new Message("placeBid", bid.toString()));
    }

}
