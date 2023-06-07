package ru.tselikov.parser.service.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tselikov.parser.model.Laptop;
import ru.tselikov.parser.service.LaptopParseService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для осуществления парсинга ноутбуков.
 */
@Service
public class LaptopParseServiceImpl implements LaptopParseService {
    @Value("${application.parsing.url}")
    private String URL; // url сайта для парсинга

    private final ClickHouseService clickHouseService;

    public LaptopParseServiceImpl(ClickHouseService clickHouseService) {
        this.clickHouseService = clickHouseService;
    }

    /**
     * Метод, который парсит ноутбуки и сохранят в БД.
     *
     * @return список ноутбуков.
     * @throws SQLException ошибка БД.
     */
    @Override
    public List<Laptop> parseLaptops() throws SQLException {
        List<Laptop> laptops = new ArrayList<>();
        Document doc;
        int page = 1;
        try {
            while (laptops.size() <= 100) {
                doc = Jsoup.connect(URL + "/catalog/kompyutery_i_noytbyki/noytbyki/?catalog=page-" + page).get();
                laptops.addAll(parseLaptops(doc));
                page++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        clickHouseService.insertData(laptops);

        return laptops;
    }

    /**
     * Метод, который парсит ноутбуки на одной странице.
     *
     * @param doc HTML документ.
     * @return список ноутбуков на одной странице.
     */
    private List<Laptop> parseLaptops(Document doc) {
        return doc.select(".catalog_item")
                .parallelStream()
                .map(this::parseLaptopElement)
                .collect(Collectors.toList());
    }

    /**
     * Метод, который достает информацию об одном ноутбуке.
     *
     * @param element элемент.
     * @return ноутбук с информацией.
     */
    private Laptop parseLaptopElement(Element element) {
        String link = element.selectFirst(".catalog_item__name").attr("href");

        Document doc;
        try {
            doc = Jsoup.connect(URL + link).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String name = doc.selectFirst(".card-bundle__title").text().replace("Ноутбук ", "");
        String priceString = doc.selectFirst(".card-bundle-basket__price").text();
        Elements listItems = doc.select("ul.product-block-description__list li.product-block-description__item");

        Map<String, String> fieldValueMap = listItems.stream()
                .collect(Collectors.toMap(
                        listItem -> listItem.selectFirst("div.product-block-description__first-elem").text().trim(),
                        listItem -> listItem.selectFirst("div.product-block-description__second-elem").text().trim(),
                        (value1, value2) -> value1));


        String processor = fieldValueMap.getOrDefault("Производитель процессора", "") + " " + fieldValueMap.getOrDefault("Модельный ряд процессора", "");
        String screenDiagonalString = fieldValueMap.getOrDefault("Диагональ экрана", "");

        int price = chooseMinPrice(priceString);
        processor = removeProcessorDuplicates(processor);
        float screenDiagonal = removeScreenDiagonalWaste(screenDiagonalString);

        return new Laptop(LocalDate.now(), name, processor, screenDiagonal, price, URL + link);
    }

    /**
     * Метод для выбора минимальной цены (Может быть так что в один div записано 2 цены: со скидкой и без нее).
     *
     * @param price строка с ценами.
     * @return минимальная цена.
     */
    private int chooseMinPrice(String price) {
        return Arrays.stream(price.replace(" ", "").split("₽"))
                .filter(x -> !x.equals(""))
                .mapToInt(Integer::parseInt)
                .min().getAsInt();
    }

    /**
     * Метод, который убирает дубликаты в названии процессора.
     *
     * @param processor строка с названием процессора.
     * @return строка без дубликатов.
     */
    private String removeProcessorDuplicates(String processor) {
        return Arrays.stream(processor.split(" "))
                .distinct()
                .collect(Collectors.joining(" "))
                .trim();
    }

    /**
     * Метод, который убирает ” из диагонали экрана.
     *
     * @param screenDiagonal строка с диагональю экрана.
     * @return диагональ экрана.
     */
    private float removeScreenDiagonalWaste(String screenDiagonal) {
        return screenDiagonal.equals("") ? 0 : Float.parseFloat(screenDiagonal
                .replace("”", "")
        );
    }
}
