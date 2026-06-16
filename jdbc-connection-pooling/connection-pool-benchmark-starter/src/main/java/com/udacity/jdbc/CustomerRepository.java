package com.udacity.jdbc;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    /**
     * Inserts a new Customer row and returns the generated id.
     */
    Long create(Customer customer);

    /**
     * Looks up a Customer by id. Returns Optional.empty() if not found.
     */
    Optional<Customer> findById(long id);

    /**
     * Updates an existing Customer's email, first name, and last name.
     */
    void update(Customer customer);

    /**
     * Removes the Customer with the given id.
     */
    void delete(long id);

    /**
     * Inserts a list of Customers in a single batched JDBC operation.
     * Returns the row counts reported by executeBatch().
     */
    int[] batchInsert(List<Customer> customers);
}