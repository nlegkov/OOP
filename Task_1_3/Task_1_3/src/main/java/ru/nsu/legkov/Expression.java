package ru.nsu.legkov;

import java.util.HashMap;
import java.util.Map;

public abstract class Expression {
    public int eval(String str) {
        return eval(parserStrMap(str));
    }

    public abstract int eval(Map<String, Integer> varMap);

    public abstract void print();

    public abstract Expression derivation(String var);

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

    protected static boolean isNumber(Expression e) {
        return e instanceof Number;
    }

    protected static boolean isZero(Expression e) {
        return e instanceof Number n && n.getValue() == 0;
    }

    protected static boolean isOne(Expression e) {
        return e instanceof Number n && n.getValue() == 1;
    }
}
