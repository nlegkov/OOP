package ru.nsu.legkov;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Получает карту из колоды и добавляет её в руку.
     *
     * @param deckCards Колода карт.
     */
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

    /**
     * Очищает руку, удаляя все карты.
     */
    public void clearCard() {
        cards.clear();
    }

    /**
     * Вычисляет и возвращает текущий счет руки.
     *
     * @return Текущий счет руки.
     */
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

    /**
     * Возвращает строковое представление последней карты в руке.
     *
     * @return Строковое представление последней карты или сообщение о пустой руке.
     */
    public String lastCard() {
        if (cards.isEmpty()) {
            return "Рука пуста";
        }
        return cards.get(cards.size() - 1).toString();
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Возвращает строковое представление руки, включая все карты.
     *
     * @return Строковое представление руки или сообщение о пустой руке.
     */
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