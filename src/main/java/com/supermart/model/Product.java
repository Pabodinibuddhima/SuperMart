package com.supermart.model;

import java.math.BigDecimal;

public class Product {

    private int productId;
    private String barcode;
    private String name;
    private int categoryId;
    private Integer supplierId;

    private BigDecimal costPrice;
    private BigDecimal sellingPrice;

    private int quantity;
    private int reorderLevel;
    private String status;

    public Product() {
    }

    public Product(
            int productId,
            String barcode,
            String name,
            int categoryId,
            Integer supplierId,
            BigDecimal costPrice,
            BigDecimal sellingPrice,
            int quantity,
            int reorderLevel,
            String status) {

        this.productId = productId;
        this.barcode = barcode;
        this.name = name;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.status = status;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }
}