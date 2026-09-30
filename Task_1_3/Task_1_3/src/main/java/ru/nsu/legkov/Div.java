package ru.nsu.legkov;

/** Деление. */
public class Div extends BinaryExpression {

    /**
     * @param l левый операнд
     * @param r правый операнд
     */
    public Div(Expression l, Expression r) {
        super(l, r);
    }

    /**
     * @param a значение левого операнда
     * @param b значение правого операнда
     * @return a / b
     * @throws ArithmeticException если b == 0
     */
    @Override
    protected int apply(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return a/b;
    }

    /**
     * @param var имя переменной
     * @return производная частного
     */
    @Override
    public Expression derivation(String var) {
        return new Div(
                new Sub(
                        new Mul(l.derivation(var), r),
                        new Mul(l, r.derivation(var))
                ),
                new Mul(r, r)
        );
    }

    /**
     * @return упрощённое выражение
     * @throws ArithmeticException если знаменатель равен нулю
     */
    @Override
    public Expression simplify() {
        Expression left = l.simplify();
        Expression right = r.simplify();

        if (isZero(right)) {
            throw new ArithmeticException("div in 0");
        }

        if (isZero(left)) {
            return new Number(0);
        }

        if (isOne(right)) {
            return left;
        }

        if (left instanceof Number ln && right instanceof  Number rn) {
            return new Number(ln.getValue() / rn.getValue());
        }

        return new Div(left, right);
    }

    /**
     * @return приоритет деления
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
        if (r.getPrioritet() <= getPrioritet()) {
            strR = "(" + r + ")";
        }

        return strL + " / " + strR;
    }
}
