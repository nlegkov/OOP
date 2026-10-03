package ru.nsu.legkov;

/** Ручной прогон парсера и simplify. */
public class Main {

    /**
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        String[] tests = {
                "1+1+2*2",
                "1 + 2 * 3",
                "(1+2)*3",
                "2*-3",
                "-x+5",
                "((1))",
                "1-2-3",
                "8/4/2",
                "x*y+z",
                "(a+b)*(c-d)",
                "1 + 1 + x - -( 4 / 2)",
                "1+3-1-3+x",
                "1+1"
        };

        ParseExpression parser = new ParseExpression();

        for (String s : tests) {
            try {
                Expression e = parser.parseExpression(s);
                System.out.println(s + " -> " + e + " -> " + e.simplify());
            } catch (Exception ex) {
                System.out.printf(ex.getMessage());
            }
        }
    }
}