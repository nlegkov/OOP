package ru.nsu.legkov;

import java.util.HashMap;
import java.util.Map;

/**
 * Рекурсивный парсер выражений с приоритетами.
 */
public class ParseExpression {

    private String inputString;
    private int currentIndex = 0;

    /**
     * Разбирает строку в математическое выражение.
     *
     * @param inputStr исходное выражение
     * @return корень дерева разбора
     * @throws IllegalArgumentException при синтаксической ошибке
     */
    public Expression parseExpression(String inputStr) {
        this.inputString = inputStr.trim();
        this.currentIndex = 0;

        skipSpaces();
        Expression expr = parseStart();
        skipSpaces();

        return expr;
    }

    /**
     * Пропускает пробельные символы во входной строке.
     */
    public void skipSpaces() {
        while (currentIndex < inputString.length()
                && Character.isWhitespace(inputString.charAt(currentIndex))) {
            currentIndex++;
        }
    }

    /**
     * Начинает разбор выражения с минимальным приоритетом.
     *
     * @return разобранное выражение
     */
    public Expression parseStart() {
        return parse(0);
    }

    private Expression parse(int prior) {
        Expression left = parsePrimary();

        while (true) {
            skipSpaces();

            if (currentIndex >= inputString.length()) {
                return left;
            }

            char c = inputString.charAt(currentIndex);
            int opPr = priority(c);

            if (opPr < prior) {
                return left;
            }

            currentIndex++;
            Expression right = parse(opPr + 1);
            left = makeBinary(c, left, right);
        }
    }

    private int priority(char c) {
        if (c == '+' || c == '-') {
            return 1;
        }
        if (c == '*' || c == '/') {
            return 2;
        }
        return -1;
    }

    private Expression makeBinary(char op, Expression left, Expression right) {
        return switch (op) {
            case '+' -> new Add(left, right);
            case '-' -> new Sub(left, right);
            case '*' -> new Mul(left, right);
            case '/' -> new Div(left, right);
            default -> throw new IllegalStateException("unk op: " + op);
        };
    }

    private Expression parsePrimary() {
        skipSpaces();

        if (currentIndex >= inputString.length()) {
            throw new IllegalArgumentException("end of exp");
        }

        char c = inputString.charAt(currentIndex);

        if (c == '(') {
            currentIndex++;
            Expression expr = parse(0);
            skipSpaces();
            if (currentIndex >= inputString.length() || inputString.charAt(currentIndex) != ')') {
                throw new IllegalArgumentException("not ) at pos " + currentIndex);
            }
            currentIndex++;
            return expr;
        }

        if (c == '-') {
            currentIndex++;
            return new UnarMinus(parsePrimary());
        }

        if (Character.isDigit(c)) {
            int start = currentIndex;
            while (currentIndex < inputString.length()
                    && Character.isDigit(inputString.charAt(currentIndex))) {
                currentIndex++;
            }
            return new Number(Integer.parseInt(inputString.substring(start, currentIndex)));
        }

        if (Character.isLetter(c)) {
            int start = currentIndex;
            while (currentIndex < inputString.length()
                    && Character.isLetterOrDigit(inputString.charAt(currentIndex))) {
                currentIndex++;
            }
            return new Variable(inputString.substring(start, currentIndex));
        }

        throw new IllegalArgumentException("what char " + c + " at pos " + currentIndex);
    }

    /**
     * Парсит строку означивания переменных в Map.
     *
     * @param str строка вида "x = 10; y = 13"
     * @return карта с именами и значениями переменных
     */
    public static Map<String, Integer> parserStrMap(String str) {
        Map<String, Integer> map = new HashMap<>();
        if (str == null || str.trim().isEmpty()) {
            return map;
        }

        String varPattern = "^[a-zA-Z_][a-zA-Z0-9_]*$";

        String[] parts = str.split(";");
        for (String part : parts) {
            String[] s = part.split("=");
            if (s.length != 2) {
                throw new IllegalArgumentException("Invalid format for pair");
            }

            String key = s[0].trim();
            String valueStr = s[1].trim();

            if (!key.matches(varPattern)) {
                throw new IllegalArgumentException("Invalid variable name");
            }

            try {
                int value = Integer.parseInt(valueStr);
                map.put(key, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer value");
            }
        }

        return map;
    }
}