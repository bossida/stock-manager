package com.market.manager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@EnableWireMock(
        @ConfigureWireMock(
                 name = "jsonplaceholder",
                 baseUrlProperties = "api.url"  //the property to inject the wiremock host and portname into
        )
)
class StockServiceTest {

    @Autowired
    StockService stockService;

    @Test
    void test(){
        var from = LocalDate.parse("2025-02-09");
        var to = LocalDate.parse("2025-02-10");
        stockService.fetchAndSaveStockData("AAPL", from, to);
    }

}