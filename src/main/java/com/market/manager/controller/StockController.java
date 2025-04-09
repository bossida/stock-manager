package com.market.manager.controller;

import com.market.manager.dto.StockPriceDto;
import com.market.manager.exception.StockPriceNotFoundException;
import com.market.manager.service.StockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("stocks")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService){
        this.stockService = stockService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<Void> fetchAndSaveStockData(
            @RequestParam String companySymbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        stockService.fetchAndSaveStockData(companySymbol, fromDate, toDate);
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
