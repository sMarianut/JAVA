package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRespository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @RequestMapping("/loans")
    public List<LoanDTO> getLoansDTO(){
        return loanService.getLoansDTO();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> loanApp(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){
        Client current = clientService.findByEmail(authentication.getName());
        Loan currentLoan = loanService.findById(loanApplicationDTO.getId());
        List<Loan> loansClient = current.getLoans();
        List<String> numbersAcc = current.getAccounts().stream().map(account -> account.getNumber()).collect(Collectors.toList());
        Account accountLoan = accountService.findByNumber(loanApplicationDTO.getAccountDest());

        if (currentLoan == null){
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }
        if(loansClient.contains(currentLoan)){
            return new ResponseEntity<>("You cannot apply for the same loan", HttpStatus.FORBIDDEN);
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

        if (loanApplicationDTO.getAmount() > currentLoan.getMaxAmount()){
            return new ResponseEntity<>("The amount couldn't be more than the max amount of this loan.", HttpStatus.FORBIDDEN);
        }
        if (!(currentLoan.getPayments().contains(loanApplicationDTO.getPaymentsReq()))){
            return new ResponseEntity<>("The payment selected it's not available, available payments:" + currentLoan.getPayments().toString(), HttpStatus.FORBIDDEN);
        }
        if(accountLoan == null){
            return new ResponseEntity<>("The account destiny not exist. please put again the number of the account", HttpStatus.FORBIDDEN);
        }
        if (!numbersAcc.contains(loanApplicationDTO.getAccountDest())){
            return new ResponseEntity<>("this account is not of this client", HttpStatus.FORBIDDEN);
        }else{
            double amountPlus = ((double)(loanApplicationDTO.getAmount()) + (loanApplicationDTO.getAmount()*0.2));
            ClientLoan clientLoanNew = new ClientLoan(amountPlus,loanApplicationDTO.getPaymentsReq(),current,currentLoan);
            clientLoanService.addClientLoan(clientLoanNew);
            Transaction loanApp = new Transaction(loanApplicationDTO.getAmount(),"Your loan "+ currentLoan.getName() + " was approved", LocalDateTime.now(),transactionType.CREDIT);
            accountLoan.setBalance(loanApplicationDTO.getAmount());
            accountLoan.addTransaction(loanApp);
            accountService.addAccount(accountLoan);
            transactionService.addTransaction(loanApp);
        }
        return new ResponseEntity<>("Created, brou", HttpStatus.OK);
    }

}
