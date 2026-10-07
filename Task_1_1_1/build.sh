#!/usr/bin/env bash
set -e

# Очистка старой сборки
rm -rf out doc app.jar

echo "1. Компиляция исходного кода (javac)"
mkdir -p out
javac -d out src/main/java/ru/nsu/legkov/HeapSort.java

echo "2. Генерация Javadoc"
mkdir -p doc
javadoc -d doc -sourcepath src/main/java -subpackages ru.nsu.legkov -quiet

echo "3. Создание JAR-файла (jar)"
jar --create --file app.jar --main-class ru.nsu.legkov.HeapSort -C out .

echo "4. Запуск приложения (java)"
java -jar app.jar

echo "Сборка и запуск завершены успешно!"