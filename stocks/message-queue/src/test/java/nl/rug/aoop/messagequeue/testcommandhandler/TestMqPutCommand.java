package nl.rug.aoop.messagequeue.testcommandhandler;

import nl.rug.aoop.messagequeue.commandhandler.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestMqPutCommand {
    private MqPutCommand command;
    private MessageQueue queue;
    @BeforeEach
    void setUp() {
        this.queue = new OrderedQueue();
        this.command = new MqPutCommand(queue);
    }
    @Test
    void testConstructor() {
        assertNotNull(command);
    }

    @Test
    void testMethods() {
        List<Method> methods = List.of(command.getClass().getMethods());
        ArrayList<String> methodNames = new ArrayList<String>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("execute"));
    }
    @Test
    void testExecute() {
        Message msg = new Message("header","body");
        String body = msg.toJson();
        Map<String,Object> map = new HashMap<>();
        map.put("body", body);
        command.execute(map);
        Message dequeued = queue.dequeue();
        assertEquals(msg.getHeader(), dequeued.getHeader());
        assertEquals(msg.getBody(), dequeued.getBody());
    }
}
