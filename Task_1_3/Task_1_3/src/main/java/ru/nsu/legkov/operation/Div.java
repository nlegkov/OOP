package ru.nsu.legkov.operation;

import ru.nsu.legkov.core.BinaryExpression;
import ru.nsu.legkov.core.Expression;
import ru.nsu.legkov.atom.Number;
import ru.nsu.legkov.core.PriorityOper;

/**
 * Класс, представляющий операцию деления.
 */
public class Div extends BinaryExpression {

    /**
     * Конструктор деления.
     *
     * @param leftOperand  левый операнд
     * @param rightOperand правый операнд
     */
    public Div(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int apply(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return a / b;
    }

    @Override
    public Expression derivation(String var) {
        return new Div(
                new Sub(
                        new Mul(leftOperand.derivation(var), rightOperand),
                        new Mul(leftOperand, rightOperand.derivation(var))
                ),
                new Mul(rightOperand, rightOperand)
        );
    }

    @Override
    public Expression simplify() {
        Expression left = leftOperand.simplify();
        Expression right = rightOperand.simplify();

        if (isZero(right)) {
            throw new ArithmeticException("div in 0");
        }

        if (isZero(left)) {
            return new Number(0);
        }

        if (isOne(right)) {
            return left;
        }

        if (left.isNumber() && right.isNumber()) {
            return new Number(left.getValue() / right.getValue());
        }

        return new Div(left, right);
    }

    @Override
    public int getPriority() {
        return PriorityOper.MUL_DIV.getPr();
    }

    @Override
    public String toString() {
        String strL = leftOperand.toString();
        String strR = rightOperand.toString();

        if (leftOperand.getPriority() < getPriority()) {
            strL = "(" + leftOperand + ")";
        }
        if (rightOperand.getPriority() <= getPriority()) {
            strR = "(" + rightOperand + ")";
        }

        return strL + " / " + strR;
    }
}