package com.stockpulse.stockpulse.service;

import com.stockpulse.stockpulse.model.user;
import com.stockpulse.stockpulse.model.watchlist;
import com.stockpulse.stockpulse.repository.watchlistrepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SentimentThresholdWatcher {

    private final watchlistrepository watchlistrepository;
    private final SentimentService sentimentService;
    private final NotificationService notificationService;
    private static final Logger log = LoggerFactory.getLogger(SentimentThresholdWatcher.class);
    @Autowired
    public SentimentThresholdWatcher(
            watchlistrepository watchlistrepository,
            SentimentService sentimentService,
            NotificationService notificationService
    ) {
        this.watchlistrepository = watchlistrepository;
        this.sentimentService = sentimentService;
        this.notificationService = notificationService;
    }

    // Trigger every 24 hours (1 day)
    @Scheduled(fixedRate = 10000)
    public void checkSentimentThresholds() {
        log.info("⏰ Checking sentiment scores for watchlisted stocks...");

        List<watchlist> allWatchlistItems = watchlistrepository.findAll();

        for (watchlist item : allWatchlistItems) {
            String symbol = item.getStockSymbol();
            user user = item.getUser();

            double sentimentScore = sentimentService.getSentimentScore(symbol);

            if (sentimentScore > 0.5) {
                log.info("✅ Alert triggered for {} (score: {}) for user {}", symbol, sentimentScore, user.getEmail());
                notificationService.sendAlert(user, symbol, sentimentScore);
            }
        }
    }
}