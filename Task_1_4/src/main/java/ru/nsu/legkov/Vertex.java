package ru.nsu.legkov;

/**
 * Вершина ориентированного графа с идентификатором и данными.
 */
public class Vertex<T> {
    private final T data;
    private final int id;

    /**
     * Создаёт вершину графа.
     *
     * @param id идентификатор вершины
     * @param data данные вершины
     * @return результат операции
     */
    public Vertex(int id, T data) {
        this.id = id;
        this.data = data;
    }

    /**
     * Возвращает идентификатор вершины.
     * @return целочисленное значение
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает данные вершины.
     * @return данные вершины
     */
    public T getData() {
        return data;
    }

    /**
     * Проверяет равенство вершин по идентификатору.
     *
     * @param o объект для сравнения
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vertex<?> vertex = (Vertex<?>) o;
        return id == vertex.id;
    }

    /**
     * Вычисляет хеш-код идентификатора вершины.
     * @return целочисленное значение
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Возвращает идентификатор и данные вершины в строковом виде.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return id + ";" + data;
    }
}
