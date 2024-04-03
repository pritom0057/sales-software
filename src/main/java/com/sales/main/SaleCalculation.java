package com.sales.main;

import com.sales.part1.Part1;
import com.sales.models.JSONArrayInput;
import com.sales.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaleCalculation {

    private static final Logger logger = LoggerFactory.getLogger(SaleCalculation.class);

    private static final Utils utils = new Utils();;
    private static final Part1 part1 = new Part1();;
    private static final JSONArrayInput jsonArrayInput = new JSONArrayInput();
    public static void main(String[] args) {
        utils.readFile(jsonArrayInput);
        part1.calculate(jsonArrayInput);
    }


}
