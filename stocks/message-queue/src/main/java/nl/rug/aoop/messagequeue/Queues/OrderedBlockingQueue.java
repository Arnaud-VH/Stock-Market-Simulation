package nl.rug.aoop.messagequeue.Queues;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Is an OrderedQueue but with a priorityBlocking queue implementation.
 */
public class OrderedBlockingQueue extends OrderedQueue {
    /**
     * Constructor for the OrderedBlockingQueue.
     */
    public OrderedBlockingQueue() {
        super(new PriorityBlockingQueue<>());
    }
}
