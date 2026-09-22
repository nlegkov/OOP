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
}
