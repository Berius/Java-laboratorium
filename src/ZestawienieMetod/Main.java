package ZestawienieMetod;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int TASKS = 10_000;
    private static final int THREADS = 8;

    public static void main(String[] args) throws Exception {
        System.out.println("Test z " + TASKS + " zadaniami przy użyciu " + THREADS + " wątków\n");

        measureWithCountDownLatch();
        measureWithCyclicBarrier();
        measureWithWhile();
    }

    static Runnable simulatedTask() {
        return () -> {
            try {
                Thread.sleep((long) (Math.random() * 5));
            } catch (InterruptedException ignored) {}
        };
    }

    static void measureWithCountDownLatch() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        long start = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(TASKS);
        for (int i = 0; i < TASKS; i++) {
            executor.submit(() -> {
                simulatedTask().run();
                latch.countDown();
            });
        }

        latch.await();
        long duration = System.currentTimeMillis() - start;
        executor.shutdown();
        System.out.println("CountDownLatch: " + duration + " ms");
    }

    static void measureWithCyclicBarrier() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        long start = System.currentTimeMillis();

        final int groupSize = THREADS;
        int fullGroups = TASKS / groupSize;

        CyclicBarrier barrier = new CyclicBarrier(groupSize);
        CountDownLatch doneLatch = new CountDownLatch(fullGroups * groupSize);

        for (int i = 0; i < fullGroups * groupSize; i++) {
            executor.submit(() -> {
                simulatedTask().run();
                try {
                    barrier.await();
                } catch (Exception ignored) {}
                doneLatch.countDown();
            });
        }

        doneLatch.await();
        long duration = System.currentTimeMillis() - start;
        executor.shutdown();
        System.out.println("CyclicBarrier: " + duration + " ms");
    }

    static void measureWithWhile() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        long start = System.currentTimeMillis();

        Object lock = new Object();
        int[] completed = {0};

        for (int i = 0; i < TASKS; i++) {
            executor.submit(() -> {
                simulatedTask().run();
                synchronized (lock) {
                    completed[0]++;
                    lock.notify();
                }
            });
        }

        synchronized (lock) {
            while (completed[0] < TASKS) {
                lock.wait();
            }
        }

        long duration = System.currentTimeMillis() - start;
        executor.shutdown();
        System.out.println("While/wait-notify: " + duration + " ms");
    }
}