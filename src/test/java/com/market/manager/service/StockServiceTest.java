package com.market.manager.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@EnableWireMock(
        @ConfigureWireMock(
                 name = "jsonplaceholder",
                 property = "api.url"
        )
)
class StockServiceTest {

    @Autowired
    StockService stockService;

    @InjectWireMock("jsonplaceholder")
    private WireMockServer wiremock;

//    @Test
//    void test(){
//        //WireMock.listAllStubMappings();
//        wiremock.getStubMappings();
//        wiremock.getServeEvents().getServeEvents().forEach(x -> System.out.println(x.getRequest()));
//        var from = LocalDate.parse("2025-02-09");
//        var to = LocalDate.parse("2025-02-10");
//        stockService.fetchAndSaveStockData("AAPL", from, to);
//    }

}