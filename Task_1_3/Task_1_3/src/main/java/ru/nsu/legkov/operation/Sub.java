package ru.nsu.legkov.operation;

import ru.nsu.legkov.core.BinaryExpression;
import ru.nsu.legkov.core.Expression;
import ru.nsu.legkov.atom.Number;
import ru.nsu.legkov.core.PriorityOper;

/**
 * Класс, представляющий операцию вычитания.
 */
public class Sub extends BinaryExpression {

    /**
     * Конструктор вычитания.
     *
     * @param leftOperand  левый операнд
     * @param rightOperand правый операнд
     */
    public Sub(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected int apply(int a, int b) {
        return a - b;
    }

    @Override
    public Expression derivation(String var) {
        return new Sub(leftOperand.derivation(var), rightOperand.derivation(var));
    }

    @Override
    public Expression simplify() {
        Expression left = leftOperand.simplify();
        Expression right = rightOperand.simplify();

        if (left.equals(right)) {
            return new Number(0);
        }

        if (isZero(right)) {
            return left;
        }

        if (left.isNumber() && right.isNumber()) {
            return new Number(left.getValue() - right.getValue());
        }

        if (right.isUnarMinus()) {
            return new Add(left, right.getOperand());
        }

        return new Sub(left, right);
    }

    @Override
    public int getPriority() {
        return PriorityOper.ADD_SUB.getPr();
    }

    @Override
    public String toString() {
        return leftOperand + " - " + rightOperand;
    }
}