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
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    private CardService cardService;
    @Autowired
    private ClientService clientService;
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
        } while (cardService.findByNumber(generatedNumber) != null);

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
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getCards().stream().collect(toList());
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication,
                                             @RequestParam String cardType,
                                             @RequestParam String cardColor) {
        String clientA = (authentication.getName());
        Client current = clientService.findByEmail(clientA);
        if(cardType.isBlank() ){
            return new ResponseEntity<>("You cannot apply for a card with blank values.", HttpStatus.FORBIDDEN);
        }
        if(cardColor.isBlank() ){
            return new ResponseEntity<>("You cannot apply for a card with blank values.", HttpStatus.FORBIDDEN);
        }
        CardType.valueOf(cardType);
        CardColor.valueOf(cardColor);
        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("You has to fill all the requirements.", HttpStatus.FORBIDDEN);

        }
        List<CardType> currentTypes = current.getCards().stream().map(card -> card.getCardType()).collect(toList());
        List<CardColor> currentColors = current.getCards().stream().map(card -> card.getCardColor()).collect(toList());
        List<Card> filterCard = cardRepository.findByClientAndIsOnCardTrue(current);
        for (Card card : filterCard) {
            if (card.getCardType().equals(CardType.valueOf(cardType)) && card.getCardColor().equals(CardColor.valueOf(cardColor))) {
                return new ResponseEntity<>("You already have this card.", HttpStatus.FORBIDDEN);
            }
        }
        String number = CardNumber();
        int cvvR = cvv();
        Card newCard = new Card(current.getFirstName()+" "+current.getLastName(), CardType.valueOf(cardType), CardColor.valueOf(cardColor), number,cvvR, LocalDate.now(), LocalDate.now().plusYears(5), true);
        current.addCards(newCard);
        clientService.addClient(current);
        cardService.addCard(newCard);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/api/clients/current/deleteCard")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam long id){
        Client current = clientService.findByEmail(authentication.getName());
        Card currentCard = cardService.findById(id);
        boolean exist = current.getCards().contains(currentCard);

        if (current == null){
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);

        }
        if (currentCard == null){
            return new ResponseEntity<>("Card not found.", HttpStatus.NOT_FOUND);

        }
        if (!exist){
            return new ResponseEntity<>("You are not the owner of this card", HttpStatus.FORBIDDEN);

        }
        if (currentCard.isOnCard()== false){
            return new ResponseEntity<>("You cannot remove a removed card.", HttpStatus.BAD_REQUEST);
        }
        else {
            currentCard.setOnCard(false);
            cardService.addCard(currentCard);
        }
    return new ResponseEntity<>("Your card was succesfully removed.", HttpStatus.OK);
    }
}

