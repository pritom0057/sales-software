package com.sales.part;

import com.sales.models.JSONArrayInput;
import com.sales.models.OrderTotals;
import com.sales.models.Totals;
import com.sales.utils.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Part2 extends BasePart {
    private static final Logger logger = LoggerFactory.getLogger(Part2.class);
    private final Map<String, BigDecimal> stackableMap;

    public Part2() {
        jsonArrayInput = new JSONArrayInput();
        discountMap = new HashMap<>();
        productPriceMap = new HashMap<>();
        stackableMap = new HashMap<>();
    }

    public void calculate() {
        jsonArrayInput = new JSONArrayInput();
        readFile(Constants.part2, logger);
        // Store discount codes and their corresponding values in a map
        createDiscountMap(jsonArrayInput.getDiscountArray());
        createProductPriceMap(jsonArrayInput.getProductArray());

        // Calculate totals
        Totals totals = calculateTotals(jsonArrayInput.getOrderArray());

        // Output results
        logger.info("******************Printing Part 2 **********************");
        printResults(logger, totals);
        logger.info("******************End of Printing Part 2 **********************");
    }

    private void createDiscountMap(JSONArray discountArray) {
        for (Object obj : discountArray) {
            JSONObject discount = (JSONObject) obj;
            String key = (String) discount.get(Constants.key);
            BigDecimal value = BigDecimal.valueOf((double) discount.get(Constants.value));
            if (discount.containsKey(Constants.stacks) && Boolean.parseBoolean(discount.get(Constants.stacks).toString())) {
                stackableMap.put(key, value);
            }
            else {
                discountMap.put(key, value);
            }
        }
    }

    private Totals calculateTotals(JSONArray orderArray) {
        BigDecimal totalSalesBeforeDiscount = BigDecimal.ZERO;
        BigDecimal totalSalesAfterDiscount = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        for (Object obj : orderArray) {
            JSONObject order = (JSONObject) obj;
            OrderTotals orderTotals = null;
            try {
                orderTotals = calculateOrderTotals(order);
            }
            catch (ParseException e) {
                logger.error("ParseException " + e.getMessage());
                throw new RuntimeException(e);
            }
            totalSalesBeforeDiscount = totalSalesBeforeDiscount.add(orderTotals.getOrderTotalBeforeDiscount());
            totalSalesAfterDiscount = totalSalesAfterDiscount.add(orderTotals.getOrderTotalAfterDiscount());
            totalDiscountAmount = totalDiscountAmount.add(orderTotals.getDiscountAmount());
        }

        BigDecimal averageDiscountPercentage = calculateAverageDiscountPercentage(totalSalesBeforeDiscount, totalDiscountAmount);

        return new Totals(totalSalesBeforeDiscount, totalSalesAfterDiscount, totalDiscountAmount, averageDiscountPercentage);
    }

    private OrderTotals calculateOrderTotals(JSONObject order) throws ParseException {
        BigDecimal orderTotalBeforeDiscount = BigDecimal.ZERO;
        BigDecimal orderTotalAfterDiscount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;

        JSONArray items = (JSONArray) order.get(Constants.items);
        for (Object itemObj : items) {
            JSONObject item = (JSONObject) itemObj;
            long sku = (long) item.get(Constants.sku);
            long quantity = (long) item.get(Constants.quantity);
            BigDecimal price = productPriceMap.get(sku);
            if (price != null) {
                BigDecimal itemTotalBeforeDiscount = price.multiply(BigDecimal.valueOf(quantity));
                orderTotalBeforeDiscount = orderTotalBeforeDiscount.add(itemTotalBeforeDiscount);
                BigDecimal itemTotalAfterDiscount = itemTotalBeforeDiscount;
                if (order.containsKey(Constants.discount)) {
                    BigDecimal discountPercentage = BigDecimal.ZERO;
                    String[] discountCodes = ((String) order.get(Constants.discount)).split(",");
                    for (String code : discountCodes) {
                        discountPercentage = getDiscountPercentage(code, discountPercentage);
                    }
                    if (!Objects.equals(discountPercentage, BigDecimal.ZERO)) {
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

    private BigDecimal getDiscountPercentage(String code, BigDecimal discountPercentage) {
        if (discountMap.get(code) != null) {
            discountPercentage = discountPercentage.add(discountMap.get(code));
        }
        else if (stackableMap.get(code) != null) {
            discountPercentage = discountPercentage.add(stackableMap.get(code));
        }
        else {
            logger.warn("No mapping discount found with this order");
        }
        return discountPercentage;
    }
}

