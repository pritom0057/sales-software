package com.sales.part1;

import com.sales.models.JSONArrayInput;
import com.sales.models.OrderTotals;
import com.sales.models.Totals;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Part1 {
    private static final Logger logger = LoggerFactory.getLogger(Part1.class);

    public void calculate(JSONArrayInput jsonArrayInput) {
        // Store discount codes and their corresponding values in a map
        Map<String, BigDecimal> discountMap = createDiscountMap(jsonArrayInput.getDiscountArray());
        Map<Long, BigDecimal> productPriceMap = createProductPriceMap(jsonArrayInput.getProductArray());

        // Calculate totals
        Totals totals = calculateTotals(jsonArrayInput.getOrderArray(), productPriceMap, discountMap);

        // Output results
        logger.info("Total sales before discount: $" + totals.getTotalSalesBeforeDiscount());
        logger.info("Total sales after discount: $" + totals.getTotalSalesAfterDiscount());
        logger.info("Total amount of money lost via customer using Discount Codes: $" + totals.getTotalDiscountAmount());
        logger.info(String.format("Average discount per customer as a percentage: %.2f%%", totals.getAverageDiscountPercentage()));

    }

    private Map<String, BigDecimal> createDiscountMap(JSONArray discountArray) {
        Map<String, BigDecimal> discountMap = new HashMap<>();
        for (Object obj : discountArray) {
            JSONObject discount = (JSONObject) obj;
            String key = (String) discount.get("key");
            BigDecimal value = BigDecimal.valueOf((double) discount.get("value"));
            discountMap.put(key, value);
        }
        return discountMap;
    }

    private Map<Long, BigDecimal> createProductPriceMap(JSONArray productArray) {
        Map<Long, BigDecimal> productPriceMap = new HashMap<>();
        for (Object obj : productArray) {
            JSONObject product = (JSONObject) obj;
            long sku = (long) product.get("sku");
            BigDecimal price = BigDecimal.valueOf((double) product.get("price"));
            productPriceMap.put(sku, price);
        }
        return productPriceMap;
    }

    private Totals calculateTotals(JSONArray orderArray, Map<Long, BigDecimal> productPriceMap, Map<String, BigDecimal> discountMap) {
        BigDecimal totalSalesBeforeDiscount = BigDecimal.ZERO;
        BigDecimal totalSalesAfterDiscount = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        for (Object obj : orderArray) {
            JSONObject order = (JSONObject) obj;
            OrderTotals orderTotals = calculateOrderTotals(order, productPriceMap, discountMap);
            totalSalesBeforeDiscount = totalSalesBeforeDiscount.add(orderTotals.getOrderTotalBeforeDiscount());
            totalSalesAfterDiscount = totalSalesAfterDiscount.add(orderTotals.getOrderTotalAfterDiscount());
            totalDiscountAmount = totalDiscountAmount.add(orderTotals.getDiscountAmount());
        }

        BigDecimal averageDiscountPercentage = calculateAverageDiscountPercentage(totalSalesBeforeDiscount, totalDiscountAmount);

        return new Totals(totalSalesBeforeDiscount, totalSalesAfterDiscount, totalDiscountAmount, averageDiscountPercentage);
    }

    private OrderTotals calculateOrderTotals(JSONObject order, Map<Long, BigDecimal> productPriceMap, Map<String, BigDecimal> discountMap) {
        BigDecimal orderTotalBeforeDiscount = BigDecimal.ZERO;
        BigDecimal orderTotalAfterDiscount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;

        JSONArray items = (JSONArray) order.get("items");
        for (Object itemObj : items) {
            JSONObject item = (JSONObject) itemObj;
            long sku = (long) item.get("sku");
            long quantity = (long) item.get("quantity");
            BigDecimal price = productPriceMap.get(sku);
            if (price != null) {
                BigDecimal itemTotalBeforeDiscount = price.multiply(BigDecimal.valueOf(quantity));
                orderTotalBeforeDiscount = orderTotalBeforeDiscount.add(itemTotalBeforeDiscount);
                BigDecimal itemTotalAfterDiscount = itemTotalBeforeDiscount;
                if (order.containsKey("discount")) {
                    String discountCode = (String) order.get("discount");
                    BigDecimal discountPercentage = discountMap.get(discountCode);
                    if (discountPercentage != null) {
                        BigDecimal itemDiscountAmount = itemTotalBeforeDiscount.multiply(discountPercentage);
                        discountAmount = discountAmount.add(itemDiscountAmount);
                        itemTotalAfterDiscount = itemTotalBeforeDiscount.subtract(itemDiscountAmount);
                    }
                }
                orderTotalAfterDiscount = orderTotalAfterDiscount.add(itemTotalAfterDiscount);
            }
        }

        return new OrderTotals(orderTotalBeforeDiscount, orderTotalAfterDiscount, discountAmount);
    }

    private BigDecimal calculateAverageDiscountPercentage(BigDecimal totalSalesBeforeDiscount, BigDecimal totalDiscountAmount) {
        if (totalSalesBeforeDiscount.compareTo(BigDecimal.ZERO) != 0) {
            return totalDiscountAmount.divide(totalSalesBeforeDiscount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

}
