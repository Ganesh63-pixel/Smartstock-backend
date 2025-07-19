package com.smartstock.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class AlphaVantageResponseParser {

    public String extractStockData(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);

        // Check if the response contains an error message
        if (json.has("Error Message") || json.has("Note") || json.has("Information")) {
            return "{\"error\": \"API Error: " + json.toString() + "\"}";
        }

        // Extract stock data
        if (json.has("Time Series (Daily)")) {
            JSONObject timeSeries = json.getJSONObject("Time Series (Daily)");
            return timeSeries.toString();
        }

        return "{\"error\": \"No stock data found\"}";
    }
}