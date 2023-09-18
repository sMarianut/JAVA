package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    private String Rnumber() {
        String random;
        do {
            int number = (int) (Math.random() * (1000000 + 99999999));
            random = "VIN-" + number;
        } while (accountService.findByNumber(random) != null);
        return random;
    }

    @RequestMapping("clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getAccounts().stream().collect(toList());
    }

    @RequestMapping("clients/current/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {
        Client currentC = clientService.findByEmail(authentication.getName());
        Account currentA = accountService.findById(id);
        if (currentC.getId() == currentA.getClient().getId()) {
            return new ResponseEntity<>(new AccountDTO(currentA), HttpStatus.OK);
        }
        return new ResponseEntity<>("Your not allowed to see this", HttpStatus.FORBIDDEN);
    }

    @PostMapping(path = "clients/current/accounts")
    public ResponseEntity<Object> createAcc(Authentication authentication, @RequestParam String type) {
        Client current = clientService.findByEmail(authentication.getName());
        List<Account> filterAcc = accountRepository.findAllByClientAndAccOnTrue(current);
        if (filterAcc.size() <= 2) {
            String numberAcc = Rnumber();
            Account newAcc = new Account(numberAcc, LocalDate.now(), 0.0, true, AccountType.valueOf(type));
            clientService.findByEmail(authentication.getName()).addAccount(newAcc);
            accountService.addAccount(newAcc);
        } else {
            return new ResponseEntity<>("You can't have more than 2 accounts", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/deleteAcc")
    public ResponseEntity<Object> deleteAcc(@RequestParam long id, Authentication authentication) {
        Client current = clientService.findByEmail(authentication.getName());
        Account currentAcc = accountService.findById(id);
        Set<Account> accounts = current.getAccounts();
        long numberAcc = accounts.stream().filter(account1 -> account1.isAccOn()).count();
        Set<Transaction> currentAccTransf = currentAcc.getTransactions();
        List<Account> filterAcc = accountRepository.findAllByClientAndAccOnTrue(current);

        if (current == null) {
            return new ResponseEntity<>("This client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (currentAcc == null) {
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!current.getAccounts().contains(currentAcc)){
            return new ResponseEntity<>("You are not the owner of this account", HttpStatus.FORBIDDEN);
        }
        if(filterAcc.size() == 1){
            return new ResponseEntity<>("You cannot delete the only account you have.", HttpStatus.FORBIDDEN);
        }

        if (currentAcc.getBalance() < 0){
            return new ResponseEntity<>("You cannot delete an account with issues", HttpStatus.FORBIDDEN);
        }
        if (currentAcc.getBalance() > 0){
            return new ResponseEntity<>("You cannot delete an account with cash in it. Do you want to transfer to another account?", HttpStatus.FORBIDDEN);
        }
        else {
            for (Transaction transaction : currentAccTransf) {
                transaction.setOnTransf(false);
                transactionService.addTransaction(transaction);

            }
                currentAcc.setAccOn(false);
                accountService.addAccount(currentAcc);
        }
        return new ResponseEntity<>("Your account and transactions has been removed.", HttpStatus.ACCEPTED);

    }
}