package ru.nsu.legkov.core;

import java.util.Map;

/**
 * Абстрактный базовый класс для всех математических выражений.
 */
public abstract class Expression {

    /**
     * Вычисляет значение выражения по строке с переменными.
     *
     * @param str строка означивания "x = 10; y = 13"
     * @return значение выражения
     * @throws IllegalArgumentException если переменная не означена
     */
    public int eval(String str) {
        return eval(ParseExpression.parserStrMap(str));
    }

    /**
     * Вычисляет значение выражения с картой переменных.
     *
     * @param varMap значения переменных
     * @return значение выражения
     * @throws IllegalArgumentException если переменная не означена
     */
    public abstract int eval(Map<String, Integer> varMap);

    /**
     * Вычисляет производную выражения по заданной переменной.
     *
     * @param var имя переменной
     * @return производная по переменной
     */
    public abstract Expression derivation(String var);

    /**
     * Упрощает математическое выражение.
     *
     * @return упрощённое выражение; по умолчанию — сам объект
     */
    public Expression simplify() {
        return this;
    }

    /**
     * Проверяет, является ли выражение нулевой константой.
     *
     * @param e узел дерева
     * @return true, если узел — константа 0
     */
    protected static boolean isZero(Expression e) {
        return e != null && e.isNumber() && e.getValue() == 0;
    }

    /**
     * Проверяет, является ли выражение единичной константой.
     *
     * @param e узел дерева
     * @return true, если узел — константа 1
     */
    protected static boolean isOne(Expression e) {
        return e != null && e.isNumber() && e.getValue() == 1;
    }

    /**
     * Возвращает приоритет операции.
     *
     * @return приоритет операции для расстановки скобок
     */
    public abstract int getPriority();

    /**
     * Проверяет, является ли выражение числом.
     *
     * @return true, если выражение число
     */
    public boolean isNumber() {
        return false;
    }

    /**
     * Возвращает числовое значение.
     *
     * @return числовое значение
     */
    public int getValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Проверяет, является ли выражение унарным минусом.
     *
     * @return true, если это унарный минус
     */
    public boolean isUnarMinus() {
        return false;
    }

    /**
     * Возвращает операнд унарной операции.
     *
     * @return операнд
     */
    public Expression getOperand() {
        throw new UnsupportedOperationException();
    }
}