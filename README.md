# Приложение-парсер ноутбуков с сайта xcom-shop.ru

## Описание
Приложение осуществляет парсинг развдела ноутбуков(https://www.xcom-shop.ru/catalog/kompyutery_i_noytbyki/noytbyki/),
а именно название ноутбука, название и модель процессора, размер диагонали экрана, цену и ссылку на товар

## Стек
- Java 11
- Maven
- Spring Boot
- Jsoup
- ClickHouse
- Docker

## Запуск
``docker-compose up -d`` - для запуска базы данных

``./mvnw spring-boot:run  `` - для запуска приложения

## Использование
- Для запуска парсинга:
`GET:` `localhost:8080/parse`

Response Body:
```json
[
    {
      "date": "2023-06-07",
      "name": "HP 15s-fq2003ny 488J2EA",
      "processor": "Intel Core i3",
      "screenDiagonal": 15.6,
      "price": 33134,
      "link": "https://www.xcom-shop.ru/hp_15s-fq2003ny_970035.html"
    },
    {
        "date": "2023-06-07",
        "name": "HIPER DZEN YB97KDOK",
        "processor": "Intel Core i3",
        "screenDiagonal": 15.6,
        "price": 30000,
        "link": "https://www.xcom-shop.ru/hiper_dzen_984932.html"
    }
]
```
- Для выбора всех значений из базы данных:
`GET:` `localhost:8080/all`

Response Body:
```json
[
    {
      "date": "2023-06-07",
      "name": "HP 15s-fq2003ny 488J2EA",
      "processor": "Intel Core i3",
      "screenDiagonal": 15.6,
      "price": 33134,
      "link": "https://www.xcom-shop.ru/hp_15s-fq2003ny_970035.html"
    },
    {
        "date": "2023-06-07",
        "name": "HIPER DZEN YB97KDOK",
        "processor": "Intel Core i3",
        "screenDiagonal": 15.6,
        "price": 30000,
        "link": "https://www.xcom-shop.ru/hiper_dzen_984932.html"
    }
]
```
