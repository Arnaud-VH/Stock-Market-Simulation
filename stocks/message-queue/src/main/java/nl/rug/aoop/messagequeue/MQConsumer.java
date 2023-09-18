package nl.rug.aoop.messagequeue;

public interface MQConsumer {
    public Message poll();
}
