package com.market.manager.service;

import com.market.manager.client.PolygonClient;
import com.market.manager.dto.StockPriceDto;
import com.market.manager.exception.InvalidDateException;
import com.market.manager.exception.StockPriceNotFoundException;
import com.market.manager.mapper.StockMapper;
import com.market.manager.model.StockPrice;
import com.market.manager.model.StockResponse;
import com.market.manager.repository.StockPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.market.manager.constants.ApplicationConstants.MULTIPLIER;
import static com.market.manager.constants.ApplicationConstants.TIME_SPAN;
import static com.market.manager.constants.Errors.*;

@Slf4j
@Service
public class StockService {
    private final PolygonClient polygonClient;
    private final StockPriceRepository repository;

    public StockService(PolygonClient polygonClient, StockPriceRepository repository) {
        this.polygonClient = polygonClient;
        this.repository = repository;
    }

    private void saveData(List<StockPrice> repoData) {
        for (var stockPrice : repoData){
            var stock = repository.findByCompanySymbolAndDate(stockPrice.getCompanySymbol(), stockPrice.getDate());
            if (stock.isEmpty()){
                repository.save(stockPrice);
            }else{
                log.warn(STOCK_DATE_ALREADY_EXISTS);
            }
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

    public void fetchAndSaveStockData(String companySymbol, LocalDate fromDate, LocalDate toDate) throws InvalidDateException {
        if (fromDate.isEqual(toDate) || fromDate.isAfter(toDate)){
            throw new InvalidDateException(INVALID_DATES);
        }
        var stockData = polygonClient.getStock(companySymbol, MULTIPLIER, TIME_SPAN, fromDate, toDate);
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
