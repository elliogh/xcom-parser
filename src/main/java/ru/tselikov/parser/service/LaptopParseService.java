package ru.tselikov.parser.service;

import ru.tselikov.parser.model.Laptop;

import java.sql.SQLException;
import java.util.List;

public interface LaptopParseService {
    List<Laptop> parseLaptops() throws SQLException;
}
