package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "active", strategy = "native")
    private Long Id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private transactionType type;
    private double currentBalance;
    private boolean isOnTransf;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account")
    private Account account;

    public Transaction() {
    }

    public Transaction(double amount, String description, LocalDateTime date, transactionType type, double currentBalance, boolean isOnTransf) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.currentBalance = currentBalance;
        this.isOnTransf = isOnTransf;
    }

    public Long getId() {
        return Id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public transactionType getType() {
        return type;
    }

    public void setType(transactionType type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public boolean isOnTransf() {
        return isOnTransf;
    }

    public void setOnTransf(boolean onTransf) {
        isOnTransf = onTransf;
    }
}
