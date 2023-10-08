package nl.rug.aoop.messagequeue.Queues;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;
import nl.rug.aoop.messagequeue.Message;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of a message queue. Here the message queue is ordered by timestamp.
 */
@Slf4j
public class OrderedQueuee implements MessageQueue {
    private final SortedMap<LocalDateTime, LinkedList<Message>> orderedQueue;

    /**
     * The constructor for the ordered queue.
     */
    public OrderedQueuee() {
        orderedQueue = new TreeMap<>();
    }

    /**
     * Enqueue for the orderded Queue. Uses Linear Chaining in the case of messages with the same timeStamp.
     * @param message The message that is enqueued into the queue.
     */
    @Override
    public void enqueue(Message message) {
        // if trying to enqueue null print error and do nothing
        if (message == null) {
            log.error("Attempting to enqueue NULL");
        } else { // else enqueue
            LocalDateTime key = message.getTimestamp();
            // if list already exists (we have a key collision) add message to list
            if (orderedQueue.containsKey(key)) {
                orderedQueue.get(key).add(message);
            } else { // else create list and add to map
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
        // if queue not empty dequeue
        if (!orderedQueue.isEmpty()) {
            LocalDateTime key = orderedQueue.firstKey();
            // each key is a list for linear probing
            Message message = orderedQueue.get(key).pop();
            // if pop was last element in the list, remove the list
            if (orderedQueue.get(key).isEmpty()) {
                orderedQueue.remove(key);
            }
            return message;
        } else { // else print error and return null
            log.error("Attempting to dequeue an empty queue");
            return null;
        }
    }

    @Override
    public int getSize() {
        int size = 0;
        // for all lists in the map check size and add up
        for (LinkedList<Message> value : orderedQueue.values()) {
            size += value.size();
        }
        return size;
    }
}
