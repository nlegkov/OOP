package ru.nsu.legkov;

public class GameState {
    private int scoreDealer;
    private int scorePlayer;
    private int numberOfRounds;

    public GameState() {
        this.scoreDealer = 0;
        this.scorePlayer = 0;
        this.numberOfRounds = 0;
    }

    public int getScoreDealer() {
        return scoreDealer;
    }

    public int getScorePlayer() {
        return scorePlayer;
    }

    public void incrementScoreDealer() {
        this.scoreDealer++;
    }

    public void incrementScorePlayer() {
        this.scorePlayer++;
    }

    public void incrementRounds() {
        this.numberOfRounds++;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public String toStringScoreRound() {
        if (this.scoreDealer > this.scorePlayer) {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  " - Дилер лидирует!";
        } else if (this.scoreDealer < this.scorePlayer) {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  " - Вы лидируете!";
        } else {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  "- пока что ничья!";
        }
    }

    public String toStringScoreEndGame() {
        if (this.scoreDealer > this.scorePlayer) {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  " - Дилер победил(казино не обыграть)!";
        } else if (this.scoreDealer < this.scorePlayer) {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  " - Вы Победили!";
        } else {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  "- ничья!";
        }
    }
}