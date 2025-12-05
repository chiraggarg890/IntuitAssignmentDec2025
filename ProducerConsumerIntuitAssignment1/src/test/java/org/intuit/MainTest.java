package org.intuit;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testMainRunsWithInput() throws Exception {

        String input = "2\n2\n5\n1\n";  // producers, consumers, bufferSize, duration
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();

        // Wait enough time for Main to complete (duration is 1 second)
        t.join(4000);

        assertFalse(t.isAlive(), "Main should terminate after completing execution");
    }

    @Test
    public void testMainInterrupt() throws Exception {

        String input = "1\n1\n3\n5\n"; // runs 5 sec normally
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();

        Thread.sleep(500);
        t.interrupt();   // Interrupt main early

        t.join(3000);

        assertFalse(t.isAlive(), "Main must stop when interrupted");
    }
}

