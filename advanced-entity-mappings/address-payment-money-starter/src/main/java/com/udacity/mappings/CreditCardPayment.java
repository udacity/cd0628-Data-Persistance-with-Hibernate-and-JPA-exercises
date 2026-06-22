package com.udacity.mappings;

import jakarta.persistence.Column;

// ============================================================
// TODO 6: Annotate this class with @Entity
//
// Marks it as a persistent subtype of Payment. No @Inheritance
// annotation here -- that lives on the parent class.
// ============================================================
public class CreditCardPayment extends Payment {

    @Column(name = "card_last_four", nullable = false, length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", nullable = false)
    private String cardBrand;

    public CreditCardPayment() {
    }

    public CreditCardPayment(Money amount, String cardLastFour, String cardBrand) {
        super(amount);
        this.cardLastFour = cardLastFour;
        this.cardBrand = cardBrand;
    }

    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }
    public String getCardBrand() { return cardBrand; }
    public void setCardBrand(String cardBrand) { this.cardBrand = cardBrand; }
}