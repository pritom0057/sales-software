package com.sales.utils;

import com.sales.part1.Part1;
import com.sales.models.JSONArrayInput;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public void readFile(JSONArrayInput jsonArrayInput){
        // Parse JSON files from resources folder
        InputStream discountStream = Part1.class.getResourceAsStream("/part1/discounts.json");
        InputStream orderStream = Part1.class.getResourceAsStream("/part1/orders.json");
        InputStream productStream = Part1.class.getResourceAsStream("/part1/products.json");

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
        int a = 0;
    }

    private JSONArray parseJSONArray(InputStream inputStream) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new InputStreamReader(inputStream));
    }

}
