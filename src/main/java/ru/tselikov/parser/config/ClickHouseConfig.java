package ru.tselikov.parser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.clickhouse.ClickHouseDataSource;

import javax.sql.DataSource;

@Configuration
public class ClickHouseConfig {

    @Bean
    public DataSource clickHouseDataSource() {
        String url = "jdbc:clickhouse://localhost:8123/my_database?user=default";
        return new ClickHouseDataSource(url);
    }
}

