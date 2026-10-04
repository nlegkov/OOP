package ru.nsu.legkov;

import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный базовый класс для всех математических выражений.
 */
public abstract class Expression {

    /**
     * @param str строка означивания "x = 10; y = 13"
     * @return значение выражения
     * @throws IllegalArgumentException если переменная не означена
     */
    public int eval(String str) {
        return eval(ParseExpression.parserStrMap(str));
    }

    /**
     * @param varMap значения переменных
     * @return значение выражения
     * @throws IllegalArgumentException если переменная не означена
     */
    public abstract int eval(Map<String, Integer> varMap);

    /**
     * @param var имя переменной
     * @return производная по переменной
     */
    public abstract Expression derivation(String var);

    /**
     * @return упрощённое выражение; по умолчанию — сам объект
     */
    public Expression simplify() {
        return this;
    }

    /**
     * @param e узел дерева
     * @return true, если узел — константа 0
     */
    protected static boolean isZero(Expression e) {
        return e != null && e.isNumber() && e.getValue() == 0;
    }

    /**
     * @param e узел дерева
     * @return true, если узел — константа 1
     */
    protected static boolean isOne(Expression e) {
        return e != null && e.isNumber() && e.getValue() == 1;
    }

    /**
     * @return приоритет операции для расстановки скобок
     */
    public abstract int getPriority();

    public boolean isNumber() {
        return false;
    }
    public int getValue() {
        throw new UnsupportedOperationException();
    }
    public boolean isUnarMinus() {
        return false;
    }
    public Expression getOperand() {
        throw new UnsupportedOperationException();
    }
    public boolean isVariable() {
        return false;
    }
}
