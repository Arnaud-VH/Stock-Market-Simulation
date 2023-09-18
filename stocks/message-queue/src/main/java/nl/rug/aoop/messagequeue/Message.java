package nl.rug.aoop.messagequeue;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Message class. Messages go into Message queues.
 */
@Getter
public class Message {
    private final String header;
    private final String body;
    private final LocalDateTime timestamp;

    /**
     * Constructor for the Messages.
     * @param messageHeader The header (Title) of the message.
     * @param messageBody The text content of the message.
     */
    public Message(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for messages to specify custom timestamp
     * @param messageHeader The header (Title) of the message.
     * @param messageBody The text content of the message.
     * @param messageTimestamp The timestamp of the message.
     */
    public Message(String messageHeader, String messageBody, LocalDateTime messageTimestamp) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = messageTimestamp;
    }
}
