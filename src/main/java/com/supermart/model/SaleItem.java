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

public class SaleItem {

    private int saleItemId;
    private int saleId;
    private int productId;

    private int quantity;

    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private BigDecimal lineTotal;


    public SaleItem() {
    }
    
    public SaleItem(
        int saleItemId,
        int saleId,
        int productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal unitCost,
        BigDecimal lineTotal
    ) {

        this.saleItemId = saleItemId;
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitCost = unitCost;
        this.lineTotal = lineTotal;
    }


    public int getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(
            int saleItemId
    ) {
        this.saleItemId = saleItemId;
    }


    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(
            int saleId
    ) {
        this.saleId = saleId;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(
            int productId
    ) {
        this.productId = productId;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(
            int quantity
    ) {
        this.quantity = quantity;
    }


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(
            BigDecimal unitPrice
    ) {
        this.unitPrice = unitPrice;
    }
  
    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(
            BigDecimal lineTotal
    ) {
        this.lineTotal = lineTotal;
    }
}