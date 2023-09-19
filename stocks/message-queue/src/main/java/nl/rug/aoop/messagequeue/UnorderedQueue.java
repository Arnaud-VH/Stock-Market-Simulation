package nl.rug.aoop.messagequeue;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Unordered queue is a message queue where messages are enqueued based on arrival time.
 */
public class UnorderedQueue implements MessageQueue {
    private final Queue<Message> unorderedQueue;
    public UnorderedQueue() {
        unorderedQueue = new LinkedList<>();
    }

    @Override
    public void enqueue(Message message) {
        if (message == null) {
            System.err.println("Attempting to enqueue a NULL");
        } else {
            unorderedQueue.add(message);
        }
    }

    @Override
    public Message dequeue() {
        if (!unorderedQueue.isEmpty()) {
            return unorderedQueue.remove();
        }
        System.out.println("Attempting to deque an empty queue");
        return null;
    }

    @Override
    public int getSize() {
        return unorderedQueue.size();
    }
}
