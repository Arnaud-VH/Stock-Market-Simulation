package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

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
        assertTrue(methods.contains("enqueue"));
        assertTrue(methods.contains("dequeue"));
        assertTrue(methods.contains("getSize"));
    }

    @Test
    void testQueueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
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
