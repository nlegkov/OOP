package ru.nsu.legkov;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import ru.nsu.legkov.atom.Number;
import ru.nsu.legkov.atom.Variable;
import ru.nsu.legkov.core.ParseExpression;
import ru.nsu.legkov.core.Expression;
import ru.nsu.legkov.core.PriorityOper;
import ru.nsu.legkov.operation.Add;
import ru.nsu.legkov.operation.Div;
import ru.nsu.legkov.operation.Mul;
import ru.nsu.legkov.operation.Sub;
import ru.nsu.legkov.operation.UnarMinus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Тесты для проверки корректности парсинга выражений.
 */
class ParseExpressionTest {

    private final ParseExpression parse = new ParseExpression();

    // ---------- Number / Variable ----------

    @Test
    void numberEval() {
        assertEquals(5, new Number(5).eval(""));
        assertEquals(5, new Number(5).eval((String) null));
    }

    @Test
    void numberDerivation() {
        assertEquals(new Number(0), new Number(5).derivation("x"));
    }

    @Test
    void numberEqualsAndHashCode() {
        assertEquals(new Number(5), new Number(5));
        assertEquals(new Number(5).hashCode(), new Number(5).hashCode());
        assertNotEquals(new Number(5), new Number(6));
        assertNotEquals(new Number(5), "строка");
        assertEquals(new Number(5), new Number(5));
        assertFalse(new Number(5).equals(null));
    }

    @Test
    void numberToString() {
        assertEquals("5", new Number(5).toString());
        assertEquals("-3", new Number(-3).toString());
    }

    @Test
    void numberPrioritet() {
        assertEquals(PriorityOper.VAR_NUM.getPr(), new Number(1).getPriority());
    }

    @Test
    void variableEval() {
        Map<String, Integer> m = new HashMap<>();
        m.put("x", 10);
        assertEquals(10, new Variable("x").eval(m));
    }

    @Test
    void variableEvalNotDefined() {
        assertThrows(IllegalArgumentException.class,
                () -> new Variable("x").eval(new HashMap<>()));
    }

    @Test
    void variableEvalString() {
        Expression e = new Variable("x");
        assertEquals(10, e.eval("x = 10"));
        assertEquals(10, e.eval("x = 10; y = 13"));
    }

    @Test
    void variableDerivation() {
        assertEquals(new Number(1), new Variable("x").derivation("x"));
        assertEquals(new Number(0), new Variable("x").derivation("y"));
    }

    @Test
    void variableEqualsAndHashCode() {
        assertEquals(new Variable("x"), new Variable("x"));
        assertEquals(new Variable("x").hashCode(), new Variable("x").hashCode());
        assertNotEquals(new Variable("x"), new Variable("y"));
        assertNotEquals(new Variable("x"), new Number(1));
    }

    @Test
    void variableToString() {
        assertEquals("abc", new Variable("abc").toString());
    }

    @Test
    void variablePrioritet() {
        assertEquals(PriorityOper.VAR_NUM.getPr(), new Variable("x").getPriority());
    }

    // ---------- Binary operations: eval ----------

