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
    @DisplayName("Отсортированный массив")
    void testAlreadySorted() {
        int[] input = new int[]{1, 2, 3, 4, 5, 6};
        int[] expected = new int[]{1, 2, 3, 4, 5, 6};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }

    @Test
    @DisplayName("Массив отсортированный в обратном порядке")
    void testReverseSorted() {
        int[] input = new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertArrayEquals(expected, HeapSort.heapsort(input));
    }
}