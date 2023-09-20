package nl.rug.aoop.messagequeue;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of a message queue. Here the message queue is ordered by timestamp.
 */
public class OrderedQueue implements MessageQueue {
    private final SortedMap<LocalDateTime, LinkedList<Message>> orderedQueue;

    /**
     * The constructor for the ordered queue.
     */
    public OrderedQueue() {
        orderedQueue = new TreeMap<>();
    }

    @Override
    public void enqueue(Message message) {
        if (message == null) {
            System.err.println("Attempting to enqueue NULL");
        } else {
            LocalDateTime key = message.getTimestamp();
            if (orderedQueue.containsKey(key)) {
                orderedQueue.get(key).add(message);
            } else {
                LinkedList<Message> l = new LinkedList<>();
                l.add(message);
                orderedQueue.put(key, l);
            }
        }
    }

    /**
     * Dequeues item from sorted queue. Uses linear probing.
     * @return null if queue is empty, else head of queue.
     */
    @Override
    public Message dequeue() {

        if (!orderedQueue.isEmpty()) {
            LocalDateTime key = orderedQueue.firstKey();
            Message message = orderedQueue.get(key).pop();
            if (orderedQueue.get(key).isEmpty()) {
                orderedQueue.remove(key);
            }
            return message;
        } else {
            System.err.println("Attempting to dequeue an empty queue");
            return null;
        }
    }

    @Override
    public int getSize() {
        int size = 0;
        for (LinkedList<Message> value : orderedQueue.values()) {
            size += value.size();
        }
        return size;
    }
}
