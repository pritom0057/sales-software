package com.sales.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderTotals {
    BigDecimal orderTotalBeforeDiscount;
    BigDecimal orderTotalAfterDiscount;
    BigDecimal discountAmount;

    public OrderTotals(BigDecimal orderTotalBeforeDiscount, BigDecimal orderTotalAfterDiscount, BigDecimal discountAmount) {
        this.orderTotalBeforeDiscount = orderTotalBeforeDiscount;
        this.orderTotalAfterDiscount = orderTotalAfterDiscount;
        this.discountAmount = discountAmount;
    }
}