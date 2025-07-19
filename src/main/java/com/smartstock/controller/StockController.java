package com.smartstock.controller;

import com.smartstock.models.Stock;
import com.smartstock.repository.StockRepository;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {

    private final StockRepository stockRepository;

    public StockController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // ✅ 1. Get all stocks
    @GetMapping
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // ✅ 2. Get the latest stock by symbol (handles multiple entries)
    @GetMapping("/{symbol}")
    public Stock getStockBySymbol(@PathVariable String symbol) {
        // Decode special characters like %26 back to &
        String decodedSymbol = URLDecoder.decode(symbol, StandardCharsets.UTF_8);
        System.out.println("Fetching stock for decoded symbol: " + decodedSymbol);

        return stockRepository.findTopByStockSymbolOrderByIdDesc(decodedSymbol)
                .orElseThrow(() -> new RuntimeException("Stock not found with symbol: " + decodedSymbol));
    }

    // ✅ 3. Save or update stock
    @PostMapping
    public String saveStock(@RequestBody Stock stock) {
        stockRepository.save(stock);
        return "Stock saved successfully!";
    }

    // ✅ 4. Ranked list by score (latest unique stocks only)
    @GetMapping("/ranked")
    public List<Stock> getRankedStocks() {
        return stockRepository.findLatestRankedStocks();
    }
}
