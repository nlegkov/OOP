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

    @Override
    public Expression simplify() {
        Expression left = l.simplify();
        Expression right = r.simplify();

        if (l.equals(r)) {
            return new Number(0);
        }

        if (isZero(right)) {
            return left;
        }

        if (left instanceof Number ln && right instanceof  Number rn) {
            return new Number(ln.getValue() - rn.getValue());
        }

        if (right instanceof UnarMinus um) {
            return new Add(left, um.e);
        }

        return new Sub(left, right);
    }

    @Override
    public int getPrioritet() {
        return PrioritetOper.ADD_SUB.getPr();
    }

    @Override
    public String toString() {
        return l + " - " + r;
    }
}
