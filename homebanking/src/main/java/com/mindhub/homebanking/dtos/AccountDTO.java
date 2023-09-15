package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountDTO {
    private long Id;
    private String number;

    private LocalDate creationDate;
    private double balance;
    private boolean isAccOn;
    private Set<TransactionDTO> transactions = new HashSet<>();

    public AccountDTO() {
    }
    public AccountDTO(Account account){
        this.Id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = new HashSet<>();
        this.isAccOn = account.isAccOn();
        for (Transaction tr : account.getTransactions()) {
            this.transactions.add(new TransactionDTO(tr));
        }

    }

    public boolean isAccOn() {
        return isAccOn;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
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
