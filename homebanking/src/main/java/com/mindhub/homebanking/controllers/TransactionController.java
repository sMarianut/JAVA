package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.transactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @GetMapping("/transactions/findDate")
    public ResponseEntity<Object> getTransactionsbyDateTime(@RequestParam String dateInit,
                                                          @RequestParam String dateEnd,
                                                          @RequestParam String numberAcc,
                                                          Authentication authentication){
        Client current = clientService.findByEmail(authentication.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        if (!accountRepository.existsByNumber(numberAcc)){
            return new ResponseEntity<>("this account dont exist", HttpStatus.BAD_REQUEST);
        }
        if (current == null){
            return new ResponseEntity<>("you are not allowed to see this", HttpStatus.FORBIDDEN);
        }
        if (dateInit == null){
           return new ResponseEntity<>("Please, fill the date requeriment", HttpStatus.BAD_REQUEST);
        }else if (dateEnd == null){
            new ResponseEntity<>("Please, fill the date end requeriment", HttpStatus.BAD_REQUEST);
        }
        if (dateInit.equals(dateEnd)){
            return new ResponseEntity<>("you cannot do this", HttpStatus.BAD_REQUEST);
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateInit, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateEnd, formatter);
        List<Transaction> transf = transactionRepository.findByDateBetweenAndAccountNumber(localDateTime, localDateTime2, numberAcc);


        return new ResponseEntity<>("pete", HttpStatus.OK);
    }
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam String amount,
                                                    @RequestParam String description,
                                                    @RequestParam String originAccountNumber,
                                                    @RequestParam String destinationAccountNumber, Authentication authentication){
        Client currentC = clientService.findByEmail(authentication.getName());
        Account accOrigin = accountService.findByNumber(originAccountNumber);
        Account destinationA = accountService.findByNumber(destinationAccountNumber);
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
        if(accOrigin.getNumber() == destinationA.getNumber()){
            return new ResponseEntity<>("You cannot transfer to the same account", HttpStatus.FORBIDDEN);
        }
        if(amount.isBlank() || Double.parseDouble(amount) <= 0){
            return new ResponseEntity<>("Please enter a valid amount", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Description cannot be empty", HttpStatus.FORBIDDEN);
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
           accountService.addAccount(accOrigin);
           accountService.addAccount(destinationA);
           transactionService.addTransaction(TransacDebit);
           transactionService.addTransaction(TransacCredit);
       }
       return new ResponseEntity<>("Transaction succesfully created", HttpStatus.CREATED);
    }
}
