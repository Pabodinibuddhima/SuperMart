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

public class Payment {

    private int paymentId;
    private int saleId;

    private String paymentMethod;
    private BigDecimal amount;

    private LocalDateTime createdAt;


    public Payment() {
    }


    public Payment(
            int paymentId,
            int saleId,
            String paymentMethod,
            BigDecimal amount,
            LocalDateTime createdAt
    ) {

        this.paymentId = paymentId;
        this.saleId = saleId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.createdAt = createdAt;
    }


    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(
            int paymentId
    ) {
        this.paymentId = paymentId;
    }


    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(
            int saleId
    ) {
        this.saleId = saleId;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod
    ) {
        this.paymentMethod = paymentMethod;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(
            BigDecimal amount
    ) {
        this.amount = amount;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }
}