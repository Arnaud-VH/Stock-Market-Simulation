package nl.rug.aoop.messagequeue.TestMessageHandlers;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command.CommandHandler;
import nl.rug.aoop.messagequeue.MessageHandlers.CommandMessageHandler;
import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.networking.Handlers.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Disabled
public class TestCommandMessageHandler {

    MessageHandler messageHandler;
    @BeforeEach
    void setUp() {
        this.messageHandler = new CommandMessageHandler(null);
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

    @Test
    void testExecute() {
        CommandHandler mockHandler = Mockito.mock(CommandHandler.class);
        this.messageHandler = new CommandMessageHandler(mockHandler);
        Message msg = new Message("header","body");
        Map<String, Object> map = new HashMap<>();
        map.put("header",msg.getHeader());
        map.put("body",msg.getBody());
        messageHandler.handleMessage(msg.toJson());
        Mockito.verify(mockHandler).execute("header",map);
    }

}
