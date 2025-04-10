package com.market.manager.integration.controller;

import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@EnableWireMock(
        @ConfigureWireMock(
                name = "polygon",
                property = "api.url"
        )
)
@ExtendWith(SpringExtension.class)
public class StockControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void GivenValidInput_WhenFetchIsCalled_ThenItShouldReturnSuccess() throws Exception {
        var requestJson = """
            {
              "companySymbol": "AAPL",
              "fromDate": "2025-02-09",
              "toDate": "2025-02-10"
            }
        """;
        mockMvc.perform(post("/stocks/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void GivenMissingSymbol_WhenFetchIsCalled_ThenItShouldReturnError() throws Exception {
        var requestJson = """
        {
          "fromDate": "2024-02-18",
          "toDate": "2024-02-19"
        }
        """;
        mockMvc.perform(post("/stocks/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(jsonPath("$.companySymbol").value("Company symbol is required"));
    }

    @Test
    void GivenMissingFromDate_WhenFetchIsCalled_ThenItShouldReturnError() throws Exception {
        var requestJson = """
        {
          "companySymbol": "AAPL",
          "toDate": "2024-02-19"
        }
        """;
        mockMvc.perform(post("/stocks/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(jsonPath("$.fromDate").value("From date is required"));
    }


    @Test
    void givenValidSymbol_whenGetStockPrices_thenReturnsStockPriceList() throws Exception {
        var requestJson = """
            {
              "companySymbol": "AAPL",
              "fromDate": "2025-02-09",
              "toDate": "2025-02-10"
            }
        """;
        mockMvc.perform(post("/stocks/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));
        mockMvc.perform(get("/stocks/AAPL?date=2025-02-10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
