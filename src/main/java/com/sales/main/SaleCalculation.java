package com.sales.main;

import com.sales.part.BasePart;
import com.sales.part.Part1;
import com.sales.part.Part2;

public class SaleCalculation {
    private static final BasePart part1 = new Part1();
    private static final BasePart part2 = new Part2();
    public static void main(String[] args) {
        part1.calculate();
        part2.calculate();
    }
}
