package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.Interfaces.MQProducer;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;

/**
 * Puts messages over the network.
 */
public class NetworkProducer implements MQProducer {
    private final MessageQueue messageQueue;

    /**
     * Constructor for the NetworkProducer.
     * @param messageQueue The message queue that the network producer can put messages in.
     */
    public NetworkProducer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void put(Message message) {
        messageQueue.enqueue(message);
    }
}
