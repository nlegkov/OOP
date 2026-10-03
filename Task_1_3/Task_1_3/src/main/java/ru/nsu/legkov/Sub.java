package ru.nsu.legkov;

/** Вычитание. */
public class Sub extends BinaryExpression{

    /**
     * @param l левый операнд
     * @param r правый операнд
     */
    public Sub(Expression l, Expression r) {
        super(l, r);
    }

    /**
     * @param a значение левого операнда
     * @param b значение правого операнда
     * @return a - b
     */
    @Override
    protected int apply(int a, int b) {
        return a - b;
    }

    /**
     * @param var имя переменной
     * @return производная разности
     */
    @Override
    public Expression derivation(String var) {
        return new Sub(l.derivation(var), r.derivation(var));
    }

    /**
     * @return упрощённое выражение
     */
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

    /**
     * @return приоритет вычитания
     */
    @Override
    public int getPrioritet() {
        return PrioritetOper.ADD_SUB.getPr();
    }

    /**
     * @return строковое представление
     */
    @Override
    public String toString() {
        return l + " - " + r;
    }
}
