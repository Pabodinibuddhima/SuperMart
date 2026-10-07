/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.model;

/**
 *
 * @author pabodini
 */
import java.math.BigDecimal;

public class ReturnItem {

    private int returnItemId;
    private int returnId;
    private int saleItemId;
    private int quantity;
    private BigDecimal refundAmount;

    public ReturnItem() {
    }

    public ReturnItem(
            int returnItemId,
            int returnId,
            int saleItemId,
            int quantity,
            BigDecimal refundAmount
    ) {
        this.returnItemId = returnItemId;
        this.returnId = returnId;
        this.saleItemId = saleItemId;
        this.quantity = quantity;
        this.refundAmount = refundAmount;
    }

    public int getReturnItemId() {
        return returnItemId;
    }

    public void setReturnItemId(int returnItemId) {
        this.returnItemId = returnItemId;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}