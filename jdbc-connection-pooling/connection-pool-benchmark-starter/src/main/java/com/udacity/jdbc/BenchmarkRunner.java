package com.udacity.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runs a side-by-side benchmark of two HikariCP pool configurations.
 *
 * For each pool size (2 and 10), launches 20 concurrent threads that
 * each execute QUERIES_PER_THREAD findById calls against the customer
 * table, then prints the resulting throughput in queries per second.
 *
 * No TODOs here. This class is complete and runs automatically on
 * application startup.
 */
@Component
public class BenchmarkRunner implements CommandLineRunner {

    private static final int THREAD_COUNT = 20;
    private static final int QUERIES_PER_THREAD = 500;
    private static final int[] POOL_SIZES_TO_COMPARE = {2, 10};

    private final HikariConfigFactory configFactory;

    public BenchmarkRunner(HikariConfigFactory configFactory) {
        this.configFactory = configFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println();
        System.out.println("========================================");
        System.out.println(" HikariCP Connection Pool Benchmark");
        System.out.println("========================================");
        System.out.println();

        for (int poolSize : POOL_SIZES_TO_COMPARE) {
            HikariConfig config = configFactory.createConfig(poolSize);
            try (HikariDataSource ds = new HikariDataSource(config)) {
                double throughput = runBenchmark(ds);
                System.out.printf("pool=%2d  threads=%d  : throughput = %,.1f queries/sec%n",
                        poolSize, THREAD_COUNT, throughput);
            }
        }

        System.out.println();
        System.out.println("========================================");
        System.out.println();
    }

    private double runBenchmark(DataSource dataSource) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger();
        CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(THREAD_COUNT);

        for (int t = 0; t < THREAD_COUNT; t++) {
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    for (int i = 0; i < QUERIES_PER_THREAD; i++) {
                        long id = ThreadLocalRandom.current().nextLong(1, 101);
                        try (Connection conn = dataSource.getConnection();
                             PreparedStatement ps = conn.prepareStatement(
                                     "SELECT id, email FROM customer WHERE id = ?")) {
                            ps.setLong(1, id);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    successCount.incrementAndGet();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        long startTimeNs = System.nanoTime();
        start.countDown();
        done.await();
        long elapsedNs = System.nanoTime() - startTimeNs;

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        double elapsedSeconds = elapsedNs / 1_000_000_000.0;
        return successCount.get() / elapsedSeconds;
    }
}