package com.market.manager.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
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
