package ru.nsu.legkov;

public class Sub extends BinaryExpression{
    public Sub(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    protected int apply(int a, int b) {
        return a - b;
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public Expression derivation(String var) {
        return new Sub(l.derivation(var), r.derivation(var));
    }
}
