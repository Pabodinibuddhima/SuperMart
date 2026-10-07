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

public class Return {

    private int returnId;
    private int saleId;
    private Integer employeeId;
    private BigDecimal refundAmount;
    private String reason;
    private LocalDateTime createdAt;

    public Return() {
    }

    public Return(
            int returnId,
            int saleId,
            Integer employeeId,
            BigDecimal refundAmount,
            String reason,
            LocalDateTime createdAt
    ) {
        this.returnId = returnId;
        this.saleId = saleId;
        this.employeeId = employeeId;
        this.refundAmount = refundAmount;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}