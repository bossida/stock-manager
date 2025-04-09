package com.market.manager.service;

import com.market.manager.client.StockClient;
import com.market.manager.constants.ApplicationConstants;
import com.market.manager.mapper.StockMapper;
import com.market.manager.model.StockPrice;
import com.market.manager.model.StockResponse;
import com.market.manager.repository.StockPriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.market.manager.constants.ApplicationConstants.MULTIPLIER;
import static com.market.manager.constants.ApplicationConstants.TIME_SPAN;

@Service
public class StockService {
    private StockClient stockClient;
    private StockPriceRepository repository;

    public StockService(StockClient stockClient, StockPriceRepository repository) {
        this.stockClient = stockClient;
        this.repository = repository;
    }

    public void fetchAndSaveStockData(String companySymbol, LocalDate fromDate, LocalDate toDate) {
        var stockData = stockClient.getStock(companySymbol, MULTIPLIER, TIME_SPAN, fromDate, toDate);
        var repoData = mapToRepository(stockData);
        saveData(repoData);
        System.out.println("getting stock data");
    }

    private void saveData(List<StockPrice> repoData) {
        for (StockPrice price : repoData){
            repository.save(price);
        }
    }

    private List<StockPrice> mapToRepository(StockResponse stockData) {
        var stockPrices = new ArrayList<StockPrice>();
        for (var bar : stockData.getResults()){
            var stockPrice = StockMapper.INSTANCE.barToStock(bar, stockData.getTicker());
            stockPrices.add(stockPrice);
        }
        return stockPrices;
    }


}
