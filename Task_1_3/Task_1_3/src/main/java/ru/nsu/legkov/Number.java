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
    public void print() {
        System.out.print(val);
    }

    @Override
    public Expression derivation(String var) {
        return new Number(0);
    }

    public int getValue() {
        return val;
    }
}