package com.smartstock.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartstock.models.Stock;
import com.smartstock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;

@Service
public class AlphaVantageAPI {

    // 🔐 API key hardcoded for development
    private static final String API_KEY = "L1I7XOJUJEOK8VAZ";

    private final StockRepository stockRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AlphaVantageAPI(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void fetchAndSaveStockData(String symbol) {
        try {
            String url = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=" +
                    symbol + "&outputsize=compact&apikey=" + API_KEY;

            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);
            JsonNode timeSeries = root.get("Time Series (Daily)");

            if (timeSeries == null) {
                System.out.println("❌ Invalid or missing data for symbol: " + symbol);
                return;
            }

            Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
            if (!fields.hasNext()) {
                System.out.println("⚠️ No time series data found for " + symbol);
                return;
            }

            Map.Entry<String, JsonNode> latestEntry = fields.next();
            String date = latestEntry.getKey();
            JsonNode dailyData = latestEntry.getValue();

            Stock stock = new Stock(
                    null,
                    symbol,
                    symbol, // Placeholder for company name
                    LocalDate.parse(date),
                    getDouble(dailyData, "1. open"),
                    getDouble(dailyData, "4. close"),
                    getDouble(dailyData, "2. high"),
                    getDouble(dailyData, "3. low"),
                    getLong(dailyData, "5. volume"),
                    null, // marketCap
                    null, // peRatio
                    null, // fiftyTwoWeekHigh
                    null  // fiftyTwoWeekLow
            );

            stockRepository.save(stock);
            System.out.println("✅ Stock saved for: " + symbol + " | Date: " + date);

        } catch (Exception e) {
            System.out.println("❌ Error fetching data for " + symbol + ": " + e.getMessage());
        }
    }

    private Double getDouble(JsonNode node, String key) {
        try {
            return Double.parseDouble(node.get(key).asText());
        } catch (Exception e) {
            return null;
        }
    }

    private Long getLong(JsonNode node, String key) {
        try {
            return Long.parseLong(node.get(key).asText());
        } catch (Exception e) {
            return null;
        }
    }
}
