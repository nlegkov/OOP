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
}
