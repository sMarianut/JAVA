package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
public class AccountController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    private String Rnumber(){
        String random;
        do{
            int number= (int)(Math.random()*100+999);
            random="VIN-" + number;
        }while(accountRepository.findByNumber(random) != null);
            return random;
    }

    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getAccounts().stream().collect(toList());
    }
    @RequestMapping("/api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
    @PostMapping(path = "/api/clients/current/accounts")
    public ResponseEntity<Object> createAcc(Authentication authentication) {
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() <= 2) {
            String numberAcc = Rnumber();
            Account newAcc = new Account(numberAcc, LocalDate.now(), 0.0);
            clientRepository.findByEmail(authentication.getName()).addAccount(newAcc);
            accountRepository.save(newAcc);
        }else {
            return new ResponseEntity<>("NO FUNCA BRO", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }}