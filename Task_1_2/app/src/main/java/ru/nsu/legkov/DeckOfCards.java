package ru.nsu.legkov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckOfCards {
    private final List<Card> cards = new ArrayList<>();

    public DeckOfCards() {
        newDeck();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card getCard() {
        if (cards.isEmpty()) {
            return null;
        }

        return cards.remove(0);
    }

    public void newDeck() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
    }
}