package ru.tselikov.parser.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Сущность ноутбук.
 */
public class Laptop {
    private final LocalDate date;        // дата парсинга
    private final String name;           // название ноутбука
    private final String processor;      // название процессора
    private final float screenDiagonal;  // диагональ экрана
    private final int price;             // цена
    private final String link;           // ссылка на ноутбук

    public Laptop(LocalDate date, String name, String processor, float screenDiagonal, int price, String link) {
        this.date = date;
        this.name = name;
        this.processor = processor;
        this.screenDiagonal = screenDiagonal;
        this.price = price;
        this.link = link;
    }

    public Laptop(ResultSet resultSet) throws SQLException {
        this(
                resultSet.getDate(1).toLocalDate(),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getFloat(4),
                resultSet.getInt(5),
                resultSet.getString(6)
        );
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getProcessor() {
        return processor;
    }

    public float getScreenDiagonal() {
        return screenDiagonal;
    }

    public int getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", processor='" + processor + '\'' +
                ", screenDiagonal='" + screenDiagonal + '\'' +
                ", price='" + price + '\'' +
                ", link='" + link + '\'';
    }
}
