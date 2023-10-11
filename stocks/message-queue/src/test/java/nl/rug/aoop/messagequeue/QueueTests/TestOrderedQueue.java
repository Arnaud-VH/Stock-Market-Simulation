package nl.rug.aoop.messagequeue.QueueTests;

import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.messagequeue.Queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderedQueue extends TestQueue {

    @Override
    @BeforeEach
    void setUp() {
        super.queue = new OrderedQueue();
    }

    @Test // alternative enqueue test with explicitly different timestamps
    void testQueueEnqueueDiffTimestamps() throws InterruptedException {
        Message message1 = new Message("header", "body", LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(1);
        Message message2 = new Message("header", "body", LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(1);
        Message message3 = new Message("header", "body", LocalDateTime.now());

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        System.out.println(queue.getSize());

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Disabled
    @Test // alternative enqueue test with explicitly same timestamps to test collisions
    void testQueueEnqueueSameTimestamps() {
        LocalDateTime t = LocalDateTime.now();
        Message message1 = new Message("header", "body", t);
        Message message2 = new Message("header", "body", t);
        Message message3 = new Message("header", "body", t);

        queue.enqueue(message1);
        queue.enqueue(message2);
        queue.enqueue(message3);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

}
