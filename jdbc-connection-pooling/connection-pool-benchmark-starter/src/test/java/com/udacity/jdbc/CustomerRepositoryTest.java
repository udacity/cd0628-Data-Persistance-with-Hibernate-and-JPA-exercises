package com.udacity.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for CustomerRepositoryImpl.
 *
 * These tests are pre-written and currently FAIL until you complete
 * TODO 1 (findById) and TODO 2 (batchInsert) in CustomerRepositoryImpl.java.
 *
 * No TODOs here. Just run the tests after completing the repository.
 */
@SpringBootTest(classes = Application.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    void findById_returnsCustomer_whenIdExists() {
        Optional<Customer> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("emma.fischer@udacibank.example");
        assertThat(result.get().getFirstName()).isEqualTo("Emma");
        assertThat(result.get().getLastName()).isEqualTo("Fischer");
    }

    @Test
    void findById_returnsEmpty_whenIdDoesNotExist() {
        Optional<Customer> result = repository.findById(999_999L);

        assertThat(result).isEmpty();
    }

    @Test
    void batchInsert_insertsThousandRows_efficiently() {
        List<Customer> batch = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            batch.add(new Customer(
                    "batch" + i + "@udacibank.example",
                    "Batch" + i,
                    "User" + i));
        }

        long startNanos = System.nanoTime();
        int[] results = repository.batchInsert(batch);
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000;

        assertThat(results).hasSize(1000);
        assertThat(elapsedMs)
                .as("Batch insert of 1,000 rows should complete in under 2 seconds")
                .isLessThan(2000);
    }
}