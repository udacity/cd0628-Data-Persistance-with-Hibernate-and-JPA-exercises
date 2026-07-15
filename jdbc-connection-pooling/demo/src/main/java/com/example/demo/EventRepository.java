package com.example.demo;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * Demonstrates raw JDBC against a HikariCP-managed DataSource.
 *
 * Two operations:
 *   1. findById - single-row lookup with PreparedStatement + try-with-resources
 *   2. batchInsert - bulk insert with setAutoCommit(false), addBatch, executeBatch, commit
 *
 * The point of the demo is to show how the pool hands out connections, how try-with-resources
 * returns them, and how batching collapses many round trips into one.
 */
public class EventRepository {

    private final DataSource dataSource;

    public EventRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Look up a single event by id.
     * try-with-resources ensures the connection returns to the pool
     * even if the query throws.
     */
    public Optional<Event> findById(Long id) throws SQLException {
        String sql = "SELECT id, venue, artist, event_date, price FROM event WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Event(
                            rs.getLong("id"),
                            rs.getString("venue"),
                            rs.getString("artist"),
                            rs.getDate("event_date").toLocalDate(),
                            rs.getBigDecimal("price")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    /**
     * Insert many events in a single database round trip.
     * setAutoCommit(false) opens an explicit transaction.
     * addBatch queues the parameter binding.
     * executeBatch sends all rows at once.
     * commit finalizes them together.
     */
    public void batchInsert(List<Event> events) throws SQLException {
        String sql = "INSERT INTO event (venue, artist, event_date, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Event e : events) {
                ps.setString(1, e.getVenue());
                ps.setString(2, e.getArtist());
                ps.setDate(3, Date.valueOf(e.getEventDate()));
                ps.setBigDecimal(4, e.getPrice());
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        }
    }
}
