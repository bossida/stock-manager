package com.market.manager.controller;

import com.market.manager.dto.FetchInputDto;
import com.market.manager.dto.StockPriceDto;
import com.market.manager.exception.InvalidDateException;
import com.market.manager.exception.StockPriceNotFoundException;
import com.market.manager.service.StockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<Void> fetchAndSaveStockData(@RequestBody FetchInputDto request) throws InvalidDateException {

        stockService.fetchAndSaveStockData(request.getCompanySymbol(), request.getFromDate(), request.getToDate());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{companySymbol}")
    public ResponseEntity<StockPriceDto> getStockBySymbolAndDate(
            @PathVariable String companySymbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws StockPriceNotFoundException {

        var stock = stockService.getStockBySymbolAndDate(companySymbol, date);
        return ResponseEntity.ok(stock);
    }

}
