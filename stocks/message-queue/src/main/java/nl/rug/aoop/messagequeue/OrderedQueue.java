package nl.rug.aoop.messagequeue;

import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 * Implementation of a message queue. Here the message queue is ordered by timestamp.
 */
public class OrderedQueue implements MessageQueue {
    private final TreeMap<LocalDateTime,Message> orderedQueue;

    public OrderedQueue () {
        orderedQueue = new TreeMap<>();
    }

    @Override
    public void enqueue(Message message) {
        orderedQueue.put(message.getTimestamp(), message);
    }

    @Override
    public Message dequeue() {
        Message message = orderedQueue.get(orderedQueue.firstKey());
        return message;
    }

    @Override
    public int getSize() {
        return orderedQueue.size();
    }
}
