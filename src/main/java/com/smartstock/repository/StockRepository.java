package com.smartstock.repository;

import com.smartstock.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    // ✅ Fetch the latest unique stock entries by symbol and order them by score (descending)
    @Query("SELECT s FROM Stock s WHERE s.id IN (SELECT MAX(s2.id) FROM Stock s2 GROUP BY s2.stockSymbol) ORDER BY s.score DESC")
    List<Stock> findLatestRankedStocks();

    // ✅ Fetch the latest stock entry by symbol
    Optional<Stock> findTopByStockSymbolOrderByIdDesc(String stockSymbol);

    // (Optional) If you also want to get all records of a stock symbol
     Optional<Stock> findByStockSymbol(String stockSymbol);
}
