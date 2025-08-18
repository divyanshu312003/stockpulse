package com.stockpulse.stockpulse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service

public class SentimentService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ml.service.base-url}")
    private String mlServiceUrl;

    public double getSentimentScore(String stockSymbol) {
        String url = mlServiceUrl + "/sentiment?symbol=" + stockSymbol;
        SentimentResponse response = restTemplate.getForObject(url, SentimentResponse.class);
        return response != null ? response.getScore() : 0.0;
    }

    public static class SentimentResponse {
        private double score;
        public double getScore() { return score; }
        public void setScore(double score) { this.score = score; }
    }
}

