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
}
