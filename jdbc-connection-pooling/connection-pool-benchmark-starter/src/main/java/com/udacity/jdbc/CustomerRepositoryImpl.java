package com.udacity.jdbc;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final DataSource dataSource;

    public CustomerRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long create(Customer customer) {
        String sql = "INSERT INTO customer (email, first_name, last_name) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getEmail());
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getLastName());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    customer.setId(id);
                    return id;
                }
            }
            throw new SQLException("Insert succeeded but no generated key was returned");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    @Override
    public Optional<Customer> findById(long id) {
        // ============================================================
        // TODO 1: Implement findById
        //
        // Steps:
        //   1. Prepare this SELECT statement:
        //        "SELECT id, email, first_name, last_name, created_at FROM customer WHERE id = ?"
        //
        //   2. Set the id parameter using ps.setLong(1, id)
        //
        //   3. Execute the query and get the ResultSet
        //
        //   4. If rs.next() returns true, map the row using the
        //      mapRow(rs) helper method below, and return Optional.of(customer)
        //
        //   5. Otherwise, return Optional.empty()
        //
        //   6. Wrap any SQLException in a RuntimeException with a
        //      descriptive message
        //
        // Hint: use try-with-resources for Connection, PreparedStatement,
        // AND ResultSet. All three are AutoCloseable. Look at create()
        // above for the pattern.
        // ============================================================
        return Optional.empty();
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customer SET email = ?, first_name = ?, last_name = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getEmail());
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getLastName());
            ps.setLong(4, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update customer " + customer.getId(), e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete customer " + id, e);
        }
    }

    @Override
    public int[] batchInsert(List<Customer> customers) {
        // ============================================================
        // TODO 2: Implement batchInsert
        //
        // Steps:
        //   1. Use the same INSERT SQL string from create() above:
        //        "INSERT INTO customer (email, first_name, last_name) VALUES (?, ?, ?)"
        //
        //   2. Open Connection and PreparedStatement using try-with-resources
        //
        //   3. Loop through the customers list. For each Customer:
        //        - call ps.setString(1, customer.getEmail())
        //        - call ps.setString(2, customer.getFirstName())
        //        - call ps.setString(3, customer.getLastName())
        //        - call ps.addBatch()
        //
        //   4. After the loop, call ps.executeBatch() and return the
        //      int[] it gives back (one entry per row, indicating rows
        //      affected)
        //
        //   5. Wrap any SQLException in a RuntimeException
        //
        // Why batch matters: looping single inserts is roughly 10x slower
        // than executeBatch() because each insert pays a network
        // round-trip cost. With batching, all 1,000 rows go in a single
        // round-trip.
        // ============================================================
        return new int[0];
    }

    /**
     * Maps the current row of a ResultSet to a Customer object.
     * Used by findById and any future read methods.
     */
    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getLong("id"));
        c.setEmail(rs.getString("email"));
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            c.setCreatedAt(ts.toLocalDateTime());
        }
        return c;
    }
}