package ru.nsu.legkov;

import java.util.Map;

/** Переменная. */
public class Variable extends Expression {
    private String name;

    /**
     * @param e имя переменной
     */
    Variable(String e) {
        this.name = e;
    }

    /**
     * @param varMap значения переменных
     * @return значение переменной
     * @throws IllegalArgumentException если переменная не задана
     */
    @Override
    public int eval(Map<String, Integer> varMap) {
        if (!varMap.containsKey(name)) {
            throw new IllegalArgumentException("Переменная " + name + " не задана");
        }

        return varMap.get(name);
    }

    /**
     * @param var имя переменной, по которой берётся производная
     * @return 1, если имена совпадают, иначе 0
     */
    @Override
    public Expression derivation(String var) {
        if (this.name.equals(var)) {
            return new Number(1);
        }
        return new Number(0);
    }

    /**
     * @param o объект для сравнения
     * @return true, если o — Variable с тем же именем
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variable vl)) {
            return false;
        }
        return name.equals(vl.name);
    }

    /**
     * @return имя переменной
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @return приоритет переменной
     */
    @Override
    public int getPriority() {
        return PriorityOper.VAR_NUM.getPr();
    }

    @Override
    public boolean isVariable() {
        return true;
    }
}
