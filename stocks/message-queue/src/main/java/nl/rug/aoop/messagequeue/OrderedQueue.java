package nl.rug.aoop.messagequeue;

/**
 * Implementation of a message queue. Here the message queue is ordered by timestamp.
 */
public class OrderedQueue implements MessageQueue {
    @Override
    public void enqueue(Message message) {

    }

    @Override
    public Message dequeue() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }
}
