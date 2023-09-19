package nl.rug.aoop.messagequeue;

public interface MQProducer {
    void put(Message message);
}
