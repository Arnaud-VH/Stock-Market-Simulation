package nl.rug.aoop.messagequeue.testcommandhandler;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.command.Command;
import nl.rug.aoop.messagequeue.commandhandler.QueueCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestQueueCommandHandler {
    private QueueCommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        try {
            this.commandHandler = QueueCommandHandler.getInstance();
        } catch (Exception e) {
            log.error("command handler should be singleton");
        }

    }
    @Test
    void testMethods() {
        List<Method> methods = List.of(commandHandler.getClass().getMethods());
        ArrayList<String> methodNames = new ArrayList<String>();
        for (Method m : methods) {
            methodNames.add(m.getName());
        }
        assertTrue(methodNames.contains("registerCommand"));
        assertTrue(methodNames.contains("execute"));
    }

    /**
     * A temporary command used to test the Command Handler.
     * This enables us to test command handler without depending on MQPutCommand nor CommandHandlerFactory.
     * The command manipulates an arbitrary object (Queue), the test then sees if the object has indeed been manipulated after
     * calling the command in the commandHandler.
     */
    private class TestCommand implements Command {
        Queue<String> q;
        public TestCommand (Queue<String> q) {
            this.q = q;
        }
        @Override
        public void execute(Map<String, Object> params) {
            q.add((String)params.get("body"));
        }
    }
    @Test
    void testExecute() {
        Queue<String> queue = new LinkedList<>();
        commandHandler.registerCommand("test", new TestCommand(queue));
        Map<String, Object> map = new HashMap<>();
        map.put("body","true");
        commandHandler.execute("test",map);
        assertEquals(queue.poll(), "true");
    }

}
