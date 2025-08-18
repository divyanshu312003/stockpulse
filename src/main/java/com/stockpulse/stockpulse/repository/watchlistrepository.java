package com.stockpulse.stockpulse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stockpulse.stockpulse.model.*;
import java.util.*;

import java.util.Optional;
@Repository
public interface watchlistrepository extends JpaRepository<watchlist, Long> {
    List<watchlist> findByUser(user user);
    void deleteByUserAndStockSymbol(user user, String symbol);
}
