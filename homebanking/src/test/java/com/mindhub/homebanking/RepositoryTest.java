package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest

@AutoConfigureTestDatabase(replace = NONE)
public class RepositoryTest {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;



    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }



    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @ParameterizedTest
    @ValueSource(longs = {24, 28, 32, 37, 39, 41, 43, 44, 19,20})
    public void existTransaction(long valor){
        Transaction transaction = transactionRepository.findById(valor).orElse(null);
        if (transaction.getType() == transactionType.CREDIT){
            assertThat(transaction.getType(), is(transactionType.CREDIT));
        }else{
            assertThat(transaction.getType(), is(transactionType.DEBIT));
        }

    }
//    @Test
//    public void findByClientAndStatusIsTrue(){
//        long id =1 ;
//        Client client = clientRepository.findById(id).orElse(null);
//        List<Account> s = accountRepository.findByClientAndStatusIsTrue(client);
//        assertThat(s,is(not(empty())));
//    }


}

