package ru.nsu.legkov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    @Test
    @DisplayName("Проверка примера из задания")
    void testStandardExample() {
        int[] input = new int[]{5, 4, 3, 2, 1};
        int[] expected = new int[]{1, 2, 3, 4, 5};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    @DisplayName("Граничные случаи: null, пустой массив, 1 элемент")
    void testEdgeCases() {
        assertNull(HeapSort.heapsort(null));
        assertArrayEquals(new int[]{}, HeapSort.heapsort(new int[]{}));
        assertArrayEquals(new int[]{42}, HeapSort.heapsort(new int[]{42}));
    }

    @Test
    @DisplayName("Массив с дубликатами и отрицательными числами")
    void testDuplicatesAndNegativeNumbers() {
        int[] input = new int[]{-3, 5, 0, -3, 2, 5, -10};
        int[] expected = new int[]{-10, -3, -3, 0, 2, 5, 5};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    @DisplayName("Уже отсортированный массив")
    void testAlreadySorted() {
        int[] input = new int[]{1, 2, 3, 4, 5, 6};
        int[] expected = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    @DisplayName("Массив, отсортированный в обратном порядке")
    void testReverseSorted() {
        int[] input = new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    @DisplayName("Проверка вызова main()")
    void testMainMethod() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            HeapSort.main(new String[]{});
            String output = outContent.toString();
            assertTrue(output.contains("Input [5, 4, 3, 2, 1]"));
            assertTrue(output.contains("Output [1, 2, 3, 4, 5]"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Экспериментальное подтверждение сложности O(N log N)")
    void testEmpiricalComplexity() {
        Random rand = new Random(42);

        int size1 = 100_000;
        int size2 = 1_000_000; // Увеличение в 10 раз

        int[] arr1 = rand.ints(size1, -1_000_000, 1_000_000).toArray();
        int[] arr2 = rand.ints(size2, -1_000_000, 1_000_000).toArray();

        // Прогрев JIT
        for (int i = 0; i < 5; i++) {
            HeapSort.heapsort(rand.ints(10_000).toArray());
        }

        long start1 = System.nanoTime();
        HeapSort.heapsort(arr1);
        long time1 = System.nanoTime() - start1;

        long start2 = System.nanoTime();
        HeapSort.heapsort(arr2);
        long time2 = System.nanoTime() - start2;

        double timeRatio = (double) time2 / time1;
        // Теоретическое отношение: (N2 * log2(N2)) / (N1 * log2(N1))
        // (1_000_000 * 19.93) / (100_000 * 16.61) ≈ 12.0
        double theoreticalRatio = (size2 * (Math.log(size2) / Math.log(2))) /
                (size1 * (Math.log(size1) / Math.log(2)));

        // Время должно быть пропорционально O(N log N) (с допустимой погрешностью JIT/системы)
        assertTrue(timeRatio < theoreticalRatio * 2.5,
                String.format("Рост времени работы (%.2f) слишком сильно превышает теоретический (%.2f)", timeRatio, theoreticalRatio));
    }
}