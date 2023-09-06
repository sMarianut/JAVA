package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Card;
import org.springframework.stereotype.Service;


public interface CardService {
    void addCard(Card card);
    Card findByNumber(String number);
}
