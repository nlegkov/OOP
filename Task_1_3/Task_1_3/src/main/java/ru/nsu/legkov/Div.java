package ru.nsu.legkov;

public class Div extends BinaryExpression {

    public Div(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    protected int apply(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Деление на ноль");
        }
        return a/b;
    }

    @Override
    public String getOperator() {
        return "/";
    }

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

    @Override
    public int getPrioritet() {
        return PrioritetOper.MUL_DIV.getPr();
    }

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
