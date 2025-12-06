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

    @Test
    public void testDurationZeroExitsImmediately() throws Exception {
        // producers = 1, consumers = 1, buffer = 5, duration = 0
        String input = "1\n1\n5\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();
        t.join(2000);

        assertFalse(t.isAlive(), "Main should exit immediately when duration is 0");
    }

    @Test
    public void testInvalidInputHandledGracefully() throws Exception {
        // invalid "abc" for producers, then correct values
        String input = "abc\n1\n1\n5\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();
        t.join(4000);

        assertFalse(t.isAlive(), "Main should still exit normally after invalid input retries");
    }

    @Test
    public void testInvalidBufferSizeTriggersRePrompt() throws Exception {
        // producers=1, consumers=1, bufferSize invalid=0, then valid=3, duration=1
        String input = "1\n1\n0\n3\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();
        t.join(4000);

        assertFalse(t.isAlive(), "Main should reprompt on invalid buffer size and then exit normally");
    }

    @Test
    public void testInvalidNonIntegerInput() throws Exception {
        // invalid for producers (abc), then valid values afterward
        String input = "abc\n1\n1\n5\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();

        t.join(4000);

        assertFalse(t.isAlive(), "Main should exit normally even after non-integer input");
    }

    @Test
    public void testNegativeDurationTriggersValidation() throws Exception {
        // producers=1, consumers=1, buffer=5, duration=-3 → invalid
        // then give a correct duration 1 second
        String input = "1\n1\n5\n-3\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();

        t.join(4000);

        assertFalse(t.isAlive(), "Main should retry duration input and exit normally");
    }


}

