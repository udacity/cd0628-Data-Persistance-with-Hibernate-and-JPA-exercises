package com.example.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Entry point for the demo. Configures a HikariCP pool, seeds five events,
 * then reads one back to prove the pool hands out reusable connections.
 */
public class DemoRunner {

    public static void main(String[] args) throws Exception {

        // ---- HikariCP configuration ----
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/banking");
        config.setUsername("banking");
        config.setPassword("banking");

        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30_000);
        config.setIdleTimeout(600_000);
        config.setMaxLifetime(1_800_000);

        try (HikariDataSource dataSource = new HikariDataSource(config)) {

            EventRepository repo = new EventRepository(dataSource);

            // ---- Batch insert: five events, one round trip ----
            List<Event> tour = List.of(
                    new Event("Madison Square Garden", "Fleetwood Mac",
                            LocalDate.of(2026, 8, 1), new BigDecimal("129.00")),
                    new Event("The Fillmore",          "Phoebe Bridgers",
                            LocalDate.of(2026, 8, 5), new BigDecimal("79.00")),
                    new Event("Red Rocks",             "The National",
                            LocalDate.of(2026, 8, 12), new BigDecimal("95.00")),
                    new Event("Radio City Music Hall", "Kacey Musgraves",
                            LocalDate.of(2026, 8, 18), new BigDecimal("110.00")),
                    new Event("The Ryman",             "Jason Isbell",
                            LocalDate.of(2026, 8, 25), new BigDecimal("85.00"))
            );

            repo.batchInsert(tour);
            System.out.println("Inserted " + tour.size() + " events in one batch.");

            // ---- Single lookup: fetch the first event back ----
            Optional<Event> found = repo.findById(1L);
            found.ifPresent(e -> System.out.println("Fetched: " + e));
        }

        // The pool closes cleanly here. All connections drained back to the pool
        // before HikariDataSource.close() shuts down the internal housekeeping.
    }
}
