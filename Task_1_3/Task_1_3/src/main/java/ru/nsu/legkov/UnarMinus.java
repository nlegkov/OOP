package ru.nsu.legkov;

import java.util.Map;

public class UnarMinus extends Expression {
    protected Expression e;

    public UnarMinus(Expression var) {
        e = var;
    }

    @Override
    public int eval(Map<String, Integer> varMap) {
        return -1 * e.eval(varMap);
    }

    @Override
    public Expression derivation(String var) {
        return new UnarMinus(e.derivation(var));
    }

    @Override
    public int getPrioritet() {
        return PrioritetOper.UnMi.getPr();
    }

    @Override
    public String toString() {
        if (e.getPrioritet() < getPrioritet()) {
            return "-(" + e + ")";
        }

        return "-" + e;
    }

    @Override
    public Expression simplify() {
        Expression ee = e.simplify();

        if (ee instanceof UnarMinus u) {
            return u.e;
        }

        return new UnarMinus(ee);
    }
}
