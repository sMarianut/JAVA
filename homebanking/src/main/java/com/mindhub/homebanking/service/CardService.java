package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    void addCard(Card card);
    Card findByNumber(String number);
}
