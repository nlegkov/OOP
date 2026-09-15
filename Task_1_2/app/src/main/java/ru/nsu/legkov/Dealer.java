package ru.nsu.legkov;

public class Dealer {
    private final Hand hand = new Hand();
    private int scoreWins = 0;

    public void takeCard(DeckOfCards cards) {
        if (hand.getScore() < 17) {
            hand.getCard(cards);
        }
    }

    public String getHiddenHandString() {
        if (hand.getCards().isEmpty()) {
            return "[]";
        }

        return "Рука дилера: [" + hand.getCards().get(0) + ", <закрытая карта>]";
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
        return "Рука дилера: [" + hand.toString() + "] -> " + getScore();
    }
}
