package com.udacity.mappings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

// @Entity makes this a persistent subtype of Payment. No @Inheritance
// here -- that lives on the parent.
@Entity
public class BankTransferPayment extends Payment {

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "routing_number", nullable = false)
    private String routingNumber;

    public BankTransferPayment() {
    }

    public BankTransferPayment(Money amount, String accountNumber, String routingNumber) {
        super(amount);
        this.accountNumber = accountNumber;
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getRoutingNumber() { return routingNumber; }
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }
}