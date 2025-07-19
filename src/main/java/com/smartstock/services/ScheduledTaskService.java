package com.smartstock.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class ScheduledTaskService {

    @Autowired
    private AlphaVantageService alphaVantageService;

    private static final List<String> COMPANY_SYMBOLS = List.of(
    		

    );

    @PostConstruct
    public void runStockDataFetch() {
        System.out.println("Fetching stock data for all companies...");

        for (int i = 0; i < COMPANY_SYMBOLS.size(); i += 5) {
            int endIndex = Math.min(i + 5, COMPANY_SYMBOLS.size());
            List<String> currentBatch = COMPANY_SYMBOLS.subList(i, endIndex);

            for (String symbol : currentBatch) {
                
                    alphaVantageService.fetchAndSaveStockData(symbol, "BSE");
                
            }

            System.out.println("Fetched stock data for companies: " + currentBatch);

            if (endIndex < COMPANY_SYMBOLS.size()) {
                try {
                    System.out.println("Waiting for 1 minute before fetching next batch...");
                    Thread.sleep(60000); // Pause for 1 minute (60,000 milliseconds)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Stock fetching interrupted.");
                    return;
                }
            }
        }

        System.out.println("✅ All company stock data fetched. Task completed.");
    }
}
