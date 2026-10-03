package ru.nsu.legkov;

/** Рекурсивный парсер выражений с приоритетами. */
public class ParseExpression {
    public String str;
    int i = 0;

    /**
     * @param str исходное выражение
     * @return корень дерева разбора
     * @throws IllegalArgumentException при синтаксической ошибке
     */
    public Expression parseExpression(String str) {
        this.str = str.trim();
        this.i = 0;
        Expression e = parseStart();

        skipSpaces();

        return e;
    }

    public void skipSpaces() {
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }
    }

    public Expression parseStart() {
        return parse(0);
    }

    private Expression parse(int prior) {
        Expression l = parsePrimary();

        while (true) {
            skipSpaces();

            if (i >= str.length()) {
                return l;
            }

            char c = str.charAt(i);
            int opPr = priority(c);

            if (opPr < prior) {
                return l;
            }

            i++;
            Expression r = parse(opPr + 1);
            l = makeBinary(c, l, r);
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

    private Expression makeBinary(char op, Expression l, Expression r) {
        return switch (op) {
            case '+' -> new Add(l, r);
            case '-' -> new Sub(l, r);
            case '*' -> new Mul(l, r);
            case '/' -> new Div(l, r);
            default -> throw new IllegalStateException("unk op: " + op);
        };
    }

    private Expression parsePrimary() {
        skipSpaces();

        if (i >= str.length()) {
            throw new IllegalArgumentException("end of exp");
        }

        char c = str.charAt(i);

        if (c == '(') {
            i++;
            Expression e = parse(0);
            skipSpaces();
            if (i >= str.length() || str.charAt(i) != ')') {
                throw new IllegalArgumentException("not ) at pos " + i);
            }
            i++;
            return e;
        }

        if (c == '-') {
            i++;
            return new UnarMinus(parsePrimary());
        }

        if (Character.isDigit(c)) {
            int start = i;
            while (i < str.length() && Character.isDigit(str.charAt(i))) {
                i++;
            }
            return new Number(Integer.parseInt(str.substring(start, i)));
        }

        if (Character.isLetter(c)) {
            int start = i;
            while (i < str.length() && Character.isLetterOrDigit(str.charAt(i))) {
                i++;
            }
            return new Variable(str.substring(start, i));
        }

        throw new IllegalArgumentException("what char " + c + " at pos " + i);
    }


}


// 1+1+2*2