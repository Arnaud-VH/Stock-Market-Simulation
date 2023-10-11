package nl.rug.aoop.messagequeue.TestCommandHandler;

import nl.rug.aoop.messagequeue.CommandHandler.Commands.MqPutCommand;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandler;
import nl.rug.aoop.messagequeue.CommandHandler.QueueCommandHandlerFactory;
import nl.rug.aoop.messagequeue.Queues.MessageQueue;
import nl.rug.aoop.messagequeue.Queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

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
