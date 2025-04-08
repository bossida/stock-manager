package com.market.manager.service;

import com.market.manager.client.StockClient;
import com.market.manager.constants.ApplicationConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.market.manager.constants.ApplicationConstants.MULTIPLIER;
import static com.market.manager.constants.ApplicationConstants.TIME_SPAN;

@Service
public class StockService {
    private StockClient stockClient;

    public StockService(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    public void fetchAndSaveStockData(String companySymbol, LocalDate fromDate, LocalDate toDate) {
        var stockData = stockClient.getStock(companySymbol, MULTIPLIER, TIME_SPAN, fromDate, toDate);
        System.out.println("getting stock data");
    }


}
