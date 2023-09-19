package nl.rug.aoop.messagequeue;

/**
 * Interface that provides the functionality for the message queues.
 */
public interface MessageQueue {
    /**
     * Enqueue puts a message into the message queue.
     *
     * @param message The message that is enqueued into the queue.
     */
    void enqueue(Message message);

    /**
     * Dequeue takes a message out of the message queue.
     *
     * @return The dequeued message.
     */
    Message dequeue();

    /**
     * Gets the size of the message queues.
     *
     * @return Returns size of queue.
     */
    int getSize();
}
