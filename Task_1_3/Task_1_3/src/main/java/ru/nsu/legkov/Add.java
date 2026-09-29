package ru.nsu.legkov;

public class Add extends BinaryExpression {

    public Add(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    protected int apply(int a, int b) {
        return a + b;
    }

    @Override
    public String getOperator() {
        return "+";
    }

    @Override
    public Expression derivation(String var) {
        return new Add(l.derivation(var), r.derivation(var));
    }

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

        if (left instanceof Number ln && right instanceof  Number rn) {
            return new Number(ln.getValue() + rn.getValue());
        }

        return new Add(left, right);
    }
}
