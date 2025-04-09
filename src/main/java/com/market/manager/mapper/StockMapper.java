package com.market.manager.mapper;

import com.market.manager.model.BarResponse;
import com.market.manager.model.StockPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper( StockMapper.class );

    @Mapping(source = "open", target = "openPrice")
    @Mapping(source = "close", target = "closePrice")
    @Mapping(source = "high", target = "highPrice")
    @Mapping(source = "low", target = "lowPrice")
    @Mapping(source = "symbol", target = "companySymbol")
    StockPrice barToStock(BarResponse bar, String symbol);

    default LocalDate fromTimestamp(Timestamp timestamp) {
        return LocalDate.ofInstant(timestamp.toInstant(), ZoneId.of("EST"));
    }

}
