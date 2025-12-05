package org.intuit.producer;

import java.util.concurrent.BlockingQueue;

/**
 * Producer class represents a task that generates messages and puts them into a shared BlockingQueue.
 * Multiple producers can run concurrently and safely share the same BlockingQueue.
 * <p>
 * Key behaviors:
 * - Produces unique messages distinguished by producerId.
 * - Sleeps briefly to simulate realistic processing time.
 * - Stops gracefully when interrupted.
 */
public class Producer implements Runnable {

    private final BlockingQueue<String> queue;  // Shared buffer
    private final int producerId;               // Unique identifier for each producer
    private int counter = 0;                    // Ensures unique messages per producer

    public Producer(BlockingQueue<String> queue, int producerId) {
        this.queue = queue;
        this.producerId = producerId;
    }

    @Override
    public void run() {
        try {
            // Continue producing until thread is interrupted
            while (!Thread.currentThread().isInterrupted()) {

                // Create unique message for this producer
                String message = "Producer-" + producerId + ": Item-" + counter++;

                // Blocks if queue is full
                queue.put(message);

                System.out.println("[PRODUCED] " + message);

                // Slow down production for demonstration purposes
                Thread.sleep(400);
            }

        } catch (InterruptedException e) {
            System.out.println("Producer " + producerId + " interrupted.");
            Thread.currentThread().interrupt(); // Restore interrupt flag
        }
    }
}

