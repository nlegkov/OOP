package ru.nsu.legkov;

import java.util.Map;

public class Number extends Expression {
    private int val;

    public Number(int number) {
        this.val = number;
    }


    @Override
    public int eval(Map<String, Integer> varMap) {
        return val;
    }

    @Override
    public Expression derivation(String var) {
        return new Number(0);
    }

    public int getValue() {
        return val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Number)) {
            return false;
        }
        return val == ((Number) o).getValue();
    }

    @Override
    public String toString() {
        return "" + val;
    }

    @Override
    public int getPrioritet() {
        return PrioritetOper.VAR_NUM.getPr();
    }
}