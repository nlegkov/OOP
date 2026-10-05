package ru.nsu.legkov;

import java.util.Map;

/**
 * Числовая константа.
 */
public class Number extends Expression {

    private final int val;

    /**
     * Конструктор числовой константы.
     *
     * @param number значение константы
     */
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

    @Override
    public int getValue() {
        return val;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Expression exp) || !exp.isNumber()) {
            return false;
        }
        return val == exp.getValue();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(val);
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public int getPriority() {
        return PriorityOper.VAR_NUM.getPr();
    }
}