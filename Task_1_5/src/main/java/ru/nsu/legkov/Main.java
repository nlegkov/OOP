package ru.nsu.legkov;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Integer> table = new HashTable<>();

        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        for (Map.Entry<String, Integer> entry : table) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
