package com.market.manager.unit.service;

import com.market.manager.client.PolygonClient;
import com.market.manager.exception.StockPriceNotFoundException;
import com.market.manager.model.BarResponse;
import com.market.manager.model.StockPrice;
import com.market.manager.model.StockResponse;
import com.market.manager.repository.StockPriceRepository;
import com.market.manager.service.StockService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.market.manager.constants.ApplicationConstants.MULTIPLIER;
import static com.market.manager.constants.ApplicationConstants.TIME_SPAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private PolygonClient polygonClient;

    @Mock
    private  StockPriceRepository repository;

    @InjectMocks
    private StockService stockService;

    private final static String SYMBOL = "AAPL";

    private StockResponse buildStockResponse(){
        var barPrice = BarResponse.builder()
                .open(BigDecimal.valueOf(150))
                .close(BigDecimal.valueOf(200))
                .highest(BigDecimal.valueOf(210))
                .lowest(BigDecimal.valueOf(120))
                .date(new Timestamp(17392386861000L))
                .volume(2000000L)
                .transactions(999123L)
                .volumeWeight(BigDecimal.valueOf(200))
                .build();
        return StockResponse.builder()
                .results(List.of(barPrice))
                .count(1)
                .queryCount(1)
                .resultsCount(1)
                .ticker(SYMBOL)
                .status("OK")
                .build();
    }

    private StockResponse buildEmptyStock(){

        return StockResponse.builder()
                .results(new ArrayList<>())
                .count(0)
                .queryCount(0)
                .resultsCount(0)
                .ticker(SYMBOL)
                .status("OK")
                .build();
    }

    private StockPrice buildStockPrice(){
        return StockPrice.builder()
                .closePrice(BigDecimal.valueOf(200))
                .lowPrice(BigDecimal.valueOf(100))
                .openPrice(BigDecimal.valueOf(120))
                .highPrice(BigDecimal.valueOf(220))
                .companySymbol(SYMBOL)
                .volume(2000000L)
                .date(LocalDate.of(2025, 2, 10))
                .id(1L)
                .build();
    }

    @Test
    @SneakyThrows
    void GivenDataExists_WhenFetchIsCalled_ThenDataIsSaved(){
        var dateFrom = LocalDate.of(2025, 2, 9);
        var dateTo = LocalDate.of(2025, 2, 10);
        var stockResponse = buildStockResponse();
        when(polygonClient.getStock(SYMBOL, MULTIPLIER, TIME_SPAN, dateFrom, dateTo)).thenReturn(stockResponse);
        stockService.fetchAndSaveStockData(SYMBOL, dateFrom, dateTo);
        verify(repository, times(1)).save(any(StockPrice.class));
    }

    @Test
    @SneakyThrows
    void GivenDataDoesNotExists_WhenFetchIsCalled_ThenDataIsNotSaved(){
        var from = LocalDate.of(2025, 2, 9);
        var to = LocalDate.of(2025, 2, 10);
        var emptyStock = buildEmptyStock();
        when(polygonClient.getStock(SYMBOL, MULTIPLIER, TIME_SPAN, from, to)).thenReturn(emptyStock);
        assertThatExceptionOfType(StockPriceNotFoundException.class)
                .isThrownBy(() ->  stockService.fetchAndSaveStockData(SYMBOL, from, to));
        verifyNoInteractions(repository);
    }

    @Test
    @SneakyThrows
    void GivenDataExists_WhenGetIsCalled_ThenDataIsRetrieved(){
        var searchDate = LocalDate.of(2025, 2, 9);
        var stockPrice = buildStockPrice();
        when(repository.findByCompanySymbolAndDate(SYMBOL, searchDate)).thenReturn(Optional.of(stockPrice));
        var stockFound = stockService.getStockBySymbolAndDate(SYMBOL, searchDate);
        assertThat(stockFound).isNotNull();
        assertThat(stockFound.getCompanySymbol()).isEqualTo(SYMBOL);
        assertThat(stockFound.getDate()).isEqualTo(LocalDate.of(2025, 2, 10));
        assertThat(stockFound.getOpenPrice()).isEqualTo(BigDecimal.valueOf(120));
        assertThat(stockFound.getClosePrice()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(stockFound.getHighPrice()).isEqualTo(BigDecimal.valueOf(220));
        assertThat(stockFound.getLowPrice()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(stockFound.getVolume()).isEqualTo(2000000L);
    }

    @Test
    @SneakyThrows
    void GivenDataDoesNotExists_WhenGetIsCalled_ThenExceptionIsThrown(){
        var searchDate = LocalDate.of(2025, 2, 9);
        when(repository.findByCompanySymbolAndDate(SYMBOL, searchDate)).thenReturn(Optional.empty());
        assertThatExceptionOfType(StockPriceNotFoundException.class)
                .isThrownBy(() -> stockService.getStockBySymbolAndDate(SYMBOL, searchDate));
    }

}
