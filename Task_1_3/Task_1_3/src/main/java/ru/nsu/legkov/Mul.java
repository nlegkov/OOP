package ru.nsu.legkov;

public class Mul extends BinaryExpression{
    public Mul(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    protected int apply(int a, int b) {
        return a*b;
    }

    @Override
    public String getOperator() {
        return "*";
    }

    @Override
    public Expression derivation(String var) {
        return new Add(
                new Mul(l.derivation(var), r),
                new Mul(l, r.derivation(var))
        );
    }

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
        if (r.getPrioritet() < getPrioritet()) {
            strR = "(" + r + ")";
        }

        return strL + " * " + strR;
    }
}
