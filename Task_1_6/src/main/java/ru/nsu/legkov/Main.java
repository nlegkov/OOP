package ru.nsu.legkov;

import java.io.IOException;

/** Точка входа для поиска в файлах UTF-8 из командной строки. */
public final class Main {
    private Main() {
    }

    /**
     * Выводит позиции по мере обнаружения вхождений без сохранения списка результатов.
     *
     * @param args путь к файлу и непустая искомая подстрока
     * @throws IOException при ошибке чтения или декодирования UTF-8
     * @throws IllegalArgumentException если переданы некорректные аргументы
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2 || args[1].isEmpty()) {
            throw new IllegalArgumentException("Укажи файл и непустую подстроку");
        }

        String fileName = args[0];
        String substring = args[1];

        System.out.print("[");
        boolean[] first = {true};
        Find.find(fileName, substring, position -> {
            if (!first[0]) {
                System.out.print(", ");
            }
            System.out.print(position);
            first[0] = false;
        });
        System.out.println("]");
    }
}
