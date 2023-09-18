package nl.rug.aoop.messagequeue;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
        ArrayList<String> methodNames = new ArrayList<String>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("enqueue"));
        assertTrue(methodNames.contains("dequeue"));
        assertTrue(methodNames.contains("getSize"));
    }

    @Disabled
    @Test
    void testQueueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        System.out.println(message1.getTimestamp());
        System.out.println(message2.getTimestamp());
        System.out.println(message3.getTimestamp());

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Test
    void testQueueEnqueueDiffTimestamps() throws InterruptedException {
        Message message1 = new Message("header", "body");
        TimeUnit.MILLISECONDS.sleep(1);
        Message message2 = new Message("header", "body");
        TimeUnit.MILLISECONDS.sleep(1);
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        System.out.println(queue.getSize());

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Test
    void testQueueEnqueueSameTimestamps() throws InterruptedException {
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

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }

}
