package ru.nsu.legkov.operation;

import ru.nsu.legkov.atom.Number;
import ru.nsu.legkov.core.Expression;
import ru.nsu.legkov.core.PriorityOper;

import java.util.Map;

/**
 * Класс, представляющий унарный минус.
 */
public class UnarMinus extends Expression {

    private final Expression operand;

    /**
     * Конструктор унарного минуса.
     *
     * @param operand операнд унарного минуса
     */
    public UnarMinus(Expression operand) {
        this.operand = operand;
    }

    /**
     * Вычисляет значение выражения с унарным минусом.
     *
     * @param varMap значения переменных
     * @return значение выражения
     */
    @Override
    public int eval(Map<String, Integer> varMap) {
        return -operand.eval(varMap);
    }

    /**
     * Вычисляет производную унарного минуса.
     *
     * @param var имя переменной
     * @return производная
     */
    @Override
    public Expression derivation(String var) {
        return new UnarMinus(operand.derivation(var));
    }

    /**
     * Упрощает выражение с унарным минусом.
     *
     * @return упрощённое выражение
     */
    @Override
    public Expression simplify() {
        Expression simplified = operand.simplify();

        if (isZero(simplified)) {
            return new ru.nsu.legkov.atom.Number(0);
        }

        if (simplified.isUnarMinus()) {
            return simplified.getOperand();
        }

        if (simplified.isNumber()) {
            return new Number(-simplified.getValue());
        }

        return new UnarMinus(simplified);
    }

    /**
     * Проверяет, является ли выражение унарным минусом.
     *
     * @return true
     */
    @Override
    public boolean isUnarMinus() {
        return true;
    }

    /**
     * Возвращает внутренний операнд.
     *
     * @return операнд
     */
    @Override
    public Expression getOperand() {
        return operand;
    }

    /**
     * Сравнивает текущий объект с другим.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnarMinus unarMinus = (UnarMinus) o;
        return operand.equals(unarMinus.operand);
    }

    @Override
    public int hashCode() {
        return operand.hashCode();
    }

    @Override
    public String toString() {
        return "-(" + operand + ")";
    }

    @Override
    public int getPriority() {
        return PriorityOper.UNAR_MINUS.getPr();
    }
}