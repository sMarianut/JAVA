package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<TransactionDTO> getTransactionsDTO() {
        return getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }

//@Override
//    public List<TransactionDTO> getTransactionsByDate(LocalDateTime dateInit, LocalDateTime dateEnd) {
//        return transactionRepository.findByDateBetween(dateInit, dateEnd).stream().map(transactionDTO -> new TransactionDTO(transactionDTO));
//    }

}
