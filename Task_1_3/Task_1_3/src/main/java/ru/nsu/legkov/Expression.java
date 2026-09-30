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
        return eval(parserStrMap(str));
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


    private Map<String, Integer> parserStrMap(String str) {
        Map<String, Integer> map = new HashMap<>();
        if (str == null || str.trim().isEmpty()) {
            return map;
        }
        String[] parts = str.split(";");
        for (String part : parts) {
            String[] s = part.split("=");
            if (s.length == 2) {
                map.put(s[0].trim(), Integer.parseInt(s[1].trim()));
            }
        }

        return map;
    }

    /**
     * @param e узел дерева
     * @return true, если узел — константа 0
     */
    protected static boolean isZero(Expression e) {
        return e instanceof Number n && n.getValue() == 0;
    }

    /**
     * @param e узел дерева
     * @return true, если узел — константа 1
     */
    protected static boolean isOne(Expression e) {
        return e instanceof Number n && n.getValue() == 1;
    }

    /**
     * @return приоритет операции для расстановки скобок
     */
    public abstract int getPrioritet();
}
