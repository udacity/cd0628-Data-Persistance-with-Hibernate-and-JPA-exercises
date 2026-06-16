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
        // Select by id, map the row with mapRow(), return Optional
        String sql = "SELECT id, email, first_name, last_name, created_at FROM customer WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customer " + id, e);
        }
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
        // Single connection, single PreparedStatement, batch all inserts together
        String sql = "INSERT INTO customer (email, first_name, last_name) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Customer customer : customers) {
                ps.setString(1, customer.getEmail());
                ps.setString(2, customer.getFirstName());
                ps.setString(3, customer.getLastName());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to batch insert customers", e);
        }
    }

    /**
     * Maps the current row of a ResultSet to a Customer object.
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