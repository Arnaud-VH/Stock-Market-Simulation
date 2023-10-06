package nl.rug.aoop.messagequeue.Queues;

import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class OrderedBlockingQueue extends OrderedQueue {
    public OrderedBlockingQueue () {
        super(new PriorityBlockingQueue<>());
    }
}
