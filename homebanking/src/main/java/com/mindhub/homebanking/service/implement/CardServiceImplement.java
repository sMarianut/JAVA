package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardRepository cardRepository;

    @Override
    public void addCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public List<Card> getCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<CardDTO> getCardsDTO() {
        return getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }


    @Override
    public Card findById(long id) {
        return cardRepository.findById(id);
    }
}
