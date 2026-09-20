package ru.nsu.legkov;

/**
 * Класс GameState отслеживает текущее состояние игры.
 * Хранит количество очков игрока, дилера и номер текущего раунда.
 */
public class GameState {
    private int scoreDealer;
    private int scorePlayer;
    private int numberOfRounds;

    /**
     * Конструктор класса GameState.
     * Инициализирует счет дилера, счет игрока и номер раунда нулями.
     */
    public GameState() {
        this.scoreDealer = 0;
        this.scorePlayer = 0;
        this.numberOfRounds = 0;
    }

    /**
     * Получает текущий счет дилера.
     *
     * @return Текущий счет дилера.
     */
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

    /**
     * Увеличивает счетчик сыгранных раундов.
     */
    public void incrementRounds() {
        this.numberOfRounds++;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    /**
     * Формирует строковое представление счета после раунда.
     *
     * @return Текстовое описание промежуточного счета.
     */
    public String toStringScoreRound() {
        if (this.scoreDealer > this.scorePlayer) {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  " - Дилер лидирует!";
        } else if (this.scoreDealer < this.scorePlayer) {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  " - Вы лидируете!";
        } else {
            return "Счет: " + this.scorePlayer + ":" + this.scoreDealer +  "- пока что ничья!";
        }
    }

    /**
     * Формирует строковое представление итогового счета игры.
     *
     * @return Текстовое описание финального счета.
     */
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