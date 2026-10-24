package ru.nsu.legkov.entity;

import ru.nsu.legkov.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Hand представляет руку игрока или дилера в игре Blackjack.
 */
public class Hand {
    private final List<Card> cards;

    /**
     * Конструктор класса Hand.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Добавляет карту в руку.
     *
     * @param card Добавляемая карта.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Рассчитывает общую сумму очков карт на руке.
     *
     * @return Сумма очков карт.
     */
    public int getScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : cards) {
            if (!card.getHide()) {
                score += card.getValue();
                if (card.isAce()) {
                    aceCount++;
                }
            }
        }

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

    /**
     * Возвращает список карт в руке.
     *
     * @return Список карт.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Очищает руку от карт.
     */
    public void clearCard() {
        cards.clear();
    }

    /**
     * Возвращает описание последней добавленной карты.
     *
     * @return Строка с описанием карты или предупреждением, если рука пуста.
     */
    public String lastCard() {
        if (!cards.isEmpty()) {
            return cards.get(cards.size() - 1).toString();
        }
        return "Рука пуста";
    }

    /**
     * Возвращает строковое представление всей руки.
     *
     * @return Список всех карт через запятую.
     */
    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "Рука пуста";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i));
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}