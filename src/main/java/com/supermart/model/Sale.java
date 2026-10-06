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
import java.time.LocalDateTime;

public class Sale {

    private int saleId;
    private String invoiceNumber;
    private Integer customerId;
    private Integer employeeId;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal totalAmount;

    private String status;
    private LocalDateTime createdAt;


    public Sale() {
    }


    public Sale(
            int saleId,
            String invoiceNumber,
            Integer customerId,
            Integer employeeId,
            BigDecimal subtotal,
            BigDecimal discount,
            BigDecimal totalAmount,
            String status,
            LocalDateTime createdAt
    ) {

        this.saleId = saleId;
        this.invoiceNumber = invoiceNumber;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.subtotal = subtotal;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }


    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }


    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(
            String invoiceNumber
    ) {
        this.invoiceNumber = invoiceNumber;
    }


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(
            Integer customerId
    ) {
        this.customerId = customerId;
    }


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(
            Integer employeeId
    ) {
        this.employeeId = employeeId;
    }


    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(
            BigDecimal subtotal
    ) {
        this.subtotal = subtotal;
    }


    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(
            BigDecimal discount
    ) {
        this.discount = discount;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
            BigDecimal totalAmount
    ) {
        this.totalAmount = totalAmount;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return invoiceNumber;
    }
}