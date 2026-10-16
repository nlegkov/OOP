package ru.nsu.legkov.core;

import java.util.Map;
import java.util.Objects;

/**
 * Общий предок бинарных операций.
 */
public abstract class BinaryExpression extends Expression {

    protected final Expression leftOperand;
    protected final Expression rightOperand;

    /**
     * Конструктор бинарного выражения.
     *
     * @param leftOperand  левый операнд
     * @param rightOperand правый операнд
     */
    public BinaryExpression(Expression leftOperand, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * Применяет бинарную операцию к двум целым числам.
     *
     * @param a значение левого операнда
     * @param b значение правого операнда
     * @return результат операции
     */
    protected abstract int apply(int a, int b);

    @Override
    public int eval(Map<String, Integer> variables) {
        return apply(leftOperand.eval(variables), rightOperand.eval(variables));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(leftOperand, that.leftOperand)
                && Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), leftOperand, rightOperand);
    }
}