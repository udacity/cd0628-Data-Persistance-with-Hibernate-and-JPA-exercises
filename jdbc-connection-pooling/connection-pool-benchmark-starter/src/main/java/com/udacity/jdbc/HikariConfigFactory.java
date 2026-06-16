package com.udacity.jdbc;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Builds HikariConfig instances for the benchmark.
 *
 * The benchmark calls createConfig twice with different pool sizes
 * (2 and 10) so it can compare throughput between them.
 *
 * JDBC URL and credentials are read from application.yml. The four
 * pool-tuning values are filled in by the learner below.
 */
@Component
public class HikariConfigFactory {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public HikariConfigFactory(
            @Value("${spring.datasource.url}") String jdbcUrl,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public HikariConfig createConfig(int maximumPoolSize) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("benchmark-pool-" + maximumPoolSize);
        config.setLeakDetectionThreshold(10_000);

        // ============================================================
        // TODO 3: Call setMaximumPoolSize
        //
        // Use the parameter passed into this method (maximumPoolSize).
        // The benchmark calls createConfig twice with different values
        // (2 and 10) so it can compare them.
        //
        // Follow the pattern of the setJdbcUrl(jdbcUrl) line above.
        // ============================================================


        // ============================================================
        // TODO 4: Call setConnectionTimeout (milliseconds)
        //
        // Controls how long a thread will wait for a connection from
        // the pool before failing with a timeout.
        //
        // Recommended starting value: 30 seconds (30_000 ms)
        //
        // Follow the same setXxx() pattern.
        // ============================================================


        // ============================================================
        // TODO 5: Call setIdleTimeout (milliseconds)
        //
        // Controls how long idle connections stay open before being
        // released back to the database.
        //
        // Recommended starting value: 10 minutes (600_000 ms)
        // ============================================================


        // ============================================================
        // TODO 6: Call setMaxLifetime (milliseconds)
        //
        // Maximum lifetime of any single connection in the pool.
        // After this duration, the connection is closed and replaced.
        //
        // Recommended starting value: 30 minutes (1_800_000 ms)
        // ============================================================


        return config;
    }
}