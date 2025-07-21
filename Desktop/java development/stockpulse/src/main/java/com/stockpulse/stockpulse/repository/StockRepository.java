
package com.stockpulse.stockpulse.repository;

import com.stockpulse.stockpulse.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findBySymbol(String symbol);
}

