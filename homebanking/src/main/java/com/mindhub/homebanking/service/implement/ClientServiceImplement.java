package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClient(Long id) {
       return new ClientDTO(findById(id));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ClientDTO getClientDTO(String email) {
        return new ClientDTO(findByEmail(email));
    }

    @Override
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getClientsDTO() {
        return getClients().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @Override
    public List<ClientLoanDTO> getClientLoans(Client client) {
        return client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(toList());
    }

}
