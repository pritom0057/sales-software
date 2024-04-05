package com.sales.part;

import com.sales.models.JSONArrayInput;
import com.sales.models.Totals;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public abstract class BasePart {
    public JSONArrayInput jsonArrayInput;
    public Map<String, BigDecimal> discountMap;
    public Map<Long, BigDecimal> productPriceMap;
    public abstract void calculate();

    public void createProductPriceMap(JSONArray productArray) {
        for (Object obj : productArray) {
            JSONObject product = (JSONObject) obj;
            long sku = (long) product.get("sku");
            BigDecimal price = BigDecimal.valueOf((double) product.get("price"));
            productPriceMap.put(sku, price);
        }
    }
    public void readFile(String partFolder, Logger logger){
        // Parse JSON files from resources folder
        InputStream discountStream = Part1.class.getResourceAsStream(partFolder + "/discounts.json");
        InputStream orderStream = Part1.class.getResourceAsStream(partFolder+"/orders.json");
        InputStream productStream = Part1.class.getResourceAsStream(partFolder+"/products.json");

        try {
            jsonArrayInput.setDiscountArray(parseJSONArray(discountStream));
            jsonArrayInput.setOrderArray(parseJSONArray(orderStream));
            jsonArrayInput.setProductArray(parseJSONArray(productStream));
        }
        catch (ParseException e) {
            logger.error("ParseException " +  e.getMessage());
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            logger.error("IOException " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private JSONArray parseJSONArray(InputStream inputStream) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new InputStreamReader(inputStream));
    }

    public void printResults(Logger logger, Totals totals) {
        // Output results
        logger.info("Total sales before discount: $" + totals.getTotalSalesBeforeDiscount());
        logger.info("Total sales after discount: $" + totals.getTotalSalesAfterDiscount());
        logger.info("Total amount of money lost via customer using Discount Codes: $" + totals.getTotalDiscountAmount());
        logger.info(String.format("Average discount per customer as a percentage: %.2f%%", totals.getAverageDiscountPercentage()));
    }
    public BigDecimal calculateAverageDiscountPercentage(BigDecimal totalSalesBeforeDiscount, BigDecimal totalDiscountAmount) {
        if (totalSalesBeforeDiscount.compareTo(BigDecimal.ZERO) != 0) {
            return totalDiscountAmount.divide(totalSalesBeforeDiscount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }
}
