// StockService.java
package com.stockpulse.stockpulse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockpulse.stockpulse.model.Stock;
import com.stockpulse.stockpulse.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {

    @Value("${stock.api.key}")
    private String apiKey;

    @Value("${stock.api.base-url}")
    private String baseUrl;

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Stock fetchLiveStock(String symbol) {
        String url = baseUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response).path("Global Quote");

            if (root.isMissingNode() || root.get("05. price") == null) {
                throw new RuntimeException("No data found for symbol: " + symbol);
            }

            double price = Double.parseDouble(root.get("05. price").asText());
            String name = symbol; // or you can extend API to get name

            Stock stock = stockRepository.findBySymbol(symbol);
            if (stock == null) {
                stock = new Stock();
                stock.setSymbol(symbol);
            }

            stock.setPrice(price);
            stock.setName(name);
            return stockRepository.save(stock);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock data: " + e.getMessage());
        }
    }
}
