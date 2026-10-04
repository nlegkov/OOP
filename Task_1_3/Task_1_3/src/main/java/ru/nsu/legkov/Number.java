package ru.nsu.legkov;

import java.util.Map;

/**
 * Константа.
 */
public class Number extends Expression {
    private int val;

    /**
     * @param number значение константы
     */
    public Number(int number) {
        this.val = number;
    }

    /**
     * @param varMap значения переменных (не используются)
     * @return значение константы
     */
    @Override
    public int eval(Map<String, Integer> varMap) {
        return val;
    }

    /**
     * @param var имя переменной
     * @return константа 0
     */
    @Override
    public Expression derivation(String var) {
        return new Number(0);
    }

    /**
     * @return значение константы
     */
    @Override
    public int getValue() {
        return val;
    }

    /**
     *
     * @return то что это число
     */
    @Override
    public boolean isNumber() {
        return true;
    }

    /**
     * @param o объект для сравнения
     * @return true, если o — Number с тем же значением
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Expression exp) || !exp.isNumber()) {
            return false;
        }
        return val == exp.getValue();
    }

    /**
     * @return строковое представление значения
     */
    @Override
    public String toString() {
        return "" + val;
    }

    /**
     * @return приоритет операции
     */
    @Override
    public int getPriority() {
        return PriorityOper.VAR_NUM.getPr();
    }
}