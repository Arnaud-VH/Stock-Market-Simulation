package nl.rug.aoop.messagequeue.TestMessageHandlers;

import nl.rug.aoop.command.Command.CommandHandler;
import nl.rug.aoop.messagequeue.MessageHandlers.CommandMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class TestCommandMessageHandler {
    CommandMessageHandler messageHandler = null;
    @BeforeEach
    void setUp() {
        this.messageHandler = new CommandMessageHandler(Mockito.mock(CommandHandler.class));
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
