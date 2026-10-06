/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.service;

/**
 *
 * @author pabodini
 */
import com.supermart.dao.SaleDAO;
import com.supermart.exception.InsufficientStockException;
import com.supermart.model.Payment;
import com.supermart.model.Sale;
import com.supermart.model.SaleItem;
import com.supermart.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SaleService {

    private final SaleDAO saleDAO;

    public SaleService() {
        this.saleDAO = new SaleDAO();
    }
    public int processSale(
            Sale sale,
            List<SaleItem> items,
            Payment payment
    ) throws SQLException, InsufficientStockException {
        
        validateSale(
                sale,
                items,
                payment
        );

        calculateTotals(
                sale,
                items
        );

        validatePayment(
                sale,
                payment
        );

        try (Connection connection =
                DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // ------------------------------------------
                // 1. Create main sale record
                // ------------------------------------------

                int saleId =
                        saleDAO.addSale(
                                connection,
                                sale
                        );

                sale.setSaleId(saleId);


                // ------------------------------------------
                // 2. Process every cart item
                // ------------------------------------------

                for (SaleItem item : items) {

                    item.setSaleId(saleId);

                    saleDAO.addSaleItem(
                            connection,
                            item
                    );


                    boolean stockUpdated =
                            saleDAO.deductProductStock(
                                    connection,
                                    item.getProductId(),
                                    item.getQuantity()
                            );


                    if (!stockUpdated) {

                        throw new InsufficientStockException(
                                "Insufficient stock for product ID "
                                + item.getProductId()
                                + "."
                        );
                    }


                    saleDAO.addSaleStockTransaction(
                            connection,
                            item.getProductId(),
                            item.getQuantity(),
                            sale.getInvoiceNumber()
                    );
                }


                // ------------------------------------------
                // 3. Save payment
                // ------------------------------------------

                payment.setSaleId(saleId);

                saleDAO.addPayment(
                        connection,
                        payment
                );


                // ------------------------------------------
                // 4. Everything succeeded
                // ------------------------------------------

                connection.commit();

                return saleId;


            } catch (SQLException
                    | InsufficientStockException e) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }

                throw e;


            } catch (RuntimeException e) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }

                throw e;


            } finally {

                try {

                    connection.setAutoCommit(true);

                } catch (SQLException ignored) {
                    // Connection is about to close.
                }
            }
        }
    }


    // ==========================================
    // VALIDATION
    // ==========================================

    private void validateSale(
            Sale sale,
            List<SaleItem> items,
            Payment payment
    ) {

        if (sale == null) {
            throw new IllegalArgumentException(
                    "Sale cannot be null."
            );
        }


        if (sale.getInvoiceNumber() == null
                || sale.getInvoiceNumber()
                        .isBlank()) {

            throw new IllegalArgumentException(
                    "Invoice number is required."
            );
        }


        if (items == null
                || items.isEmpty()) {

            throw new IllegalArgumentException(
                    "A sale must contain "
                    + "at least one item."
            );
        }


        for (SaleItem item : items) {

            if (item == null) {

                throw new IllegalArgumentException(
                        "Sale item cannot be null."
                );
            }


            if (item.getProductId() <= 0) {

                throw new IllegalArgumentException(
                        "Invalid product ID."
                );
            }


            if (item.getQuantity() <= 0) {

                throw new IllegalArgumentException(
                        "Product quantity "
                        + "must be greater than zero."
                );
            }


            if (item.getUnitPrice() == null
                    || item.getUnitPrice()
                            .compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Invalid product price."
                );
            }
            
            
            if (item.getUnitCost() == null
                    || item.getUnitCost()
                            .compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Invalid product cost."
                );
            }
            
            
            
        }


        if (payment == null) {

            throw new IllegalArgumentException(
                    "Payment is required."
            );
        }


        if (payment.getPaymentMethod() == null
                || (!payment.getPaymentMethod()
                        .equals("CASH")
                && !payment.getPaymentMethod()
                        .equals("CARD"))) {

            throw new IllegalArgumentException(
                    "Payment method must be "
                    + "CASH or CARD."
            );
        }


        if (payment.getAmount() == null
                || payment.getAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Invalid payment amount."
            );
        }


        if (sale.getDiscount() == null) {

            sale.setDiscount(
                    BigDecimal.ZERO
            );
        }


        if (sale.getDiscount()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Discount cannot be negative."
            );
        }


        if (sale.getStatus() == null
                || sale.getStatus().isBlank()) {

            sale.setStatus("COMPLETED");
        }
    }


    // ==========================================
    // CALCULATE TOTALS
    // ==========================================

    private void calculateTotals(
            Sale sale,
            List<SaleItem> items
    ) {

        BigDecimal subtotal =
                BigDecimal.ZERO;


        for (SaleItem item : items) {

            BigDecimal lineTotal =
                    item.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getQuantity()
                                    )
                            );


            item.setLineTotal(
                    lineTotal
            );


            subtotal =
                    subtotal.add(
                            lineTotal
                    );
        }


        if (sale.getDiscount()
                .compareTo(subtotal) > 0) {

            throw new IllegalArgumentException(
                    "Discount cannot be greater "
                    + "than the subtotal."
            );
        }


        BigDecimal total =
                subtotal.subtract(
                        sale.getDiscount()
                );


        sale.setSubtotal(
                subtotal
        );

        sale.setTotalAmount(
                total
        );
    }
    
    
    private void validatePayment(
            Sale sale,
            Payment payment
    ) {

        BigDecimal total =
                sale.getTotalAmount();

        BigDecimal amount =
                payment.getAmount();


        if ("CARD".equals(
                payment.getPaymentMethod()
        )) {

            if (amount.compareTo(
                    total
            ) != 0) {

                throw new IllegalArgumentException(
                        "Card payment must equal "
                        + "the sale total."
                );
            }

            return;
        }


        if ("CASH".equals(
                payment.getPaymentMethod()
        )) {

            if (amount.compareTo(
                    total
            ) < 0) {

                throw new IllegalArgumentException(
                        "Cash received cannot be "
                        + "less than the sale total."
                );
            }
        }
    }
    
}