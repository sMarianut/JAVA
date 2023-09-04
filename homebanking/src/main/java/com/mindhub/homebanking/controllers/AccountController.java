package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    private String Rnumber(){
        String random;
        do{
            int number= (int)(Math.random()*(1000000+99999999));
            random="VIN-" + number;
        }while(accountService.findByNumber(random) != null);
            return random;
    }

    @RequestMapping("clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getAccounts().stream().collect(toList());
    }
    @RequestMapping("clients/current/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
       Client currentC =  clientService.findByEmail(authentication.getName());
       Account currentA = accountService.findById(id);
       if (currentC.getId() == currentA.getClient().getId()) {
           return new ResponseEntity<>(new AccountDTO(currentA), HttpStatus.OK);
       }
       return new ResponseEntity<>("Your not allowed to see this", HttpStatus.FORBIDDEN);
    }
    @PostMapping(path = "clients/current/accounts")
    public ResponseEntity<Object> createAcc(Authentication authentication) {
        if (clientService.findByEmail(authentication.getName()).getAccounts().size() <= 2) {
            String numberAcc = Rnumber();
            Account newAcc = new Account(numberAcc, LocalDate.now(), 0.0);
            clientService.findByEmail(authentication.getName()).addAccount(newAcc);
            accountService.addAccount(newAcc);
        }else {
            return new ResponseEntity<>("You can't have more than 2 accounts", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }}