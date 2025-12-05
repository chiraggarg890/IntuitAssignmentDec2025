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

        System.out.print("Enter number of producers: ");
        int numProducers = sc.nextInt();

        System.out.print("Enter number of consumers: ");
        int numConsumers = sc.nextInt();

        System.out.print("Enter buffer size: ");
        int bufferSize = sc.nextInt();

        System.out.print("Enter program run duration (in seconds): ");
        int duration = sc.nextInt();

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
}


