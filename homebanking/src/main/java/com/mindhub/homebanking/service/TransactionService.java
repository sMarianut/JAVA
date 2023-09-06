package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.stereotype.Service;


public interface TransactionService {
    void addTransaction(Transaction transaction);

}
