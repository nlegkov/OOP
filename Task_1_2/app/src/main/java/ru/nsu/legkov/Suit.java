package ru.nsu.legkov;

public enum Suit {
    PIKI("Пики"),
    CHERVI("Червы"),
    BUBNI("Бубны"),
    KRESTI("Крести");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
