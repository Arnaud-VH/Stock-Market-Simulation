package nl.rug.aoop.messagequeue.testqueues;

import nl.rug.aoop.messagequeue.queues.OrderedBlockingQueue;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOrderedBlockingQueue extends TestOrderedQueue {
    @Override
    @BeforeEach
    void setUp() {
        super.queue = new OrderedBlockingQueue();
    }

    // add test for checking thread safety

}
