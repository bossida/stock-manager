package com.market.manager.model;

import lombok.Data;

import java.util.List;

@Data
public class StockResponse {
    private String ticker;
    private Integer queryCount;
    private Integer resultsCount;
    private boolean adjusted;
    private String status;
    private String requestId;
    private Integer count;
    private List<BarResponse> results;

}
