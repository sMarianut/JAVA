package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.sun.istack.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@SpringBootApplication
public class HomebankingApplication {

	LocalDate creationDate = LocalDate.now();
	LocalDate creationDate2 = LocalDate.now().plusDays(1);
	LocalDateTime date = LocalDateTime.now();
	LocalDateTime date2 = LocalDateTime.now().plusHours(2);

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRespository clientLoanRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melbax@gmail.com");
			Client client2 = new Client("Marianut", "Makanouchi",  "Ippo@Hajime.com");
			clientRepository.save(client1);
			clientRepository.save(client2);
			Account account1 = new Account("VIN001", creationDate, 5000);
			Account account2 = new Account("VIN002", creationDate2, 7000);
			Account account3 = new Account("VIN003",this.creationDate,51000);
			Account account4 = new Account("VIN004",this.creationDate2,75000);
			Transaction transaction1 = new Transaction(2000, "Other", date, transactionType.CREDIT );
			Transaction transaction2 = new Transaction(-1500, "Others", date2, transactionType.DEBIT);
			Loan loan1 = new Loan("Hipotecary", 400000, Set.of(12, 24, 36));
			Loan loan2 = new Loan("Personal", 10000, Set.of(6,12,24));
			Loan loan3 = new Loan("Automotive", 300000, Set.of(12,24,36,48));
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(100000, 12, client1, loan2);
			ClientLoan clientLoan3 = new ClientLoan(150000, 6, client2, loan3);


			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);

			client1.addClientLoans(clientLoan1);
			client1.addClientLoans(clientLoan2);
			client2.addClientLoans(clientLoan3);



			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
        };
	}
}



