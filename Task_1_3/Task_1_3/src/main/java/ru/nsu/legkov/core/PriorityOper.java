package ru.nsu.legkov.core;

/**
 * Приоритеты операций для расстановки скобок.
 */
public enum PriorityOper {

    ADD_SUB(1),
    MUL_DIV(2),
    UNAR_MINUS(3),
    VAR_NUM(4);

    private final int priority;

    /**
     * Конструктор элемента перечисления.
     *
     * @param priority числовой приоритет
     */
    PriorityOper(int priority) {
        this.priority = priority;
    }

    /**
     * Возвращает приоритет операции.
     *
     * @return числовой приоритет
     */
    public int getPr() {
        return priority;
    }
}