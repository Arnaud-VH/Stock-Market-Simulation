package nl.rug.aoop.messagequeue.QueueTests;

import nl.rug.aoop.messagequeue.Interfaces.MessageQueue;
import nl.rug.aoop.messagequeue.Message;
import nl.rug.aoop.messagequeue.Queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new OrderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
    }

    @Test
    void testQueueMethods() {
        List<Method> methods = List.of(queue.getClass().getDeclaredMethods());
        ArrayList<String> methodNames = new ArrayList<>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("enqueue"));
        assertTrue(methodNames.contains("dequeue"));
        assertTrue(methodNames.contains("getSize"));
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

    @Test
    void testEnqueueNull() {
        queue.enqueue(null);
        assertEquals(0, queue.getSize());
    }

    @Test
    void testDequeueEmptyQueue() {
        assertNull(queue.dequeue());
    }

    @Test
    void testGetSize() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }

}
