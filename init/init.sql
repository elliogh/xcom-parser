-- Создание базы данных
CREATE DATABASE my_database;

-- Использование созданной базы данных
USE my_database;

-- Создание таблицы
CREATE TABLE laptop (
    date Date,
    name String,
    processor String,
    screen_diagonal Float32,
    price Int32,
    link String
) ENGINE = MergeTree()
ORDER BY price;