    @Test
    void binaryEval() {
        Expression e = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));
        assertEquals(23, e.eval("x = 10"));
    }

    @Test
    void addEval() {
        assertEquals(5, new Add(new Number(2), new Number(3)).eval(""));
    }

    @Test
    void subEval() {
        assertEquals(-1, new Sub(new Number(2), new Number(3)).eval(""));
    }

    @Test
    void mulEval() {
        assertEquals(6, new Mul(new Number(2), new Number(3)).eval(""));
    }

    @Test
    void divEval() {
        assertEquals(2, new Div(new Number(6), new Number(3)).eval(""));
    }

    @Test
    void divEvalByZero() {
        assertThrows(ArithmeticException.class,
                () -> new Div(new Number(1), new Number(0)).eval(""));
    }

    // ---------- Binary operations: derivation ----------

    @Test
    void addDerivation() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        assertEquals("1 + 0", e.derivation("x").toString());
    }

    @Test
    void subDerivation() {
        Expression e = new Sub(new Variable("x"), new Variable("y"));
        assertEquals("1 - 0", e.derivation("x").toString());
    }

    @Test
    void mulDerivation() {
        Expression e = new Mul(new Variable("x"), new Variable("y"));
        // (x*y)' = x'*y + x*y' = 1*y + x*0
        Expression d = e.derivation("x");
        assertEquals(0, new Sub(d,
                new Add(new Mul(new Number(1), new Variable("y")),
                        new Mul(new Variable("x"), new Number(0))))
                .eval("x = 5; y = 7"));
    }

    @Test
    void divDerivation() {
        // (x/x)' = (1*x - x*1) / (x*x) = 0
        Expression e = new Div(new Variable("x"), new Variable("x"));
        Expression d = e.derivation("x");
        assertEquals(0, d.eval("x = 5"));
    }

    // ---------- Binary operations: toString ----------

    @Test
    void binaryToString() {
        Expression e = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));
        assertEquals("3 + 2 * x", e.toString());
    }

    @Test
    void mulToStringWithAddInside() {
        Expression e = new Mul(new Add(new Number(1), new Number(2)), new Number(3));
        assertEquals("(1 + 2) * 3", e.toString());
    }

    @Test
    void divToStringWithMulRight() {
        Expression e = new Div(new Number(1), new Mul(new Number(2), new Number(3)));
        assertEquals("1 / (2 * 3)", e.toString());
    }

    @Test
    void divToStringWithMulLeft() {
        Expression e = new Div(new Mul(new Number(2), new Number(3)), new Number(6));
        assertEquals("2 * 3 / 6", e.toString());
    }

    // ---------- Binary operations: getPrioritet ----------

    @Test
    void priorities() {
        assertEquals(PriorityOper.ADD_SUB.getPr(),
                new Add(new Number(1), new Number(2)).getPriority());
        assertEquals(PriorityOper.ADD_SUB.getPr(),
                new Sub(new Number(1), new Number(2)).getPriority());
        assertEquals(PriorityOper.MUL_DIV.getPr(),
                new Mul(new Number(1), new Number(2)).getPriority());
        assertEquals(PriorityOper.MUL_DIV.getPr(),
                new Div(new Number(1), new Number(2)).getPriority());
    }

    // ---------- Add.simplify ----------

    @Test
    void addSimplifyZeroLeft() {
        assertEquals(new Variable("x"),
                new Add(new Number(0), new Variable("x")).simplify());
    }

    @Test
    void addSimplifyZeroRight() {
        assertEquals(new Variable("x"),
                new Add(new Variable("x"), new Number(0)).simplify());
    }

    @Test
    void addSimplifyConstants() {
        assertEquals(new Number(5),
                new Add(new Number(2), new Number(3)).simplify());
    }

    @Test
    void addSimplifyEqual() {
        Expression e = new Add(new Variable("x"), new Variable("x")).simplify();
        assertEquals(new Mul(new Number(2), new Variable("x")), e);
    }

    @Test
    void addSimplifyUnarMinus() {
        Expression e = new Add(new Variable("x"),
                new UnarMinus(new Variable("y"))).simplify();
        assertEquals(new Sub(new Variable("x"), new Variable("y")), e);
    }

    @Test
    void addSimplifyNoChange() {
        Expression e = new Add(new Variable("x"), new Variable("y")).simplify();
        assertEquals("x + y", e.toString());
    }

    // ---------- Sub.simplify ----------

    @Test
    void subSimplifyEqual() {
        assertEquals(new Number(0),
                new Sub(new Variable("x"), new Variable("x")).simplify());
    }

    @Test
    void subSimplifyZeroRight() {
        assertEquals(new Variable("x"),
                new Sub(new Variable("x"), new Number(0)).simplify());
    }

    @Test
    void subSimplifyConstants() {
        assertEquals(new Number(1),
                new Sub(new Number(5), new Number(4)).simplify());
    }

    @Test
    void subSimplifyUnarMinus() {
        Expression e = new Sub(new Variable("x"),
                new UnarMinus(new Variable("y"))).simplify();
        assertEquals(new Add(new Variable("x"), new Variable("y")), e);
    }

    @Test
    void subSimplifyNoChange() {
        Expression e = new Sub(new Variable("x"), new Variable("y")).simplify();
        assertEquals("x - y", e.toString());
    }

    // ---------- Mul.simplify ----------

    @Test
    void mulSimplifyZeroLeft() {
        assertEquals(new Number(0),
                new Mul(new Number(0), new Variable("x")).simplify());
    }

    @Test
    void mulSimplifyZeroRight() {
        assertEquals(new Number(0),
                new Mul(new Variable("x"), new Number(0)).simplify());
    }

    @Test
    void mulSimplifyOneLeft() {
        assertEquals(new Variable("x"),
                new Mul(new Number(1), new Variable("x")).simplify());
    }

    @Test
    void mulSimplifyOneRight() {
        assertEquals(new Variable("x"),
                new Mul(new Variable("x"), new Number(1)).simplify());
    }

    @Test
    void mulSimplifyConstants() {
        assertEquals(new Number(6),
                new Mul(new Number(2), new Number(3)).simplify());
    }

    @Test
    void mulSimplifyNoChange() {
        assertEquals("x * y",
                new Mul(new Variable("x"), new Variable("y")).simplify().toString());
    }

    // ---------- Div.simplify ----------

    @Test
    void divSimplifyByZero() {
        assertThrows(ArithmeticException.class,
                () -> new Div(new Variable("x"), new Number(0)).simplify());
    }

    @Test
    void divSimplifyByZeroComputed() {
        // (x / (2-2)) — знаменатель должен стать 0 и бросить исключение
        assertThrows(ArithmeticException.class,
                () -> new Div(new Variable("x"),
                        new Sub(new Number(2), new Number(2))).simplify());
    }

    @Test
    void divSimplifyZeroLeft() {
        assertEquals(new Number(0),
                new Div(new Number(0), new Variable("x")).simplify());
    }

    @Test
    void divSimplifyOneRight() {
        assertEquals(new Variable("x"),
                new Div(new Variable("x"), new Number(1)).simplify());
    }

    @Test
    void divSimplifyConstants() {
        assertEquals(new Number(2),
                new Div(new Number(6), new Number(3)).simplify());
    }

    @Test
    void divSimplifyNoChange() {
        assertEquals("x / y",
                new Div(new Variable("x"), new Variable("y")).simplify().toString());
    }

    // ---------- UnarMinus ----------

    @Test
    void unarMinusEval() {
        assertEquals(-5, new UnarMinus(new Number(5)).eval(""));
        assertEquals(-10, new UnarMinus(new Variable("x")).eval("x = 10"));
    }

    @Test
    void unarMinusDerivation() {
        Expression e = new UnarMinus(new Variable("x")).derivation("x");
        assertEquals("-1", e.toString());
    }

    @Test
    void unarMinusSimplifyDouble() {
        Expression e = new UnarMinus(new UnarMinus(new Variable("x"))).simplify();
        assertEquals(new Variable("x"), e);
    }

    @Test
    void unarMinusSimplifyNumber() {
        assertEquals(new Number(-5),
                new UnarMinus(new Number(5)).simplify());
    }

    @Test
    void unarMinusSimplifyNoChange() {
        assertEquals("-x",
                new UnarMinus(new Variable("x")).simplify().toString());
    }

    @Test
    void unarMinusEqualsAndHashCode() {
        assertEquals(new UnarMinus(new Variable("x")),
                new UnarMinus(new Variable("x")));
        assertEquals(new UnarMinus(new Variable("x")).hashCode(),
                new UnarMinus(new Variable("x")).hashCode());
        assertNotEquals(new UnarMinus(new Variable("x")),
                new UnarMinus(new Variable("y")));
    }

    @Test
    void unarMinusToStringWithAddInside() {
        Expression e = new UnarMinus(new Add(new Variable("x"), new Variable("y")));
        assertEquals("-(x + y)", e.toString());
    }

    @Test
    void unarMinusToStringWithVar() {
        assertEquals("-x", new UnarMinus(new Variable("x")).toString());
    }

    @Test
    void unarMinusPrioritet() {
        assertEquals(PriorityOper.UNAR_MINUS.getPr(),
                new UnarMinus(new Variable("x")).getPriority());
    }

    // ---------- Parser: базовые случаи ----------

    @Test
    void parseNumber() {
        assertEquals("5", parse.parseExpression("5").toString());
    }

    @Test
    void parseVariable() {
        assertEquals("x", parse.parseExpression("x").toString());
    }

    @Test
    void parseMultiLetterVariable() {
        assertEquals("abc", parse.parseExpression("abc").toString());
    }

    @Test
    void parseVariableWithDigits() {
        assertEquals("x1", parse.parseExpression("x1").toString());
    }

    @Test
    void parseAdd() {
        assertEquals("1 + 2", parse.parseExpression("1+2").toString());
    }

    @Test
    void parseSub() {
        assertEquals("1 - 2", parse.parseExpression("1-2").toString());
    }

    @Test
    void parseMul() {
        assertEquals("1 * 2", parse.parseExpression("1*2").toString());
    }

    @Test
    void parseDiv() {
        assertEquals("1 / 2", parse.parseExpression("1/2").toString());
    }

    // ---------- Parser: приоритеты ----------

    @Test
    void parsePriorityMulBeforeAdd() {
        assertEquals("1 + 2 * 3", parse.parseExpression("1+2*3").toString());
    }

    @Test
    void parsePriorityDivBeforeSub() {
        assertEquals("1 - 2 / 3", parse.parseExpression("1-2/3").toString());
    }

    @Test
    void parseLeftAssociativitySub() {
        // 1-2-3 = (1-2)-3
        Expression e = parse.parseExpression("1-2-3");
        assertEquals(-4, e.eval(""));
    }

    @Test
    void parseLeftAssociativityDiv() {
        // 8/4/2 = (8/4)/2
        Expression e = parse.parseExpression("8/4/2");
        assertEquals(1, e.eval(""));
    }

    // ---------- Parser: скобки ----------

    @Test
    void parseParentheses() {
        assertEquals("(1 + 2) * 3", parse.parseExpression("(1+2)*3").toString());
    }

    @Test
    void parseNestedParentheses() {
        assertEquals("1", parse.parseExpression("((1))").toString());
    }

    @Test
    void parseParenthesesChangePriority() {
        assertEquals(9, parse.parseExpression("(1+2)*3").eval(""));
    }

    // ---------- Parser: унарный минус ----------

    @Test
    void parseUnaryMinusVariable() {
        assertEquals("-x", parse.parseExpression("-x").toString());
    }

    @Test
    void parseUnaryMinusAfterOperator() {
        assertEquals("2 * -3", parse.parseExpression("2*-3").toString());
        assertEquals(-6, parse.parseExpression("2*-3").eval(""));
    }

    @Test
    void parseUnaryMinusWithParentheses() {
        assertEquals(-6, parse.parseExpression("-(1+2)*2").eval(""));
    }

    // ---------- Parser: пробелы ----------

    @Test
    void parseWithSpaces() {
        assertEquals(7, parse.parseExpression(" 1 + 2 * 3 ").eval(""));
    }

    @Test
    void parseWithTabs() {
        assertEquals(7, parse.parseExpression("1\t+\t2\t*\t3").eval(""));
    }

    @Test
    void parseWithSpacesInParentheses() {
        assertEquals("(1 + 2) * 3", parse.parseExpression("( 1 + 2 ) * 3").toString());
    }

    // ---------- Parser: ошибки ----------

    @Test
    void parseEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> parse.parseExpression(""));
    }

    @Test
    void parseMissingClosingParen() {
        assertThrows(IllegalArgumentException.class,
                () -> parse.parseExpression("(1+2"));
    }

    @Test
    void parseInvalidChar() {
        assertThrows(IllegalArgumentException.class,
                () -> parse.parseExpression("1@2"));
    }

    // ---------- Expression.eval(String) ----------

    @Test
    void evalWithEmptyString() {
        assertEquals(5, new Number(5).eval(""));
        assertEquals(5, new Number(5).eval((String) null));
    }

    @Test
    void evalWithMultipleVars() {
        Expression e = new Add(new Variable("x"), new Variable("y"));
        assertEquals(23, e.eval("x = 10; y = 13"));
    }

    @Test
    void evalWithSpacesAroundEquals() {
        Expression e = new Variable("x");
        assertEquals(5, e.eval("  x  =  5  "));
    }

    // ---------- PrioritetOper ----------

    @Test
    void prioritetOperValues() {
        assertEquals(1, PriorityOper.ADD_SUB.getPr());
        assertEquals(2, PriorityOper.MUL_DIV.getPr());
        assertEquals(3, PriorityOper.UNAR_MINUS.getPr());
        assertEquals(4, PriorityOper.VAR_NUM.getPr());
    }

    // ---------- Комплексные сценарии ----------

    @Test
    void complexExpression() {
        Expression e = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));
        assertEquals(23, e.eval("x = 10"));
        assertEquals("3 + 2 * x", e.toString());
    }

    @Test
    void complexSimplify() {
        // (2+3)*x + 0 → 5*x
        Expression e = new Add(
                new Mul(
                        new Add(new Number(2), new Number(3)),
                        new Variable("x")),
                new Number(0));
        assertEquals("5 * x", e.simplify().toString());
    }

    @Test
    void simplifyDoesNotMutateOriginal() {
        Expression orig = new Add(new Number(2), new Number(3));
        Expression simplified = orig.simplify();
        assertEquals("2 + 3", orig.toString());
        assertEquals("5", simplified.toString());
    }

    @Test
    void equalityOnComplexTrees() {
        Expression a = new Add(new Number(1),
                new Mul(new Variable("x"), new Number(2)));
        Expression b = new Add(new Number(1),
                new Mul(new Variable("x"), new Number(2)));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}