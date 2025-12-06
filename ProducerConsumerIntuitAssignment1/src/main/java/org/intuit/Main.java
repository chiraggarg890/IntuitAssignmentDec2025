package org.intuit;

import org.intuit.consumer.Consumer;
import org.intuit.producer.Producer;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Main class to execute the multi-producer and multi-consumer system.
 * <p>
 * Features:
 * - Takes user input for number of producers, consumers, and buffer size.
 * - Uses ArrayBlockingQueue for thread-safe blocking queue operations.
 * - Starts all producers and consumers as independent threads.
 * - Automatically stops all threads after a user-specified duration.
 */
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numProducers = readPositiveInt(sc, "number of producers (>= 1)");
        int numConsumers = readPositiveInt(sc, "number of consumers (>= 1)");
        int bufferSize = readPositiveInt(sc, "buffer size (>= 1)");
        int duration = readPositiveInt(sc, "program run duration in seconds (>= 0)");

        sc.close();

        // Shared BlockingQueue
        List<Thread> threads = getThreads(bufferSize, numProducers, numConsumers);

        // Allow program to run for specified duration
        try {
            Thread.sleep(duration * 1000L);
        } catch (InterruptedException ignored) {
        }

        System.out.println("\n=== Stopping all threads ===");

        // Interrupt all producer and consumer threads
        for (Thread t : threads) {
            t.interrupt();
        }

        // Wait for all threads to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("=== All threads stopped. Program exiting. ===");
    }

    private static List<Thread> getThreads(int bufferSize, int numProducers, int numConsumers) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(bufferSize);

        List<Thread> threads = new ArrayList<>();

        // Start producers
        for (int i = 1; i <= numProducers; i++) {
            Thread t = new Thread(new Producer(queue, i));
            threads.add(t);
            t.start();
        }

        // Start consumers
        for (int i = 1; i <= numConsumers; i++) {
            Thread t = new Thread(new Consumer(queue, i));
            threads.add(t);
            t.start();
        }
        return threads;
    }

    /**
     * Reads a positive integer (>=1) from Scanner, prompting until valid value is entered.
     */
    private static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print("Enter " + prompt + ": ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input: not an integer. Please try again.");
                sc.next(); // consume invalid token
                continue;
            }
            int val = sc.nextInt();
            if (val < 1) {
                System.out.println("Invalid input: must be >= 1. Please try again.");
                continue;
            }
            return val;
        }
    }

}


