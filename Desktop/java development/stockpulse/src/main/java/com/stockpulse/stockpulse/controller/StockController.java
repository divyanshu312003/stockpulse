
package com.stockpulse.stockpulse.controller;

import com.stockpulse.stockpulse.model.Stock;
import com.stockpulse.stockpulse.repository.StockRepository;
import com.stockpulse.stockpulse.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private  StockRepository stockRepository;
    @Autowired
    private StockService stockService;

    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        return stockRepository.save(stock);
    }

    @GetMapping("/{symbol}")
    public Stock getStockBySymbol(@PathVariable String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    @GetMapping("/live/{symbol}")
    public Stock getLiveStock(@PathVariable String symbol) {
        return stockService.fetchLiveStock(symbol);
    }

}


