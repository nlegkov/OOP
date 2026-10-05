package ru.nsu.legkov;

import java.util.Map;

/** Унарный минус. */
public class UnarMinus extends Expression {
    protected Expression e;

    /**
     * @param var выражение, перед которым ставится минус
     */
    public UnarMinus(Expression var) {
        this.e = var;
    }

    /**
     * @param varMap значения переменных
     * @return значение операнда со знаком минус
     */
    @Override
    public int eval(Map<String, Integer> varMap) {
        return -1 * e.eval(varMap);
    }

    /**
     * @param var имя переменной
     * @return производная унарного минуса
     */
    @Override
    public Expression derivation(String var) {
        return new UnarMinus(e.derivation(var));
    }

    /**
     * @return приоритет унарного минуса
     */
    @Override
    public int getPriority() {
        return PriorityOper.UnMi.getPr();
    }

    /**
     * @return строковое представление
     */
    @Override
    public String toString() {
        if (e.getPriority() < getPriority()) {
            return "-(" + e + ")";
        }

        return "-" + e;
    }

    /**
     * @return упрощённое выражение
     */
    @Override
    public Expression simplify() {
        Expression ee = e.simplify();

        if (ee.isUnarMinus()) {
            return ee.getOperand();
        }

        return new UnarMinus(ee);
    }

    /**
     * @param o объект для сравнения
     * @return true, если o — UnarMinus с равным операндом
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnarMinus u)) return false;
        return e.equals(u.e);
    }

    @Override
    public boolean isUnarMinus() {
        return true;
    }

    @Override
    public Expression getOperand() {
        return this.e;
    }
}
