package com.market.manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
public class BarResponse {
    @JsonProperty("v")
    private Long volume;
    @JsonProperty("vw")
    private BigDecimal volumeWeight;
    @JsonProperty("o")
    private BigDecimal open;
    @JsonProperty("c")
    private BigDecimal close;
    @JsonProperty("h")
    private BigDecimal highest;
    @JsonProperty("l")
    private BigDecimal lowest;
    @JsonProperty("t")
    private Timestamp date;
    @JsonProperty("n")
    private Long transactions;

}
