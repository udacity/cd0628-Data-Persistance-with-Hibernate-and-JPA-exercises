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
 * JDBC URL and credentials are read from application.yml.
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

        // Pool size — parameterized so the benchmark can vary it
        config.setMaximumPoolSize(maximumPoolSize);

        // Connection timeout: how long a thread waits for a connection (30 seconds)
        config.setConnectionTimeout(30_000);

        // Idle timeout: how long idle connections stay open (10 minutes)
        config.setIdleTimeout(600_000);

        // Max lifetime: maximum age of any connection (30 minutes)
        config.setMaxLifetime(1_800_000);

        return config;
    }
}