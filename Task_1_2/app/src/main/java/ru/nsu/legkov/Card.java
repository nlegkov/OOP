package ru.nsu.legkov;

/**
 * Класс Card представляет карту в игре.
 * Каждая карта имеет масть и ранг.
 */
public class Card {
    private final Suit suit;
    private final Rank rank;
    private boolean isHand;

    /**
     * Конструктор класса Card.
     * Создает карту с указанной мастью и рангом.
     *
     * @param suit Масть карты.
     * @param rank Ранг карты.
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.isHand = false;
    }

    /**
     * Получает масть карты.
     *
     * @return Масть карты.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Получает ранг карты.
     *
     * @return Ранг карты.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Получает значение карты.
     * Значение карты определяется её рангом.
     *
     * @return Значение карты.
     */
    public int getValue() {
        return rank.getValue();
    }

    /**
     * Проверяет, является ли карта тузом.
     *
     * @return true, если карта является тузом; false в противном случае.
     */
    public boolean isAce() {
        return rank == Rank.ACE;
    }

    /**
     * Возвращает строковое представление карты.
     *
     * @return Строковое представление карты в формате "Ранг Масть (Значение)".
     */
    @Override
    public String toString() {
        if (!isHand) {
            return rank.getName() + " " + suit.getName() + " (" + getValue() + ")";
        }
        return "<скрытая карта>";
    }

    /**
     * Проверяет, является ли карта частью руки.
     *
     * @param  true, если карта является частью руки; false в противном случае.
     */
    public void setHand(boolean isHand) {
        this.isHand = isHand;
    }
}
