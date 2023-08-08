package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;

public class AccountDTO {
    private long Id;
    private String number;

    private LocalDate creationDate;
    private double balance;

    public AccountDTO() {
    }
    public AccountDTO(Account Account){
        this.Id = Account.getId();
        this.number = Account.getNumber();
        this.creationDate = Account.getCreationDate();
        this.balance = Account.getBalance();
    }

    public long getId() {
        return Id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
