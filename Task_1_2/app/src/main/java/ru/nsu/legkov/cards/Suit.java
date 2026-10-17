package ru.nsu.legkov.cards;

/**
 * Перечисление мастей карт.
 */
public enum Suit {
    PIKI("Пики"),
    CHERVI("Червы"),
    BUBNI("Бубны"),
    KRESTI("Крести");

    private final String name;

    /**
     * Конструктор масти.
     *
     * @param name Русское название масти.
     */
    Suit(String name) {
        this.name = name;
    }

    /**
     * Получает название масти.
     *
     * @return Название масти.
     */
    public String getName() {
        return name;
    }
}