package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnorderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new UnorderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
        assertEquals(0, queue.getSize());
    }

    @Test
    void testQueueMethods() {
        List<Method> methods = List.of(queue.getClass().getDeclaredMethods());
        int count = 0;
        for (Method m : methods) {
            if (m.getName().equals("enqueue")) count++;
            if (m.getName().equals("dequeue")) count++;
            if (m.getName().equals("getSize")) count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testEnqueueNull() {
        assertThrows(NullPointerException.class, ()-> queue.enqueue(null));
    }

    @Test
    void testDequeueEmptyQueue() {
        assertThrows(NoSuchElementException.class, ()-> queue.dequeue());
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

    @Test
    void testSizeWhenNull() {
        MessageQueue test = null;
        assertThrows(NullPointerException.class, () -> test.getSize());
    }

    @Test
    void testQueueOrdering() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message3, queue.dequeue());
        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
    }

}
