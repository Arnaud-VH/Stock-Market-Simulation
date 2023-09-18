package nl.rug.aoop.messagequeue;

/**
 * Unordered queue is a message queue where messages are enqueued based on arrival time.
 */
public class UnorderedQueue implements MessageQueue {
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
