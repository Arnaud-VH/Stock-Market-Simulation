package nl.rug.aoop.messagequeue.testproducers;

import nl.rug.aoop.messagequeue.producers.Producer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestProducer {
    private OrderedQueue queue = null;
    private Producer producer = null;

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
        assertEquals(message1, queue.dequeue());
    }

    @Test
    void testPutNull() {
        producer.put(null);
        assertEquals(0, queue.getSize());
    }
}
