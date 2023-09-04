package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @RequestMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        return loanService.getLoansDTO();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loanApp(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client current = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplicationDTO.getId());
        if (loan == null){
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("the amount couldn't be empty, try again", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getPaymentsReq() < 0 || loanApplicationDTO.getPaymentsReq() == null){
            return new ResponseEntity<>("the payments couldn't be empty, try again", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAccountDest().isBlank()) {
            return new ResponseEntity<>("the account destiny couldn't be empty, try again", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The amount couldn't be more than the max amount of this loan.", HttpStatus.FORBIDDEN);
        }
        if (!(loan.getPayments().contains(loanApplicationDTO.getPaymentsReq()))){
            return new ResponseEntity<>("The payment selected it's not available, available payments:" + loan.getPayments().toString(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Created, brou", HttpStatus.OK);
    }

}
