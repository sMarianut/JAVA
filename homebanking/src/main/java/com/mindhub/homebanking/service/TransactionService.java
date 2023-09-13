package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


public interface TransactionService {
    void addTransaction(Transaction transaction);

    List<Transaction> getTransactions();
    List<TransactionDTO> getTransactionsDTO();

//    List<TransactionDTO> getTransactionsByDate(LocalDateTime dateInit, LocalDateTime dateEnd);

}
