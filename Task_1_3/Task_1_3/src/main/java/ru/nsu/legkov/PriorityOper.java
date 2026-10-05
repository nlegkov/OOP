package ru.nsu.legkov;

/** Приоритеты операций для расстановки скобок. */
public enum PriorityOper {
    ADD_SUB(1),
    MUL_DIV(2),
    UnMi(3),
    VAR_NUM(4);


    private final int pr;

    /**
     * @param pr числовой приоритет
     */
    PriorityOper(int pr) {
        this.pr = pr;
    }

    /**
     * @return числовой приоритет
     */
    public int getPr() {
        return pr;
    }
}
