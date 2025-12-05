package org.intuit;

import org.intuit.consumer.Consumer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumerTest {

    /**
     * Test 1 → Consumer properly consumes messages.
     */
    @Test
    public void testConsumerConsumes() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        queue.put("A");
        queue.put("B");

        Consumer consumer = new Consumer(queue, 1);
        Thread t = new Thread(consumer);

        t.start();
        Thread.sleep(1200);
        t.interrupt();
        t.join();

        assertEquals(0, queue.size(), "Consumer should empty queue");
    }

    /**
     * Test 2 → Interrupt while queue.take() is blocking
     */
    @Test
    public void testConsumerInterruptDuringTake() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        Consumer consumer = new Consumer(queue, 3);
        Thread t = new Thread(consumer);

        t.start();
        Thread.sleep(200);
        t.interrupt(); // interrupt while consumer is blocked in take()
        t.join();

        assertFalse(t.isAlive());
    }

    /**
     * Test 3 → Interrupt during sleep() after take()
     */
    @Test
    public void testConsumerInterruptDuringSleep() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        queue.put("X");

        Consumer consumer = new Consumer(queue, 2);
        Thread t = new Thread(consumer);

        t.start();
        Thread.sleep(50);
        t.interrupt(); // interrupt during sleep
        t.join();

        assertTrue(t.getState() == Thread.State.TERMINATED);
    }
}
