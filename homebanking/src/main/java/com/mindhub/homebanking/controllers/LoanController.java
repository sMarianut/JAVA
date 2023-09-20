package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRespository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.*;
import com.mindhub.homebanking.utils.InterestCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.InterestCalculator.calculateInterest;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private LoanRepository loanRepository;
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
            return new ResponseEntity<>("You already have this loan", HttpStatus.FORBIDDEN);
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

            double amountTotalWI = calculateInterest(loanApplicationDTO, currentLoan);
            ClientLoan clientLoanNew = new ClientLoan(amountTotalWI,loanApplicationDTO.getPaymentsReq(),current,currentLoan);
            clientLoanService.addClientLoan(clientLoanNew);
            accountLoan.setBalance(accountLoan.getBalance() + loanApplicationDTO.getAmount());
            Transaction loanApp = new Transaction(loanApplicationDTO.getAmount(),"Your loan "+ currentLoan.getName() + " was approved", LocalDateTime.now(),transactionType.CREDIT, accountLoan.getBalance(), true);
            transactionService.addTransaction(loanApp);
            accountService.addAccount(accountLoan);
            accountLoan.addTransaction(loanApp);


        }
        return new ResponseEntity<>("Your loan was approved!, Enjoy!", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/createLoan")
    public ResponseEntity<Object> createLoan(@RequestBody LoanDTO loan, Authentication authentication) {
        Client admin = clientService.findByEmail(authentication.getName());
        String loanName = loan.getName();
        Loan existsLoan = loanRepository.findByName(loanName);

        if (admin == null) {
        new ResponseEntity<>("You cannot se this", HttpStatus.FORBIDDEN);
        }
        if (!admin.getEmail().contains("@admSpecific445MB.com")){
            return new ResponseEntity<>("you cannot access.", HttpStatus.FORBIDDEN);
        }
        if (existsLoan != null){
            return new ResponseEntity<>("This loan already exists.", HttpStatus.FORBIDDEN);
        }
        double maxAmount = loan.getMaxAmount();
        List<Integer> payments = loan.getPayments();
        double interest = loan.getInterest();
        if (loanName.isBlank()){
            return new ResponseEntity<>("The name of the loan couldn't be empty", HttpStatus.FORBIDDEN);
        }
        if(maxAmount <= 0){
            return new ResponseEntity<>("The max amount couldn't be zero", HttpStatus.FORBIDDEN);
        }
        if(payments.size() == 0){
            return new ResponseEntity<>("The payments couldn't be zero", HttpStatus.FORBIDDEN);
        }
        if (interest <= 0){
            return new ResponseEntity<>("The initial interest couldn't be zero", HttpStatus.FORBIDDEN);
        }
        Loan loanCreated = new Loan(loanName,maxAmount,payments,interest);
        loanRepository.save(loanCreated);
        return new ResponseEntity<>("Loan successfully created", HttpStatus.CREATED);
    }

}
