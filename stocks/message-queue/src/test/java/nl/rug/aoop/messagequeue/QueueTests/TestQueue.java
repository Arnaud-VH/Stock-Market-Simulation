package nl.rug.aoop.messagequeue.QueueTests;

import nl.rug.aoop.messagequeue.Queues.MessageQueue;
import nl.rug.aoop.messagequeue.Queues.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TestQueue {
    protected MessageQueue queue;
    @BeforeEach
    abstract void setUp();

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
        assertEquals(0, queue.getSize());
    }

    @Test
    void testQueueMethods() {
        List<Method> methods = List.of(queue.getClass().getMethods());
        ArrayList<String> methodNames = new ArrayList<String>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("enqueue"));
        assertTrue(methodNames.contains("dequeue"));
        assertTrue(methodNames.contains("getSize"));
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

        assertEquals(0, queue.getSize());

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());

        Message message = queue.dequeue();
        assertEquals(2, queue.getSize());
        message = queue.dequeue();
        assertEquals(1, queue.getSize());
        message = queue.dequeue();
        assertEquals(0, queue.getSize());
    }
}
