package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AccountService {
    Account findById(long id);
    void addAccount(Account account);
    Account findByNumber(String number);

}
