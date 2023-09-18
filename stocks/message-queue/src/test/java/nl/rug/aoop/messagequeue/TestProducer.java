package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestProducer {
    OrderedQueue queue = null;
    Producer producer = null;

    @BeforeEach
    void setUp() {
        this.queue = new OrderedQueue();
        this.producer = new Producer(queue);
    }

    @Test
    void testProducerConstructor() {
        assertNotNull(producer.getMessageQueue());
    }

    @Test
    void testProducerMethods() {
        List<Method> methods = List.of(producer.getClass().getDeclaredMethods());
        ArrayList<String> methodNames = new ArrayList<>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("put"));
    }

    @Test
    void testPut() {
        Message message1 = new Message("header", "body");
        producer.put(message1);
        assertEquals(queue.dequeue(), message1);
    }
}
