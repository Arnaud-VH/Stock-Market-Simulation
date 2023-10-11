package nl.rug.aoop.messagequeue.Queues;

import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Ordered Queue gives design of a messageQueue where messages are ordered based on timeStamp.
 */
@Slf4j
public class OrderedQueue implements MessageQueue {
    /**
     * Ordinary Queue that is passed down to subclasses.
     */
    protected final Queue<Message> queue;

    /**
     * Class constructor.
     */
    public OrderedQueue() {
        this.queue = new PriorityQueue<>();
    }

    /**
     * Constructor that passes down to subclasses.
     * @param queue The queue implementation that is used in the subclass.
     */
    protected OrderedQueue(Queue<Message> queue) {
        this.queue = queue;
    }

    /**
     * Enqueue's a message into the class's queue.
     * @param message The message that is enqueued into the queue.
     */
    @Override
    public void enqueue(Message message) {
        if (message == null) {
            log.error("Attempting to enqueue NULL");
        } else {
            queue.add(message);
        }
    }

    @Override
    public Message dequeue() {
        if (queue.isEmpty()) {
            log.error("Attempting to dequeue an empty queue");
            return null;
        }
        return queue.poll();
    }

    @Override
    public int getSize() {
        return queue.size();
    }
}
