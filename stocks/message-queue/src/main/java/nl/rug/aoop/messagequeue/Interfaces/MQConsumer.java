package nl.rug.aoop.messagequeue.Interfaces;

import nl.rug.aoop.messagequeue.Message;

/**
 * Interface that models the behaviour of a Consumer on a message queue.
 */
public interface MQConsumer {
    /**
     * Poll gets a message from the message queue and gives it to the consumer.
     * @return Returns a message.
     */
    Message poll();
}
