package nl.rug.aoop.messagequeue.consumers;

import lombok.Getter;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.Message;

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
