package nl.rug.aoop.messagequeue;

import lombok.Getter;

@Getter
public class Producer implements MQProducer {
    private final MessageQueue messageQueue;

    public Producer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void put(Message message) {
        messageQueue.enqueue(message);
    }
}
