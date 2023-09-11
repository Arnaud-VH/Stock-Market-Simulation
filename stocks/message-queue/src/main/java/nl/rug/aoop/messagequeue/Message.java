package nl.rug.aoop.messagequeue;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Message {
    private final String header;
    private final String body;
    private final LocalDateTime timestamp;

    public Message(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = LocalDateTime.now();
    }
}
