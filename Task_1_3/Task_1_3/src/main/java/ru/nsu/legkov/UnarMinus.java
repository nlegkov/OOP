package ru.nsu.legkov;

import java.util.Map;

public class UnarMinus extends Expression {
    private Expression e;

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
}
