package ru.nsu.legkov;

import java.util.Map;

public abstract class BinaryExpression extends Expression{
    protected final Expression l;
    protected final Expression r;

    public BinaryExpression(Expression l, Expression r) {
        this.l = l;
        this.r = r;
    }

    protected abstract int apply(int a, int b);

    public abstract String getOperator();

    @Override
    public void print() {
        System.out.print("(");
        l.print();
        System.out.print(getOperator());
        r.print();
        System.out.print(")");
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return apply(l.eval(variables), r.eval(variables));
    }
}
