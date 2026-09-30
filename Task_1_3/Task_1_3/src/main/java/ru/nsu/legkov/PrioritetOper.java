package ru.nsu.legkov;

public enum PrioritetOper {
    ADD_SUB(1),
    MUL_DIV(2),
    UnMi(3),
    VAR_NUM(4);


    private final int pr;

    PrioritetOper(int pr) {
        this.pr = pr;
    }

    public int getPr() {
        return pr;
    }
}
