package com.stockpulse.stockpulse.controller;


import com.stockpulse.stockpulse.service.SentimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/sentiment")
public class SentimentController {

    @Autowired
    private SentimentService sentimentService;

    @GetMapping("/{stockName}")
    public ResponseEntity<Double> getSentiment(@PathVariable String stockName) {
        return ResponseEntity.ok(sentimentService.getSentimentScore(stockName));
    }
}