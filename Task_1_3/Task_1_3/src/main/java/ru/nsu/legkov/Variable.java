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
    public Expression derivation(String var) {
        if (this.name.equals(var)) {
            return new Number(1);
        }
        return new Number(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variable)) {
            return false;
        }
        return name.equals(((Variable) o).name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int getPrioritet() {
        return PrioritetOper.VAR_NUM.getPr();
    }
}
