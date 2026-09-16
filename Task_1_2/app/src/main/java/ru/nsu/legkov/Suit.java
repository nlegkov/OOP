package ru.nsu.legkov;

/**
 * Перечисление мастей карт.
 */
public enum Suit {
    PIKI("Пики"),
    CHERVI("Червы"),
    BUBNI("Бубны"),
    KRESTI("Крести");

    private final String name;

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
