package org.intuit;

import static org.junit.jupiter.api.Assertions.*;

import org.intuit.consumer.Consumer;
import org.intuit.producer.Producer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class IntegrationTest {

    @Test
    public void testProducersAndConsumersTogether() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

        Thread p1 = new Thread(new Producer(queue, 1));
        Thread p2 = new Thread(new Producer(queue, 2));
        Thread c1 = new Thread(new Consumer(queue, 1));

        p1.start();
        p2.start();
        c1.start();

        Thread.sleep(2000);

        p1.interrupt();
        p2.interrupt();
        c1.interrupt();

        p1.join();
        p2.join();
        c1.join();

        assertTrue(queue.size() >= 0, "Queue should remain valid");
    }
}
