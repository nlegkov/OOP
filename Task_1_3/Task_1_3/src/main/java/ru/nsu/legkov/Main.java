package ru.nsu.legkov;

public class Main {
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
        };

        ParseExpression parser = new ParseExpression();

        for (String s : tests) {
            try {
                Expression e = parser.parseExpression(s);
                System.out.println("\n" + s + " -> ");
                e.print();

            } catch (Exception ex) {
                System.out.printf("ERROR");
            }
        }
    }
}