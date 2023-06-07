package ru.tselikov.parser.service;

import ru.tselikov.parser.model.Laptop;

import java.sql.SQLException;
import java.util.List;

public interface DataBaseService {
    void insertData(List<Laptop> laptops) throws SQLException;

    List<Laptop> selectAllData() throws SQLException;
}
