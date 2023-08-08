package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.transactionType;

import java.time.LocalDateTime;

public class TransactionDTO{
    private Long Id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private transactionType type;

    public TransactionDTO() {
    }
    public TransactionDTO(Transaction transaction){
        this.Id = transaction.getId();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
    }

    public Long getId() {
        return Id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public transactionType getType() {
        return type;
    }

}
