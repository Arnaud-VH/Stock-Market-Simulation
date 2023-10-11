package nl.rug.aoop.messagequeue.TestQueues;

import nl.rug.aoop.messagequeue.Queues.Message;
import nl.rug.aoop.messagequeue.Queues.UnorderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnorderedQueue extends TestQueue {

    @Override
    @BeforeEach
    void setUp() {
        super.queue = new UnorderedQueue();
    }

    @Test
    void testQueueOrdering() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message3, queue.dequeue());
        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
    }

}
