package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.Interfaces.MQProducer;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;
import nl.rug.aoop.networking.Client.Client;
import nl.rug.aoop.networking.NetworkMessage.NetworkMessage;

/**
 * Puts messages over the network.
 */
public class NetworkProducer implements MQProducer {
    private final Client client;

    /**
     * Constructor for the NetworkProducer.
     * @param messageQueue The message queue that the network producer can put messages in.
     */
    public NetworkProducer(Client client) {
        this.client = client;
    }

    @Override
    public void put(Message message) {
        client.sendMessage(new NetworkMessage("MQPut", message.toJson()).toJson());
    }
}
