package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.Queues.OrderedQueuee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestConsumer {
    Consumer consumer = null;
    OrderedQueuee queue = null;

    @BeforeEach
    void setUp() {
        this.queue = new OrderedQueuee();
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

    @Test
    void testPollEmpty() {
        assertNull(consumer.poll());
    }
}
