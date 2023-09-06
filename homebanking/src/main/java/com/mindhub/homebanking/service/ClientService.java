package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClientService {
    void addClient(Client client);

    Client findById(Long id);

    ClientDTO getClient(Long id);
    Client findByEmail(String email);
    ClientDTO getClientDTO(String email);
    List<Client> getClients();
    List<ClientDTO> getClientsDTO();
}
