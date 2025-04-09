package com.market.manager.client;


import com.market.manager.model.StockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDate;

@Component
public class StockClient {
    @Autowired
    private WebClient webClient;


    public StockResponse getStock(String ticker, int multiplier, String timespan, LocalDate from, LocalDate to) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ticker/{ticker}/range/{multiplier}/{timespan}/{from}/{to}")
                        .build(ticker, multiplier, timespan, from, to)
                )
                .retrieve()
                .bodyToMono(StockResponse.class)
                .block();
    }
}
