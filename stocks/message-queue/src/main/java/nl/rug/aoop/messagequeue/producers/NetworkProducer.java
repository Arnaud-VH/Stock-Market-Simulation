package nl.rug.aoop.messagequeue.producers;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.networkmessage.NetworkMessage;

/**
 * Puts messages over the network.
 */
public class NetworkProducer implements MQProducer {
    private final Client client;

    /**
     * Constructor for the network producer.
     * @param client The client that is the producer over the network.
     */
    public NetworkProducer(Client client) {
        this.client = client;
    }

    @Override
    public void put(Message message) {
        client.sendMessage(new NetworkMessage("MQPut", message.toJson()).toJson());
    }
}
