package ru.tselikov.parser.service.impl;

import ru.tselikov.parser.model.Laptop;
import ru.tselikov.parser.service.DataBaseService;
import ru.yandex.clickhouse.response.ClickHouseResultSet;
import org.springframework.stereotype.Service;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHousePreparedStatement;

import java.sql.SQLException;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для осуществления взаимодействия с БД.
 */
@Service
public class ClickHouseService implements DataBaseService {

    private final DataSource clickHouseDataSource;

    public ClickHouseService(DataSource clickHouseDataSource) {
        this.clickHouseDataSource = clickHouseDataSource;
    }

    /**
     * Метод для вставки значений в БД.
     *
     * @param laptops список ноутбуков.
     * @throws SQLException ошибка в БД.
     */
    @Override
    public void insertData(List<Laptop> laptops) throws SQLException {
        executeQuery("INSERT INTO laptop (date ,name, processor, screen_diagonal, price, link) VALUES (?, ?, ?, ?, ?, ?)", laptops);
    }

    /**
     * Метод для выбора всех значений из БД.
     *
     * @throws SQLException ошибка в БД.
     */
    @Override
    public List<Laptop> selectAllData() throws SQLException {
        return executeQuery("SELECT * FROM laptop");
    }

    /**
     * Метод для выполнения запроса с параметрами.
     *
     * @param sql     запрос.
     * @param laptops список ноутбуков.
     * @throws SQLException ошибка в БД.
     */
    private void executeQuery(String sql, List<Laptop> laptops) throws SQLException {
        try (ClickHouseConnection connection = (ClickHouseConnection) clickHouseDataSource.getConnection();
             ClickHousePreparedStatement statement = (ClickHousePreparedStatement) connection.prepareStatement(sql)) {
            for (Laptop laptop : laptops) {
                statement.setDate(1, java.sql.Date.valueOf(laptop.getDate()));
                statement.setString(2, laptop.getName());
                statement.setString(3, laptop.getProcessor());
                statement.setFloat(4, laptop.getScreenDiagonal());
                statement.setInt(5, laptop.getPrice());
                statement.setString(6, laptop.getLink());

                statement.executeUpdate();
            }
        }
    }

    /**
     * Метод для выполнения запроса без параметров.
     *
     * @param sql запрос.
     * @throws SQLException ошибка в БД.
     */
    private List<Laptop> executeQuery(String sql) throws SQLException {
        List<Laptop> laptops = new ArrayList<>();
        try (ClickHouseConnection connection = (ClickHouseConnection) clickHouseDataSource.getConnection();
             ClickHousePreparedStatement statement = (ClickHousePreparedStatement) connection.prepareStatement(sql);
             ClickHouseResultSet resultSet = (ClickHouseResultSet) statement.executeQuery()) {

            while (resultSet.next()) {
                Laptop laptop = new Laptop(resultSet);
                laptops.add(laptop);
                System.out.println(laptop);
            }
        }
        return laptops;
    }
}

