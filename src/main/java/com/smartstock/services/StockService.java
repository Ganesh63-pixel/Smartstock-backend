package com.smartstock.services;

import com.smartstock.models.Stock;
import com.smartstock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    // ✅ Save a single stock (insert or update)
    @Transactional
    public Stock saveStockData(Stock stock) {
        System.out.println("💾 Saving stock data for symbol: " + stock.getStockSymbol());
        return stockRepository.save(stock);
    }

    // ✅ Save multiple stocks in batch (insert or update)
    @Transactional
    public List<Stock> saveAllStockData(List<Stock> stocks) {
        System.out.println("📦 Saving multiple stocks. Total: " + stocks.size());
        return stockRepository.saveAll(stocks);
    }

    // ✅ Fetch a stock by symbol
    public Stock fetchStockBySymbol(String stockSymbol) {
        Optional<Stock> optionalStock = stockRepository.findByStockSymbol(stockSymbol);
        if (optionalStock.isPresent()) {
            System.out.println("🔍 Found stock for symbol: " + stockSymbol);
            return optionalStock.get();
        } else {
            System.out.println("❌ No stock found for symbol: " + stockSymbol);
            return null;
        }
    }

    // ✅ Fetch all stocks
    public List<Stock> fetchAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        System.out.println("📊 Total stocks fetched: " + stocks.size());
        return stocks;
    }

    // ✅ Delete a stock by symbol (if it exists)
    @Transactional
    public void deleteStockBySymbol(String symbol) {
        stockRepository.findByStockSymbol(symbol).ifPresent(stock -> {
            System.out.println("🗑 Deleting stock with symbol: " + symbol);
            stockRepository.delete(stock);
        });
    }

    // ✅ Delete all stocks
    @Transactional
    public void deleteAllStocks() {
        System.out.println("🧹 Deleting all stocks from the database.");
        stockRepository.deleteAll();
    }
}
