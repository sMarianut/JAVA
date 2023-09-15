package com.mindhub.homebanking;

import com.mindhub.homebanking.controllers.CardsController;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@SpringBootApplication
public class HomebankingApplication {

    private LocalDate creationDate = LocalDate.now();
    private LocalDate creationDate2 = LocalDate.now().plusDays(1);
    private LocalDateTime date = LocalDateTime.now();
    private LocalDateTime date2 = LocalDateTime.now().plusHours(2);
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRespository clientLoanRepository, CardRepository cardRepository) {
        return (args) -> {
            Client client1 = new Client("Melba", "Morel", "melbax@gmail.com", passwordEncoder.encode("1234"));
            Client client2 = new Client("Ippo", "Makanouchi", "Ippo@Hajime.com", passwordEncoder.encode("4321"));
            Client admin = new Client("admin", "admin", "admin@admSpecific445MB.com", passwordEncoder.encode("soyadminjeje"));
            clientRepository.save(client1);
            clientRepository.save(client2);
            clientRepository.save(admin);
            Card debitCard = new Card("Melba GGEz", CardType.DEBIT, CardColor.GOLD,
                    "4342-4234-5667-3456", 666, this.creationDate.plusDays(3),
                    this.creationDate.plusDays(3).plusYears(5), true);
            Card creditCard = new Card("Melba GGEz", CardType.CREDIT, CardColor.TITANIUM,
                    "8968-3524-7246-9164", 777, this.creationDate.plusDays(7), this.creationDate.plusDays(7).plusYears(8),true);
            Card debitCard2 = new Card("Melba GGEz", CardType.DEBIT, CardColor.SILVER, "9184-2954-9023-6438", 911, this.creationDate.plusDays(1), this.creationDate2.plusYears(5), true);
            Card debitCard3 = new Card("Ippo Makanouchi", CardType.DEBIT, CardColor.SILVER, "1234-4567-6789-1011", 333, this.creationDate.plusDays(7),
                    this.creationDate.plusDays(7).plusYears(5), true);
            Account account1 = new Account("VIN001", creationDate, 5000, true);
            Account account2 = new Account("VIN002", creationDate2, 7000, true);
            Account account3 = new Account("VIN003", this.creationDate, 51000, true);
            Account account4 = new Account("VIN004", this.creationDate2, 75000, true);
            Transaction transaction1 = new Transaction(2000, "Other", date, transactionType.CREDIT,true);
            Transaction transaction2 = new Transaction(-1500, "Others", date2, transactionType.DEBIT,true);
            Loan loan1 = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60));
            Loan loan2 = new Loan("Personal", 10000, Arrays.asList(6, 12, 24));
            Loan loan3 = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24, 36));
            loanRepository.save(loan1);
            loanRepository.save(loan2);
            loanRepository.save(loan3);
            ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, loan1);
            ClientLoan clientLoan2 = new ClientLoan(100000, 12, client1, loan2);
            ClientLoan clientLoan3 = new ClientLoan(100000, 24, client2, loan2);
            ClientLoan clientLoan4 = new ClientLoan(200000, 36, client2, loan3);


            clientLoanRepository.save(clientLoan1);
            clientLoanRepository.save(clientLoan2);
            clientLoanRepository.save(clientLoan3);
            clientLoanRepository.save(clientLoan4);
            client1.addAccount(account1);
            client1.addAccount(account2);
            client2.addAccount(account3);
            client2.addAccount(account4);
            client1.addCards(debitCard);
            client1.addCards(creditCard);
            client1.addCards(debitCard2);
            client2.addCards(debitCard3);

            client1.addClientLoans(clientLoan1);
            client1.addClientLoans(clientLoan2);
            client2.addClientLoans(clientLoan3);
            client2.addClientLoans(clientLoan4);


            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            accountRepository.save(account4);
            cardRepository.save(debitCard);
            cardRepository.save(creditCard);
            cardRepository.save(debitCard2);
            cardRepository.save(debitCard3);

            account1.addTransaction(transaction1);
            account1.addTransaction(transaction2);
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);

        };
    }
}



