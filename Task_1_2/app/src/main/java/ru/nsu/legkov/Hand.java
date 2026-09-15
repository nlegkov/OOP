package ru.nsu.legkov;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void getCard(DeckOfCards deckCards) {
        Card card = deckCards.getCard();

        if (card == null) {
            System.out.println("В колоде больше нет карт. Дилер взял новую колоду и тщательно перемешал её.");
            deckCards.newDeck();
            cards.add(deckCards.getCard());
        } else {
            cards.add(card);
        }
    }

    public void clearCard() {
        cards.clear();
    }

    public int getScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getValue();
            if (card.isAce()) {
                aces++;
            }
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    public String lastCard() {
        if (cards.isEmpty()) {
            return "Рука пуста";
        }
        return cards.get(cards.size() - 1).toString();
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "Рука пуста";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i).toString());
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}