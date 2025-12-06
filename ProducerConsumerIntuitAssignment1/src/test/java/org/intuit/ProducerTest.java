package org.intuit;

import org.intuit.producer.Producer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class ProducerTest {

    /**
     * Producer generates unique messages and inserts into queue.
     */
    @Test
    public void testProducerProducesMessages() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        Producer producer = new Producer(queue, 1);
        Thread t = new Thread(producer);

        t.start();
        Thread.sleep(600);
        t.interrupt();
        t.join();

        assertTrue(queue.size() > 0, "Producer should have produced items");
    }

    /**
     * Producer interruptions inside queue.put()
     */
    @Test
    public void testProducerInterruptDuringPut() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        queue.put("FULL"); // fill queue to force blocking

        Producer producer = new Producer(queue, 99);
        Thread t = new Thread(producer);

        t.start();
        Thread.sleep(200);
        t.interrupt();
        t.join();

        assertTrue(t.getState() == Thread.State.TERMINATED);
    }

    /**
     * Test 3 → Producer interruption during Thread.sleep()
     */
    @Test
    public void testProducerInterruptDuringSleep() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

        Producer producer = new Producer(queue, 5);
        Thread t = new Thread(producer);

        t.start();
        Thread.sleep(100);
        t.interrupt(); // will interrupt during sleep
        t.join();

        assertFalse(t.isAlive(), "Producer thread must exit on interrupt");
    }
}
