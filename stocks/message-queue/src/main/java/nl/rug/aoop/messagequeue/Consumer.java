package nl.rug.aoop.messagequeue;

import lombok.Getter;
import nl.rug.aoop.messagequeue.Interfaces.MQConsumer;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;

/**
 * Consumer class that can interact with the Message Queue.
 */
public class Consumer implements MQConsumer {
    @Getter
    private final MessageQueue messageQueue;

    /**
     * Constructor for the consumer.
     * @param messageQueue The message queue that the consumer polls from.
     */
    public Consumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public Message poll() {
        return messageQueue.dequeue();
    }
}
