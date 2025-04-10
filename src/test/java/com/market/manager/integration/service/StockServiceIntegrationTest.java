package com.market.manager.integration.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.market.manager.exception.StockPriceNotFoundException;
import com.market.manager.service.StockService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EnableWireMock(
        @ConfigureWireMock(
                 name = "polygon",
                 property = "api.url"
        )
)
class StockServiceIntegrationTest {

    @Autowired
    StockService stockService;

    @InjectWireMock("polygon")
    private WireMockServer wiremock;

    private final static String SYMBOL = "AAPL";


    @Test
    @SneakyThrows
    void GivenDataExists_WhenGetStockIsCalled_ThenReturnSuccess() {
        var from = LocalDate.of(2025, 2, 9);
        var to = LocalDate.of(2025, 2, 10);
        stockService.fetchAndSaveStockData(SYMBOL, from, to);
        var dto = stockService.getStockBySymbolAndDate(SYMBOL, to);
        assertThat(dto).isNotNull();
    }

    @Test
    @SneakyThrows
    void GivenDataDoesNotExist_WhenGetStockIsCalled_ThenThrowException() {
        var from = LocalDate.of(2025, 2, 9);
        var to = LocalDate.of(2025, 2, 10);
        var search = LocalDate.of(2020, 4, 1);
        stockService.fetchAndSaveStockData(SYMBOL, from, to);
        assertThatExceptionOfType(StockPriceNotFoundException.class)
                .isThrownBy(() -> stockService.getStockBySymbolAndDate(SYMBOL, search));
    }

}