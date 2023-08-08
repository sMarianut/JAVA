package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

}
