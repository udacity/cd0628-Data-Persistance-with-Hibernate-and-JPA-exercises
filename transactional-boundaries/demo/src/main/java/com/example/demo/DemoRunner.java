package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

/**
 * Runs three scenarios end to end:
 *
 *   1. Show starting balances.
 *   2. Happy transfer - balances change, audit row committed.
 *   3. Failing transfer - audit row committed, balances rolled back.
 *
 * Note in scenario 3 that the audit row survives even though the transfer
 * threw. That is the whole point of REQUIRES_NEW.
 */
@SpringBootApplication
public class DemoRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoRunner.class, args);
    }

    @Bean
    public CommandLineRunner run(AccountRepository accounts,
                                 AuditLogRepository auditLogs,
                                 TransferService transfer) {
        return args -> {
            Account alice = accounts.save(new Account("Alice", new BigDecimal("500.00")));
            Account bob   = accounts.save(new Account("Bob",   new BigDecimal("200.00")));

            System.out.println("---- 1. Starting balances ----");
            accounts.findAll().forEach(a -> System.out.println("  " + a));

            System.out.println("---- 2. Happy transfer: Alice -> Bob $100 ----");
            transfer.transfer(alice.getId(), bob.getId(), new BigDecimal("100.00"));
            accounts.findAll().forEach(a -> System.out.println("  " + a));

            System.out.println("---- 3. Failing transfer: Alice -> Bob $50 (will throw) ----");
            try {
                transfer.transferAndFail(alice.getId(), bob.getId(), new BigDecimal("50.00"));
            } catch (IllegalStateException e) {
                System.out.println("  Caught expected failure: " + e.getMessage());
            }

            System.out.println("---- 4. Balances after failed transfer (rolled back) ----");
            accounts.findAll().forEach(a -> System.out.println("  " + a));

            System.out.println("---- 5. Audit log (both entries survive, even the failed one) ----");
            auditLogs.findAll().forEach(a -> System.out.println("  " + a));
        };
    }
}
