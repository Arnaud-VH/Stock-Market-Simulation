package nl.rug.aoop.messagequeue.Queues;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;
import nl.rug.aoop.messagequeue.Message;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
public class OrderedQueue implements MessageQueue {
    protected final Queue<Message> queue;

    public OrderedQueue () {
        this.queue = new PriorityQueue<>();
    }

    protected OrderedQueue (Queue<Message> queue) {
        this.queue = queue;
    }

    @Override
    public void enqueue(Message message) {
        if (message == null) {
            log.error("Attempting to enqueue NULL");
        } else { // else enqueue
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
