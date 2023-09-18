package nl.rug.aoop.messagequeue;

import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class Consumer implements MQConsumer {
    private final MessageQueue messageQueue;
    public Consumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public Message poll() {
        return messageQueue.dequeue();
    }
}
