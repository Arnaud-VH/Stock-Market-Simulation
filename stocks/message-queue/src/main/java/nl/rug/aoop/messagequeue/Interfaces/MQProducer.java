package nl.rug.aoop.messagequeue.Interfaces;

import nl.rug.aoop.messagequeue.Message;

/**
 * Models the behaviour of a Producer with a Message Queue.
 */
public interface MQProducer {

    /**
     * Adds a message into the message queue.
     * @param message The message that is put into the message queue.
     */
    void put(Message message);
}
