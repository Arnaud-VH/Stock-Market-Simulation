package nl.rug.aoop.messagequeue;

import lombok.Getter;

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
