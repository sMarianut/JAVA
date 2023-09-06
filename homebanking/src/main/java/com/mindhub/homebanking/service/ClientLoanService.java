package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.ClientLoan;
import org.springframework.stereotype.Service;


public interface ClientLoanService {
    void addClientLoan(ClientLoan clientLoan);
}
