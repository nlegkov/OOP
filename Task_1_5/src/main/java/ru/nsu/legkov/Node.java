package ru.nsu.legkov;

/**
 * класс Node представляет собой узел в хеш-таблице.
 *
 * @param <K> параметр типа ключа
 * @param <V> аргумент типа значения
 */
public class Node<K, V> {
    K key;
    V value;
    Node<K, V> next;

    Node(K key, V value, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
}
