package nl.rug.aoop.messagequeue.testcommandhandler;

import nl.rug.aoop.messagequeue.commandhandler.QueueCommandHandler;
import nl.rug.aoop.messagequeue.commandhandler.QueueCommandHandlerFactory;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueueCommandHandlerFactory {
    private QueueCommandHandlerFactory factory;
    @BeforeEach
    void setUp() {
        MessageQueue queue = new OrderedQueue();
        this.factory = new QueueCommandHandlerFactory(queue);
    }
    @Test
    void testConstructor() {
        assertNotNull(factory);
    }

    @Test
    void testMethods() {
        List<Method> methods = List.of(factory.getClass().getMethods());
        ArrayList<String> methodNames = new ArrayList<String>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("createCommandHandler"));
    }

    @Test
    void createCommandHandler() {
        factory.createCommandHandler();
        assertTrue(QueueCommandHandler.getInstance().getCommandMap().containsKey("MQPut"));  // this assumes queueCommandHandler works
    }
}
