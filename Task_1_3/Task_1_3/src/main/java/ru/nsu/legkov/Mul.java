package ru.nsu.legkov;

/** Умножение. */
public class Mul extends BinaryExpression{

    /**
     * @param l левый операнд
     * @param r правый операнд
     */
    public Mul(Expression l, Expression r) {
        super(l, r);
    }

    /**
     * @param a значение левого операнда
     * @param b значение правого операнда
     * @return a * b
     */
    @Override
    protected int apply(int a, int b) {
        return a*b;
    }

    /**
     * @param var имя переменной
     * @return производная произведения
     */
    @Override
    public Expression derivation(String var) {
        return new Add(
                new Mul(l.derivation(var), r),
                new Mul(l, r.derivation(var))
        );
    }

    /**
     * @return упрощённое выражение
     */
    @Override
    public Expression simplify() {
        Expression left = l.simplify();
        Expression right = r.simplify();

        if (isZero(left) || isZero(right)) {
            return new Number(0);
        }

        if (isOne(left)) {
            return right;
        }
        if (isOne(right)) {
            return left;
        }

        if (left instanceof Number ln && right instanceof  Number rn) {
            return new Number(ln.getValue() * rn.getValue());
        }

        return new Mul(left, right);
    }

    /**
     * @return приоритет умножения
     */
    @Override
    public int getPrioritet() {
        return PrioritetOper.MUL_DIV.getPr();
    }

    /**
     * @return строковое представление
     */
    @Override
    public String toString() {
        String strL = l.toString();
        String strR = r.toString();

        if (l.getPrioritet() < getPrioritet()) {
            strL = "(" + l + ")";
        }
        if (r.getPrioritet() < getPrioritet()) {
            strR = "(" + r + ")";
        }

        return strL + " * " + strR;
    }
}
