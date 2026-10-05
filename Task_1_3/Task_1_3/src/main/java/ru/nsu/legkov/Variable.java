package ru.nsu.legkov;

import java.util.Map;

/**
 * Переменная в математическом выражении.
 */
public class Variable extends Expression {

    private final String name;

    /**
     * Конструктор переменной.
     *
     * @param name имя переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Вычисляет значение переменной по карте значений.
     *
     * @param varMap карта значений переменных
     * @return значение переменной
     */
    @Override
    public int eval(Map<String, Integer> varMap) {
        if (!varMap.containsKey(name)) {
            throw new IllegalArgumentException("Variable not found: " + name);
        }
        return varMap.get(name);
    }

    /**
     * Вычисляет производную переменной.
     *
     * @param var имя переменной, по которой берется производная
     * @return 1 если переменная совпадает, иначе 0
     */
    @Override
    public Expression derivation(String var) {
        if (name.equals(var)) {
            return new Number(1);
        }
        return new Number(0);
    }

    /**
     * Возвращает имя переменной.
     *
     * @return имя переменной
     */
    public String getName() {
        return name;
    }

    /**
     * Сравнивает переменную с другим объектом.
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
        Variable variable = (Variable) o;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int getPriority() {
        return PriorityOper.VAR_NUM.getPr();
    }
}