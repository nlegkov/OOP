package ru.nsu.legkov.operation;

import ru.nsu.legkov.atom.Number;
import ru.nsu.legkov.core.BinaryExpression;
import ru.nsu.legkov.core.Expression;
import ru.nsu.legkov.core.PriorityOper;

/**
 * Класс, представляющий операцию сложения.
 */
public class Add extends BinaryExpression {

    /**
     * Конструктор сложения.
     *
     * @param leftOperand  левый операнд
     * @param rightOperand правый операнд
     */
    public Add(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int apply(int a, int b) {
        return a + b;
    }

    @Override
    public Expression derivation(String var) {
        return new Add(leftOperand.derivation(var), rightOperand.derivation(var));
    }

    @Override
    public Expression simplify() {
        Expression left = leftOperand.simplify();
        Expression right = rightOperand.simplify();

        if (isZero(left) && isZero(right)) {
            return new Number(0);
        }

        if (isZero(left)) {
            return right;
        }
        if (isZero(right)) {
            return left;
        }

        if (left.isNumber() && right.isNumber()) {
            return new Number(left.getValue() + right.getValue());
        }

        if (left.equals(right)) {
            return new Mul(new Number(2), left);
        }

        if (right.isUnarMinus()) {
            return new Sub(left, right.getOperand()).simplify();
        }

        return new Add(left, right);
    }

    @Override
    public String toString() {
        return leftOperand + " + " + rightOperand;
    }

    @Override
    public int getPriority() {
        return PriorityOper.ADD_SUB.getPr();
    }
}