package com.sales.models;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class Totals {
    BigDecimal totalSalesBeforeDiscount;
    BigDecimal totalSalesAfterDiscount;
    BigDecimal totalDiscountAmount;
    BigDecimal averageDiscountPercentage;

    public Totals(BigDecimal totalSalesBeforeDiscount, BigDecimal totalSalesAfterDiscount, BigDecimal totalDiscountAmount, BigDecimal averageDiscountPercentage) {
        this.totalSalesBeforeDiscount = totalSalesBeforeDiscount;
        this.totalSalesAfterDiscount = totalSalesAfterDiscount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.averageDiscountPercentage = averageDiscountPercentage;
    }
}