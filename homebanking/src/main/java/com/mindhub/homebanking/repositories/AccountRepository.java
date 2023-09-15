package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long>{
   Account findByNumber(String number);
   boolean existsByNumber(String number);
   List<Account> findAllByClientAndAccOnTrue(Client client);
//   List<Account> findByClientAndStatusIsTrue(Client client);

}
