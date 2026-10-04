package ru.nsu.legkov;

/** Сложение. */
public class Add extends BinaryExpression {

    /**
     * @param l левый операнд
     * @param r правый операнд
     */
    public Add(Expression l, Expression r) {
        super(l, r);
    }

    /**
     * @param a значение левого операнда
     * @param b значение правого операнда
     * @return a + b
     */
    @Override
    protected int apply(int a, int b) {
        return a + b;
    }

    /**
     * @param var имя переменной
     * @return производная суммы
     */
    @Override
    public Expression derivation(String var) {
        return new Add(l.derivation(var), r.derivation(var));
    }

    /**
     * @return упрощённое выражение
     */
    @Override
    public Expression simplify() {
        Expression left = l.simplify();
        Expression right = r.simplify();

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
            return new Sub(left, right.getOperand());
        }
        return new Add(left, right);
    }

    /**
     * @return строковое представление
     */
    @Override
    public String toString() {
        return l + " + " + r;
    }

    /**
     * @return приоритет сложения
     */
    @Override
    public int getPriority() {
        return PriorityOper.ADD_SUB.getPr();
    }
}
