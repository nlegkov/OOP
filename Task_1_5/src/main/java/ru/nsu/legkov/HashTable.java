package ru.nsu.legkov;

import java.util.AbstractMap;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Класс HashTable представляет собой реализацию хеш-таблицы.
 *
 * @param <K> параметр типа ключа
 * @param <V> аргумент типа значения
 */
public class HashTable<K, V> implements Iterable<Map.Entry<K, V>> {
    private Node<K, V>[] table;
    private int modCount = 0;
    private int size = 0;

    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashTable() {
        table = (Node<K, V>[]) new Node[16];
    }

    /**
     * Вычисляет хеш-код для заданного ключа.
     *
     * @param key ключ, для которого вычисляется хеш-код
     * @return хеш-код ключа
     */
    private int hash(K key) {
        return hash(key, table.length);
    }

    /**
     * Вычисляет хеш-код для заданного ключа с учетом емкости таблицы.
     *
     * @param key      ключ, для которого вычисляется хеш-код
     * @param capacity емкость таблицы
     * @return хеш-код ключа
     */
    private int hash(K key, int capacity) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        return h & (capacity - 1);
    }

    /**
     * Добавляет пару ключ-значение в хеш-таблицу.
     *
     * @param key   ключ, который нужно добавить
     * @param value значение, которое нужно добавить
     * @return true, если пара успешно добавлена, false, если ключ уже существует
     */
    public boolean put(K key, V value) {
        int hash = hash(key);

        if (table[hash] == null) {
            Node<K, V> node = new Node<K, V>(key, value, null);
            table[hash] = node;
            modCount++;
            size++;

            if (size > table.length * LOAD_FACTOR) {
                resize(table.length * 2);
            }

            return true;
        }

        Node<K, V> current = table[hash];

        while (true) {
            if (Objects.equals(current.key, key)) {
                return false;
            }

            if (current.next != null) {
                current = current.next;
            } else {
                break;
            }
        }

        current.next = new Node<K, V>(key, value, null);
        modCount++;
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize(table.length * 2);
        }

        return true;
    }

    /**
     * Удаляет пару ключ-значение из хеш-таблицы по заданному ключу.
     *
     * @param key ключ, который нужно удалить
     * @return true, если пара успешно удалена, false, если ключ не найден
     */
    public boolean remove(K key) {
        int hash = hash(key);

        Node<K, V> current = table[hash];
        Node<K, V> tmp = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (tmp == null) {
                    table[hash] = current.next;
                }
                else {
                    tmp.next = current.next;
                }
                modCount++;
                size--;

                if (size < table.length * 0.25 && table.length > 16) {
                    resize(table.length / 2);
                }

                return true;
            }

            tmp = current;
            current = current.next;
        }

        return false;
    }

    /**
     * Получает значение по заданному ключу из хеш-таблицы.
     *
     * @param key ключ, для которого нужно получить значение
     * @return значение, связанное с ключом, или null, если ключ не найден
     */
    public V get(K key) {
        int hash = hash(key);

        Node<K, V> current = table[hash];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Обновляет значение по заданному ключу в хеш-таблице.
     *
     * @param key   ключ, для которого нужно обновить значение
     * @param value новое значение, которое нужно установить
     * @return true, если значение успешно обновлено, false, если ключ не найден
     */
    public boolean update(K key, V value) {
        int hash = hash(key);

        Node<K, V> current = table[hash];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Возвращает количество элементов в хеш-таблице.
     *
     * @return количество элементов в хеш-таблице
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Внутренний класс HashTableIterator реализует итератор для хеш-таблицы.
     */
    private class HashTableIterator
            implements Iterator<Map.Entry<K, V>> {

        private int index = 0;
        private Node<K, V> current;
        private final int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            findNext();
            return current != null;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            findNext();

            if (current == null) {
                throw new NoSuchElementException();
            }

            Node<K, V> result = current;
            current = current.next;

            return new AbstractMap.SimpleImmutableEntry<>(
                    result.key,
                    result.value
            );
        }

        private void findNext() {
            while (current == null && index < table.length) {
                current = table[index];
                index++;
            }
        }
    }

    /**
     * Проверяет, содержит ли хеш-таблица пару ключ-значение.
     *
     * @param key   ключ, который нужно проверить
     * @param value значение, которое нужно проверить
     * @return true, если пара ключ-значение существует в хеш-таблице, false в противном случае
     */
    private boolean containsEntry(Object key, Object value) {
        for (Map.Entry<K, V> entry : this) {
            if (Objects.equals(entry.getKey(), key)
                    && Objects.equals(entry.getValue(), value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Возвращает количество элементов в хеш-таблице.
     *
     * @return количество элементов в хеш-таблице
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof HashTable<?, ?> other)) {
            return false;
        }

        if (size != other.size) {
            return false;
        }

        for (Map.Entry<K, V> entry : this) {
            Object key = entry.getKey();

            if (key == null) {
                return false;
            }

            Node<?, ?> node = other.findNode(key);

            if (node == null) {
                return false;
            }

            if (!Objects.equals(entry.getValue(), node.value)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Вычисляет хеш-код хеш-таблицы.
     *
     * @return хеш-код хеш-таблицы
     */
    @Override
    public int hashCode() {
        int result = 0;

        for (Map.Entry<K, V> entry : this) {
            result += Objects.hashCode(entry.getKey())
                    ^ Objects.hashCode(entry.getValue());
        }

        return result;
    }

    /**
     * Изменяет размер хеш-таблицы.
     *
     * @param newCapacity новая емкость хеш-таблицы
     */
    private void resize(int newCapacity) {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int index = hash(node.key, newCapacity);
                Node<K, V> nextNode = node.next;

                node.next = newTable[index];
                newTable[index] = node;

                node = nextNode;
            }
        }

        table = newTable;
    }

    /**
     * Возвращает строковое представление хеш-таблицы.
     *
     * @return строковое представление хеш-таблицы
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (Map.Entry<K, V> entry : this) {
            if (sb.length() > 1) {
                sb.append(", ");
            }

            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * Находит узел по заданному ключу в хеш-таблице.
     *
     * @param key ключ, для которого нужно найти узел
     * @return узел, связанный с ключом, или null, если ключ не найден
     */
    private Node<K, V> findNode(Object key) {
        int h = key.hashCode();
        h ^= (h >>> 16);

        int index = h & (table.length - 1);

        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Проверяет, является ли хеш-таблица пустой.
     *
     * @return true, если хеш-таблица пустая, false в противном случае
     */
    public boolean isEmpty() {
        return size == 0;
    }
}

