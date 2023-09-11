package nl.rug.aoop.messagequeue;

public interface MessageQueue {
    void enqueue(Message message);

    Message dequeue();

    int getSize();
}
