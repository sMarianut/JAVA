package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRespository;
import com.mindhub.homebanking.service.ClientLoanService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienLoanServiceImplement implements ClientLoanService {
    @Autowired
    private ClientLoanRespository clientLoanRespository;
    @Autowired
    private ClientService clientService;


    @Override
    public void addClientLoan(ClientLoan clientLoan) {
        clientLoanRespository.save(clientLoan);
    }
}
