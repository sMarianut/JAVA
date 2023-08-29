package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class ClientController{
    private LocalDate creationDate = LocalDate.now();
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String Rnumber(){
        String random;
        do{
            int number= (int)(Math.random()*100+999);
            random="VIN-" + number;
        }while(accountRepository.findByNumber(random) != null);
        return random;
    }

    @RequestMapping(path = "/api/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Fill all the fields", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("This email is already in use, BRODER", HttpStatus.FORBIDDEN);
        }
        String number = Rnumber(); ///variable
        Account newAccount = new Account(number, this.creationDate ,0);
        Client newClient = new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientRepository.save(newClient);
        newClient.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }

    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id)
                .map(client -> new ClientDTO(client))
                .orElse(null);
    }
    @RequestMapping("/api/clients/current")
    public ClientDTO getClient(Authentication authentication){
     return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }



}
