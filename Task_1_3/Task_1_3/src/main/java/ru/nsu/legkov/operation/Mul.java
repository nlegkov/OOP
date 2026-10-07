package ru.nsu.legkov.operation;

import ru.nsu.legkov.atom.Number;
import ru.nsu.legkov.core.BinaryExpression;
import ru.nsu.legkov.core.Expression;
import ru.nsu.legkov.core.PriorityOper;

/**
 * Класс, представляющий операцию умножения.
 */
public class Mul extends BinaryExpression {

    /**
     * Конструктор умножения.
     *
     * @param leftOperand  левый операнд
     * @param rightOperand правый операнд
     */
    public Mul(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int apply(int a, int b) {
        return a * b;
    }

    @Override
    public Expression derivation(String var) {
        return new Add(
                new Mul(leftOperand.derivation(var), rightOperand),
                new Mul(leftOperand, rightOperand.derivation(var))
        );
    }

    @Override
    public Expression simplify() {
        Expression left = leftOperand.simplify();
        Expression right = rightOperand.simplify();

        if (isZero(left) || isZero(right)) {
            return new Number(0);
        }

        if (isOne(left)) {
            return right;
        }
        if (isOne(right)) {
            return left;
        }

        if (left.isNumber() && right.isNumber()) {
            return new Number(left.getValue() * right.getValue());
        }

        return new Mul(left, right);
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
        if (rightOperand.getPriority() < getPriority()) {
            strR = "(" + rightOperand + ")";
        }

        return strL + " * " + strR;
    }
}