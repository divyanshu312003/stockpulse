package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.config.JwtUserUtil;
import com.stockpulse.stockpulse.dto.WatchlistRequest;
import com.stockpulse.stockpulse.model.user;
import com.stockpulse.stockpulse.model.watchlist;
import com.stockpulse.stockpulse.repository.watchlistrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private watchlistrepository watchlistRepository;

    @Autowired
    private JwtUserUtil jwtUserUtil;

    // ➕ Add to watchlist
    @PostMapping
    public ResponseEntity<String> addToWatchlist(@RequestBody WatchlistRequest request) {
        user user = jwtUserUtil.getCurrentUser();

        watchlist item = new watchlist();
        item.setUser(user);
        item.setStockSymbol(request.getStockSymbol());

        watchlistRepository.save(item);
        return ResponseEntity.ok("Added to watchlist");
    }

    // ➖ Remove from watchlist
    @DeleteMapping("/{symbol}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable String symbol) {
        user user = jwtUserUtil.getCurrentUser();
        watchlistRepository.deleteByUserAndStockSymbol(user, symbol.toUpperCase());

        return ResponseEntity.ok("Removed from watchlist");
    }

    // 📋 Get all watchlist items
    @GetMapping
    public ResponseEntity<List<String>> getWatchlist() {
        user user = jwtUserUtil.getCurrentUser();
        List<watchlist> items = watchlistRepository.findByUser(user);

        List<String> symbols = items.stream()
                .map(watchlist::getStockSymbol)
                .collect(Collectors.toList());

        return ResponseEntity.ok(symbols);
    }
}
