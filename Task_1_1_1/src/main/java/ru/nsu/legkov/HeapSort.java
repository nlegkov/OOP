package ru.nsu.legkov;

import java.util.Arrays;

/**
 * Класс реализующий алгоритм пирамидальной сортировки, или простр куча.
 *
 * @author Legkov
 */

public class HeapSort {
    /**
     * Сортирует данный массив с помощью пирамидальной сортировки.
     * Сложность по времени: O(n log(n)).
     * Сложность по памяти: О(1).
     *
     * @param array исходный массив целых чисел
     * @return возвращает тот же массив, но в отсортированном по возростанию виде
     */
    public static int[] heapsort(int[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }

        int n = array.length;

        //преобразуем массив в heap max
        for (int i = n / 2 - 1; i >= 0; i--) {
            heap(array, n, i);
        }

        //ставим максимум на последнюю позицию и перепросеиваем оставшуюся кучу
        for (int i = n-1; i > 0; i--) {
            int t = array[0];
            array[0] = array[i];
            array[i] = t;

            heap(array, i, 0);
        }

        return array;
    }

    /**
     * Преобразует поддерево с корнем в узле i в max heap.
     *
     * @param array массив представляющий кучу
     * @param n размер кучи
     * @param i индекс коренвого узла поддерева
     */
    public static void heap(int [] array, int n, int i) {
        int x = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[x]) {
            x = left;
        }
        if (right < n && array[right] > array[x]) {
            x = right;
        }
        // если у нас есть лист который больше родителя то меняем их местами и просеиваем
        if (x != i) {
            int t = array[i];
            array[i] = array[x];
            array[x] = t;

            heap(array, n, x);
        }
    }

    /**
     * Точка входа для демонстраци работы.
     *
     * @param args аргументы cmd
     */
    public static void main(String[] args) {
        int[] input = new int[]{5, 4, 3, 2, 1};
        System.out.println("Input " + Arrays.toString(input));
        heapsort(input);
        System.out.println("Output " + Arrays.toString(input));
    }
}
