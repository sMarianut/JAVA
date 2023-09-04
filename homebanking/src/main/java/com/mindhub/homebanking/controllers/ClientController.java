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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController{
    private LocalDate creationDate = LocalDate.now();
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    private String Rnumber(){
        String random;
        do{
            int number= (int)(Math.random()*(10000000+99999999));
            random="VIN-" + number;
        }while(accountService.findByNumber(random) != null);
        return random;
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank()) {
            return new ResponseEntity<>("First name cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()){
            return new ResponseEntity<>("Last name cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()){
            return new ResponseEntity<>("Email cannot be empty", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()){
            return new ResponseEntity<>("Password cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) !=  null) {

            return new ResponseEntity<>("This email is already in use, BRODER", HttpStatus.FORBIDDEN);
        }
        String number = Rnumber(); ///variable
        Account newAccount = new Account(number, this.creationDate ,0);
        Client newClient = new Client(firstName,lastName,email,passwordEncoder.encode(password));
        clientService.addClient(newClient);
        newClient.addAccount(newAccount);
        accountService.addAccount(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientService.getClientsDTO();
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientService.getClient(id);
    }
    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
     return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }



}
