package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class PostnetController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;



    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> newPayment(@RequestBody CardPaymentDTO cardPaymentDTO){
        String cardNumber = cardPaymentDTO.getNumber();
        if (cardNumber.isBlank()){
            return new ResponseEntity<>("Card number cannot be empty", HttpStatus.FORBIDDEN);
        }
        Card exist = cardService.findByNumber(cardNumber);
        if (exist.isOnCard() == false || exist == null){
            return new ResponseEntity<>("This card not exist", HttpStatus.NOT_FOUND);
        }
        if (exist.getCvv() != cardPaymentDTO.getCvv()){
            return new ResponseEntity<>("CVV error, please try again", HttpStatus.BAD_GATEWAY);
        }

        long idClient = exist.getClient().getId();
        Client client = clientService.findById(idClient);
        Set<Account> accountList = client.getAccounts();
        Account maxBalanceAccount = accountList.stream().reduce((ac2, ac3) -> ac2.getBalance() > ac3.getBalance() ? ac2 : ac3).orElse(null);
        if (maxBalanceAccount.getBalance() < cardPaymentDTO.getAmountToPay()){
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_GATEWAY);
        }else{
            maxBalanceAccount.setBalance(maxBalanceAccount.getBalance() - cardPaymentDTO.getAmountToPay());
            Transaction transaction = new Transaction(cardPaymentDTO.getAmountToPay(), cardPaymentDTO.getDescription(), LocalDateTime.now(), transactionType.DEBIT, maxBalanceAccount.getBalance(), true);
            maxBalanceAccount.addTransaction(transaction);
            transactionRepository.save(transaction);
            accountRepository.save(maxBalanceAccount);
        }

        return new ResponseEntity<>("Your payment was successful", HttpStatus.OK);
    }
}
