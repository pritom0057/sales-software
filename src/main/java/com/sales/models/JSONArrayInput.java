package com.sales.models;


import lombok.Data;
import org.json.simple.JSONArray;

import java.math.BigDecimal;

@Data
public class JSONArrayInput {

    private JSONArray discountArray;
    private JSONArray orderArray;
    private JSONArray productArray;

    public JSONArrayInput() {
    }
}