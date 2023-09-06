package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoansDTO();
    Loan findById(long id);
}
