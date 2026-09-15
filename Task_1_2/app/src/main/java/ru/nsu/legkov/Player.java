package ru.nsu.legkov;

public class Player {
    private final Hand hand = new Hand();
    private int scoreWins = 0;

    public void takeCard(DeckOfCards cards) {
        if (hand.getScore() < 21) {
            hand.getCard(cards);
        }
    }

    public void resetHand() {
        hand.clearCard();
    }

    public void addWin() {
        scoreWins++;
    }

    public int getScoreWins() {
        return scoreWins;
    }

    public int getScore() {
        return hand.getScore();
    }

    public String lastCards() {
        return hand.lastCard();
    }

    @Override
    public String toString() {
        return "Рука игрока: [" + hand.toString() + "] -> " + getScore();
    }
}
