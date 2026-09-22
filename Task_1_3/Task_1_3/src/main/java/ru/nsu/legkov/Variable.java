package ru.nsu.legkov;

import java.util.Map;

public class Variable extends Expression {
    private String name;

    Variable(String e) {
        this.name = e;
    }

    @Override
    public int eval(Map<String, Integer> varMap) {
        if (!varMap.containsKey(name)) {
            throw new IllegalArgumentException("Переменная " + name + " не задана");
        }

        return varMap.get(name);
    }

    @Override
    public void print() {
        System.out.print(name);
    }

    @Override
    public Expression derivation(String var) {
        if (this.name.equals(var)) {
            return new Number(1);
        }
        return new Number(0);
    }
}
