package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CardService {
    void addCard(Card card);
    Card findByNumber(String number);
    List<Card> getCards();
    List<CardDTO> getCardsDTO();
    Card findById(long id);
}
