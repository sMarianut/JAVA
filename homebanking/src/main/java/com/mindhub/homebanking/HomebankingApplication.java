package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.transactionType;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;


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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melbax@gmail.com");
			Account account1 = new Account("VIN001", creationDate, 5000);
			Account account2 = new Account("VIN002", creationDate2, 7000);
			Transaction transaction1 = new Transaction(2000, "Other", date, transactionType.CREDIT );
			Transaction transaction2 = new Transaction(-1500, "Others", date2, transactionType.DEBIT);
			client1.addAccount(account1);
			client1.addAccount(account2);
			clientRepository.save(client1);
			accountRepository.save(account1);
			accountRepository.save(account2);
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);


			Client client2 = new Client("Marianut", "Makanouchi",  "Ippo@Hajime.com");
			Account account3 = new Account("VIN003",this.creationDate,50000);
			Account account4 = new Account("VIN004",this.creationDate2,75000);
			client2.addAccount(account3);
			client2.addAccount(account4);
			clientRepository.save(client2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
        };
	}
}



