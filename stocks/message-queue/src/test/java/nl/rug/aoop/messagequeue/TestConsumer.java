package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestConsumer {
    Consumer consumer = null;
    OrderedQueue queue = null;

    @BeforeEach
    void setUp() {
        this.queue = new OrderedQueue();
        this.consumer = new Consumer(queue);
    }

    @Test
    void testConsumerConstructor() {
        assertNotNull(consumer.getMessageQueue());
    }

    @Test
    void testConsumerMethods() {
        List<Method> methods = List.of(consumer.getClass().getDeclaredMethods());
        ArrayList<String> methodNames = new ArrayList<>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("poll"));
    }

    @Test
    void testPoll() {
        Message message1 = new Message("header", "body");
        queue.enqueue(message1);
        assertEquals(consumer.poll(), message1);
    }

    // it would be good to have a test for dequeue on an empty queue, however what should that result in? A null?
}
