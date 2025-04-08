package com.market.manager.client;


import com.market.manager.model.StockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDate;

@Component
public class StockClient {
    @Value("${api.url}")
    private String apiUrl;
    private final WebClient webClient;
    private final String apiKey = "QSo5GRADl0AWaii20X7NzeqoL_Vkuo25";

    public StockClient() {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public StockResponse getStock(String ticker, int multiplier, String timespan, LocalDate from, LocalDate to) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ticker/{ticker}/range/{multiplier}/{timespan}/{from}/{to}")
                        .build(ticker, multiplier, timespan, from, to)
                )
                .retrieve()
                .bodyToMono(StockResponse.class)
                .block();
    }
}
