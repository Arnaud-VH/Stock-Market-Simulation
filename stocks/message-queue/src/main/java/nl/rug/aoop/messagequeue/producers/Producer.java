package nl.rug.aoop.messagequeue.producers;

import lombok.Getter;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.Message;

/**
 * Producer class that creates messages that are put int the message queue.
 */
public class Producer implements MQProducer {
    @Getter
    private final MessageQueue messageQueue;

    /**
     * Constructor for the Producer.
     * @param messageQueue The message queue that the producer can put messages in.
     */
    public Producer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void put(Message message) {
        messageQueue.enqueue(message);
    }
}
