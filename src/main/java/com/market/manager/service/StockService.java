package com.market.manager.service;

import com.market.manager.client.StockClient;
import com.market.manager.dto.StockPriceDto;
import com.market.manager.exception.StockPriceNotFoundException;
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
import static com.market.manager.constants.Errors.STOCK_NOT_FOUND;


@Service
public class StockService {
    private final StockClient stockClient;
    private final StockPriceRepository repository;

    public StockService(StockClient stockClient, StockPriceRepository repository) {
        this.stockClient = stockClient;
        this.repository = repository;
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

    public void fetchAndSaveStockData(String companySymbol, LocalDate fromDate, LocalDate toDate) {
        var stockData = stockClient.getStock(companySymbol, MULTIPLIER, TIME_SPAN, fromDate, toDate);
        var repoData = mapToRepository(stockData);
        saveData(repoData);
    }

    public StockPriceDto getStockBySymbolAndDate(String companySymbol, LocalDate date) throws StockPriceNotFoundException {
        var stock = repository.findByCompanySymbolAndDate(companySymbol, date);
        if (stock.isPresent()){
            return StockMapper.INSTANCE.stockPriceToDto(stock.get());
        }else{
            throw new StockPriceNotFoundException(STOCK_NOT_FOUND);
        }
    }

}
