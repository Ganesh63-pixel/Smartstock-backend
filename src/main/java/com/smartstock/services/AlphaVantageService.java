package com.smartstock.services;

import com.smartstock.models.*;
import com.smartstock.repository.StockRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;

@Service
public class AlphaVantageService {

    private static final String API_KEY = "L1I7XOJUJEOK8VAZ";

    @Autowired
    private StockRepository stockRepository;

    public void fetchAndSaveStockData(String symbol, String market) {
        try {
            String formattedSymbol = market.equalsIgnoreCase("BSE") ? symbol + ".BSE" : symbol + ".NS";
            String urlString = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=" + formattedSymbol + "&apikey=" + API_KEY;

            URL url = URI.create(urlString).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("📡 Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? con.getInputStream() : con.getErrorStream()
            ));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            saveStockData(response.toString(), symbol);

        } catch (Exception e) {
            System.err.println("❌ Error fetching stock data: " + e.getMessage());
        }
    }

    private void saveStockData(String jsonResponse, String symbol) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            if (!json.has("Time Series (Daily)")) {
                System.err.println("❌ API response does not contain time series data.");
                return;
            }

            JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");
            String latestDate = timeSeries.keys().next(); // Get latest available date
            JSONObject latestData = timeSeries.getJSONObject(latestDate);

            double open = latestData.getDouble("1. open");
            double high = latestData.getDouble("2. high");
            double low = latestData.getDouble("3. low");
            double close = latestData.getDouble("4. close");
            long volume = latestData.getLong("5. volume");

            // Placeholder values
            Double peRatio = null;
            Long marketCap = null;
            Double fiftyTwoWeekHigh = null;
            Double fiftyTwoWeekLow = null;

            Stock stock = new Stock();
            stock.setStockSymbol(symbol);
            stock.setName(symbol); // Placeholder, can update with real company name later
            stock.setLatestDate(LocalDate.parse(latestDate));
            stock.setOpenPrice(open);
            stock.setHighPrice(high);
            stock.setLowPrice(low);
            stock.setClosePrice(close);
            stock.setVolume(volume);
            stock.setPeRatio(peRatio);
            stock.setMarketCap(marketCap);
            stock.setFiftyTwoWeekHigh(fiftyTwoWeekHigh);
            stock.setFiftyTwoWeekLow(fiftyTwoWeekLow);

            stockRepository.save(stock);
            System.out.println("✅ Stock saved: " + symbol + " | Date: " + latestDate);

        } catch (Exception e) {
            System.err.println("❌ Error parsing/saving stock data: " + e.getMessage());
        }
    }
}
