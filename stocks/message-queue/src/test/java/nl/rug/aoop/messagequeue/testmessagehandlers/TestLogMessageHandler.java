package nl.rug.aoop.messagequeue.testmessagehandlers;

import nl.rug.aoop.messagequeue.messagehandlers.LogMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestLogMessageHandler {
    LogMessageHandler messageHandler = null;
    @BeforeEach
    void setUp() {
        this.messageHandler = new LogMessageHandler();
    }
    @Test
    void testLogMessageMethods() {
        List<Method> methods = List.of(messageHandler.getClass().getDeclaredMethods());
        ArrayList<String> methodNames = new ArrayList<>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("handleMessage"));
    }
}
