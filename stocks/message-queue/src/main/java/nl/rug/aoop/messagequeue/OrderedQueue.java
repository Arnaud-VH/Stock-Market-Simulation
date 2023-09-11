package nl.rug.aoop.messagequeue;

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
