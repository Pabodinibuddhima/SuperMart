/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.service;

/**
 *
 * @author pabodini
 */
import com.supermart.dao.ReturnDAO;
import com.supermart.exception.InvalidReturnException;
import com.supermart.model.Return;
import com.supermart.model.ReturnItem;
import com.supermart.util.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;


public class ReturnService {

    private final ReturnDAO returnDAO;


    public ReturnService() {

        returnDAO =
                new ReturnDAO();
    }


    public int processReturn(
            Return returnTransaction,
            List<ReturnItem> items
    ) throws SQLException,
             InvalidReturnException {

        validateBasicReturn(
                returnTransaction,
                items
        );


        try (Connection connection =
                DBConnection.getConnection()) {

            connection.setAutoCommit(
                    false
            );


            try {

                BigDecimal totalRefund =
                        BigDecimal.ZERO;


                // ------------------------------------------
                // VALIDATE + CALCULATE EACH RETURN ITEM
                // ------------------------------------------

                for (ReturnItem item : items) {

                    Object[] saleItem =
                            returnDAO.getSaleItemForReturn(
                                    connection,
                                    item.getSaleItemId()
                            );


                    if (saleItem == null) {

                        throw new InvalidReturnException(
                                "Sale item "
                                + item.getSaleItemId()
                                + " could not be found."
                        );
                    }


                    int originalSaleId =
                            (Integer)
                            saleItem[1];


                    int productId =
                            (Integer)
                            saleItem[2];


                    int soldQuantity =
                            (Integer)
                            saleItem[3];


                    BigDecimal unitPrice =
                            (BigDecimal)
                            saleItem[4];


                    BigDecimal subtotal =
                            (BigDecimal)
                            saleItem[5];


                    BigDecimal discount =
                            (BigDecimal)
                            saleItem[6];


                    String saleStatus =
                            (String)
                            saleItem[8];


                    // --------------------------------------
                    // Ensure item belongs to selected sale
                    // --------------------------------------

                    if (originalSaleId
                            != returnTransaction.getSaleId()) {

                        throw new InvalidReturnException(
                                "A returned item does not "
                                + "belong to the selected sale."
                        );
                    }


                    if ("REFUNDED".equals(
                            saleStatus
                    )) {

                        throw new InvalidReturnException(
                                "This sale has already "
                                + "been fully refunded."
                        );
                    }


                    // --------------------------------------
                    // Prevent returning same units twice
                    // --------------------------------------

                    int alreadyReturned =
                            returnDAO.getReturnedQuantity(
                                    connection,
                                    item.getSaleItemId()
                            );


                    int availableToReturn =
                            soldQuantity
                            - alreadyReturned;


                    if (item.getQuantity()
                            > availableToReturn) {

                        throw new InvalidReturnException(
                                "Return quantity exceeds "
                                + "the quantity still "
                                + "available for return."
                        );
                    }


                    // --------------------------------------
                    // Calculate refund after original
                    // sale-level discount
                    // --------------------------------------

                    BigDecimal discountRatio =
                            BigDecimal.ZERO;


                    if (subtotal != null
                            && subtotal.compareTo(
                                    BigDecimal.ZERO
                            ) > 0) {

                        discountRatio =
                                discount.divide(
                                        subtotal,
                                        10,
                                        RoundingMode.HALF_UP
                                );
                    }


                    BigDecimal grossRefund =
                            unitPrice.multiply(
                                    BigDecimal.valueOf(
                                            item.getQuantity()
                                    )
                            );


                    BigDecimal itemDiscount =
                            grossRefund.multiply(
                                    discountRatio
                            );


                    BigDecimal itemRefund =
                            grossRefund.subtract(
                                    itemDiscount
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );


                    item.setRefundAmount(
                            itemRefund
                    );


                    totalRefund =
                            totalRefund.add(
                                    itemRefund
                            );
                }


                totalRefund =
                        totalRefund.setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


                returnTransaction.setRefundAmount(
                        totalRefund
                );


                // ------------------------------------------
                // CREATE RETURN HEADER
                // ------------------------------------------

                int returnId =
                        returnDAO.addReturn(
                                connection,
                                returnTransaction
                        );


                returnTransaction.setReturnId(
                        returnId
                );


                // ------------------------------------------
                // SAVE ITEMS + RESTORE STOCK
                // ------------------------------------------

                for (ReturnItem item : items) {

                    Object[] saleItem =
                            returnDAO.getSaleItemForReturn(
                                    connection,
                                    item.getSaleItemId()
                            );


                    int productId =
                            (Integer)
                            saleItem[2];


                    item.setReturnId(
                            returnId
                    );


                    returnDAO.addReturnItem(
                            connection,
                            item
                    );


                    returnDAO.restoreProductStock(
                            connection,
                            productId,
                            item.getQuantity()
                    );


                    returnDAO.addReturnStockTransaction(
                            connection,
                            productId,
                            item.getQuantity(),
                            returnId
                    );
                }


                // ------------------------------------------
                // UPDATE ORIGINAL SALE STATUS
                // ------------------------------------------

                boolean fullyReturned =
                        returnDAO.isSaleFullyReturned(
                                connection,
                                returnTransaction.getSaleId()
                        );


                String newStatus =
                        fullyReturned
                        ? "REFUNDED"
                        : "PARTIALLY_REFUNDED";


                returnDAO.updateSaleStatus(
                        connection,
                        returnTransaction.getSaleId(),
                        newStatus
                );


                // ------------------------------------------
                // EVERYTHING SUCCEEDED
                // ------------------------------------------

                connection.commit();


                return returnId;


            } catch (SQLException
                    | InvalidReturnException
                    | RuntimeException e) {

                connection.rollback();

                throw e;


            } finally {

                try {

                    connection.setAutoCommit(
                            true
                    );

                } catch (SQLException ignored) {
                }
            }
        }
    }


    private void validateBasicReturn(
            Return returnTransaction,
            List<ReturnItem> items
    ) throws InvalidReturnException {

        if (returnTransaction == null) {

            throw new InvalidReturnException(
                    "Return information is required."
            );
        }


        if (returnTransaction.getSaleId()
                <= 0) {

            throw new InvalidReturnException(
                    "A valid sale is required."
            );
        }


        if (items == null
                || items.isEmpty()) {

            throw new InvalidReturnException(
                    "Select at least one item "
                    + "to return."
            );
        }


        for (ReturnItem item : items) {

            if (item == null) {

                throw new InvalidReturnException(
                        "Invalid return item."
                );
            }


            if (item.getSaleItemId()
                    <= 0) {

                throw new InvalidReturnException(
                        "Invalid sale item."
                );
            }


            if (item.getQuantity()
                    <= 0) {

                throw new InvalidReturnException(
                        "Return quantity must "
                        + "be greater than zero."
                );
            }
        }
    }
}