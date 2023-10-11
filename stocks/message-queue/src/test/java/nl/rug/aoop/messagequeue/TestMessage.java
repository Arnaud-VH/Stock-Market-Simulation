package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.Queues.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestMessage {

    private Message message;
    private String messageHeader;
    private String messageBody;


    @BeforeEach
    void setUp() {
        messageHeader = "header";
        messageBody = "body";
        message = new Message(messageHeader, messageBody);
    }


    @Test
    void testMessageConstructor() {
        assertEquals(messageHeader, message.getHeader());
        assertEquals(messageBody, message.getBody());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testMessageImmutable() {
        List<Field> fields = List.of(Message.class.getDeclaredFields());
        fields.forEach(field -> assertTrue(Modifier.isFinal(field.getModifiers()), field.getName() + " is not final"));
    }

    @Test
    void testMessageJson() {
        Message messageCopy = Message.fromJson(message.toJson());
        assertEquals(messageCopy.getHeader(),message.getHeader());
        assertEquals(messageCopy.getBody(), message.getBody());
        assertEquals(messageCopy.getTimestamp(), message.getTimestamp());
    }

    @Test
    void testNullMessageJson() {
        String json = null;
        Message message = Message.fromJson(json);
        assertNull(message);
    }
}
