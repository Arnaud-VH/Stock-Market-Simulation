package nl.rug.aoop.messagequeue.testproducers;

import nl.rug.aoop.messagequeue.producers.NetworkProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestNetworkProducer {
    NetworkProducer producer = null;
    @BeforeEach
    void setUp() {
        this.producer = new NetworkProducer(null);
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

}
