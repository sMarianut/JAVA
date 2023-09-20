package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.transactionType;

public class CardPaymentDTO {
    private String number;
    private int cvv;
    private double amountToPay;
    private String description;

    public CardPaymentDTO() {
    }

    public CardPaymentDTO(String number, int cvv, double amountToPay, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amountToPay = amountToPay;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public String getDescription() {
        return description;
    }
}
