package ru.tselikov.parser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tselikov.parser.service.impl.ClickHouseService;
import ru.tselikov.parser.service.DataBaseService;
import ru.tselikov.parser.service.LaptopParseService;

import java.sql.SQLException;

@RestController
public class AppController {
    private final LaptopParseService laptopParseService;
    private final DataBaseService clickHouseService;

    public AppController(LaptopParseService laptopParseService, ClickHouseService clickHouseService) {
        this.laptopParseService = laptopParseService;
        this.clickHouseService = clickHouseService;
    }

    /**
     * Endpoint запуска парсера.
     *
     * @return результат парсинга.
     * @throws SQLException ошибка БД.
     */
    @GetMapping("/parse")
    public ResponseEntity<?> parse() throws SQLException {
        return ResponseEntity.ok(laptopParseService.parseLaptops());
    }

    /**
     * Endpoint вывода всех значений.
     *
     * @return значения.
     * @throws SQLException ошибка БД.
     */
    @GetMapping("/all")
    public ResponseEntity<?> all() throws SQLException {
        return ResponseEntity.ok(clickHouseService.selectAllData());
    }
}
