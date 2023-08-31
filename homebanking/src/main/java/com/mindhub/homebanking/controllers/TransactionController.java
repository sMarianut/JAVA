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
    public ResponseEntity<Object> createTransaction(@RequestParam String amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String destinationAccountNumber, Authentication authentication){
        Client currentC = clientRepository.findByEmail(authentication.getName());
        Account accOrigin = accountRepository.findByNumber(originAccountNumber);
        Account destinationA = accountRepository.findByNumber(destinationAccountNumber);
        if (destinationAccountNumber.isBlank()){
            return new ResponseEntity<>("Destination account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (originAccountNumber.isBlank()){
            return new ResponseEntity<>("Origin account number cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (accOrigin == null){
           return new ResponseEntity<>("Origin account not found", HttpStatus.FORBIDDEN);
       }
       if(destinationA == null){
          return new ResponseEntity<>("Destination account not found", HttpStatus.FORBIDDEN);
        }
        if(amount.isBlank() || Double.parseDouble(amount) <= 0){
            return new ResponseEntity<>("Please enter a valid amount", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.FORBIDDEN);
        }
        if(accOrigin.getNumber() == destinationA.getNumber()){
            return new ResponseEntity<>("You cannot transfer to the same account", HttpStatus.FORBIDDEN);
        }
       if( accOrigin.getBalance() < Double.parseDouble(amount) ){
           return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
       }else{
           accOrigin.setBalance(accOrigin.getBalance() - Double.parseDouble(amount));
           destinationA.setBalance(destinationA.getBalance() + Double.parseDouble(amount));
           Transaction TransacDebit = new Transaction(Double.parseDouble(amount),description, LocalDateTime.now(), transactionType.DEBIT);
           Transaction TransacCredit = new Transaction(Double.parseDouble(amount),description, LocalDateTime.now(), transactionType.CREDIT);
           accOrigin.addTransaction(TransacDebit);
           destinationA.addTransaction(TransacCredit);
           accountRepository.save(accOrigin);
           accountRepository.save(destinationA);
           transactionRepository.save(TransacDebit);
           transactionRepository.save(TransacCredit);
       }
       return new ResponseEntity<>("Transaction succesfully created", HttpStatus.CREATED);
    }
}
