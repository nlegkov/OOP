package ru.nsu.legkov;

import java.util.Map;

/** Общий предок бинарных операций. */
public abstract class BinaryExpression extends Expression{
    protected final Expression l;
    protected final Expression r;

    /**
     * @param l левый операнд
     * @param r правый операнд
     */
    public BinaryExpression(Expression l, Expression r) {
        this.l = l;
        this.r = r;
    }

    /**
     * @param a значение левого операнда
     * @param b значение правого операнда
     * @return результат операции
     */
    protected abstract int apply(int a, int b);

    /**
     * @param variables значения переменных
     * @return значение выражения
     */
    @Override
    public int eval(Map<String, Integer> variables) {
        return apply(l.eval(variables), r.eval(variables));
    }
}
