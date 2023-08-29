package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class CardsController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    public String CardNumber() {
        String generatedNumber;
        do {
            StringBuilder number = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                number.append(section());
                if (i < 3) {
                    number.append("-");
                }
            }
            generatedNumber = number.toString();
        } while (cardRepository.findByNumber(generatedNumber) != null);

        return generatedNumber;
    }

    public String section(){
        int section=(int)(Math.random()*10000);
        return String.format("%04d", section);
    }
    public int cvv(){
        int cvv=(int)(Math.random()*900) + 100;
        return cvv;
    }

@GetMapping("/api/clients/current/cards")
public List<CardDTO> getCards(Authentication authentication){
    return new ClientDTO(clientRepository.findByEmail(authentication.getName())).getCards().stream().collect(toList());
}

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication,
                                             @RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor) {
        String clientA = (authentication.getName());
        Client current = clientRepository.findByEmail(clientA);
        List<CardType> currentTypes = current.getCards().stream().map(card -> card.getCardType()).collect(toList());
        List<CardColor> currentColors = current.getCards().stream().map(card -> card.getCardColor()).collect(toList());



        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("You has to fill all the requirements.", HttpStatus.BAD_REQUEST);

        }
        for (Card card : current.getCards()) {
            if (card.getCardType().equals(cardType) && card.getCardColor().equals(cardColor)) {
                return new ResponseEntity<>("You already have this card.", HttpStatus.FORBIDDEN);
            }
        }
        String number = CardNumber();
        int cvvR = cvv();
        Card newCard = new Card(current.getFirstName()+" "+current.getLastName(), cardType, cardColor, number,cvvR, LocalDate.now(), LocalDate.now().plusYears(5));
        current.addCards(newCard);
        clientRepository.save(current);
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
