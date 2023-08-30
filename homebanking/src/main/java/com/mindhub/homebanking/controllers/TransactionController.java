package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.transactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String destinationAccountNumber, Authentication authentication){
        Client currentC = clientRepository.findByEmail(authentication.getName());
        Account originA = currentC.getAccounts().stream().filter(account -> account.getNumber().equals(originAccountNumber)).findFirst().orElse(null);
        Account destinationA = accountRepository.findByNumber(destinationAccountNumber);
        if(amount <= 0){
            return new ResponseEntity<>("Amount must be greater than 0", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.FORBIDDEN);
        }
        if(originA.getNumber() == destinationA.getNumber()){
            return new ResponseEntity<>("You cannot transfer to the same account", HttpStatus.FORBIDDEN);
        }
       if ( originA == null ){
           return new ResponseEntity<>("Origin account not found", HttpStatus.FORBIDDEN);
       }
       if(destinationA == null){
           return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
       }
       if( originA.getBalance() < amount ){
           return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
       }else{
           originA.setBalance(originA.getBalance() - amount);
           destinationA.setBalance(destinationA.getBalance() + amount);
           Transaction TransacDebit = new Transaction(amount,description, LocalDateTime.now(), transactionType.DEBIT);
           Transaction TransacCredit = new Transaction(amount,description, LocalDateTime.now(), transactionType.CREDIT);
           originA.addTransaction(TransacDebit);
           destinationA.addTransaction(TransacCredit);
           accountRepository.save(originA);
           accountRepository.save(destinationA);
           transactionRepository.save(TransacDebit);
           transactionRepository.save(TransacCredit);
       }
       return new ResponseEntity<>("Transaction succesfully created", HttpStatus.CREATED);
    }
}
