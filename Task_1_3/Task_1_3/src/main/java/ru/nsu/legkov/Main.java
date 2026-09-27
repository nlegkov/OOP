package ru.nsu.legkov;

public class Main {
    public static void main(String[] args) {
        Expression e1 = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );
        System.out.print("Выражение e1: ");
        e1.print(); // Ожидается: (3+(2*x))
        System.out.println();

        // 3. Дифференцирование по "x"
        Expression de1 = e1.derivation("x");
        System.out.print("Производная de1 (по x): ");
        de1.print(); // Ожидается: (0+((0*x)+(2*1)))
        System.out.println();

        // 4. Вычисление значения
        int res1 = e1.eval("x = 10; y = 13");
        System.out.println("e1.eval(\"x = 10; y = 13\"): " + res1); // Ожидается: 23
        System.out.println();


        System.out.println("=== 2. Проверка дифференцирования по другой переменной ===");
        // Проверяем, что dy/dx дает 0 для переменной y
        Expression e2 = new Add(new Variable("x"), new Variable("y"));
        System.out.print("Выражение e2: ");
        e2.print(); // (x+y)
        System.out.println();

        Expression de2_dx = e2.derivation("x");
        System.out.print("Производная e2 по x: ");
        de2_dx.print(); // (1+0)
        System.out.println();


        System.out.println("\n=== 3. Проверка парсера из строки ===");
        // Парсим строку с многобуквенной переменной и всеми операторами
        String inputStr = "((var1*10)-(x/2))";
        Expression parsedExpr = parseExpression(inputStr);

        System.out.print("Спарсенное выражение: ");
        parsedExpr.print(); // Ожидается: ((var1*10)-(x/2))
        System.out.println();

        // Вычисление спарсенного выражения
        int resParsed = parsedExpr.eval("var1 = 5; x = 4");
        System.out.println("Результат eval (var1=5, x=4): " + resParsed); // (5*10) - (4/2) = 50 - 2 = 48

        // Дифференцирование спарсенного выражения по var1
        Expression dParsed = parsedExpr.derivation("var1");
        System.out.print("Производная по var1: ");
        dParsed.print(); // (((1*10)+(var1*0))-(((0*2)-(x*0))/(2*2)))
        System.out.println();
    }
}

