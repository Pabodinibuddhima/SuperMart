/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */
import com.supermart.model.Payment;
import com.supermart.model.Sale;
import com.supermart.model.SaleItem;
import com.supermart.util.DBConnection;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SaleDAO {


    // ==========================================
    // INSERT SALE
    // ==========================================

    public int addSale(
            Connection connection,
            Sale sale
    ) throws SQLException {

        String sql =
                "INSERT INTO sales "
                + "(invoice_number, customer_id, employee_id, "
                + "subtotal, discount, total_amount, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {


            statement.setString(
                    1,
                    sale.getInvoiceNumber()
            );


            if (sale.getCustomerId() == null) {

                statement.setNull(
                        2,
                        java.sql.Types.INTEGER
                );

            } else {

                statement.setInt(
                        2,
                        sale.getCustomerId()
                );
            }


            if (sale.getEmployeeId() == null) {

                statement.setNull(
                        3,
                        java.sql.Types.INTEGER
                );

            } else {

                statement.setInt(
                        3,
                        sale.getEmployeeId()
                );
            }


            statement.setBigDecimal(
                    4,
                    sale.getSubtotal()
            );

            statement.setBigDecimal(
                    5,
                    sale.getDiscount()
            );

            statement.setBigDecimal(
                    6,
                    sale.getTotalAmount()
            );

            statement.setString(
                    7,
                    sale.getStatus()
            );


            int affectedRows =
                    statement.executeUpdate();


            if (affectedRows == 0) {

                throw new SQLException(
                        "Creating sale failed. "
                        + "No database row was created."
                );
            }


            try (ResultSet generatedKeys =
                    statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    return generatedKeys.getInt(1);
                }
            }


            throw new SQLException(
                    "Creating sale failed. "
                    + "No sale ID was returned."
            );
        }
    }


    // ==========================================
    // INSERT SALE ITEM
    // ==========================================
    
    public void addSaleItem(
            Connection connection,
            SaleItem item
    ) throws SQLException {

        String sql =
                "INSERT INTO sale_items "
                + "(sale_id, product_id, quantity, "
                + "unit_price, unit_cost, line_total) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    item.getSaleId()
            );

            statement.setInt(
                    2,
                    item.getProductId()
            );

            statement.setInt(
                    3,
                    item.getQuantity()
            );

            statement.setBigDecimal(
                    4,
                    item.getUnitPrice()
            );

            statement.setBigDecimal(
                    5,
                    item.getUnitCost()
            );

            statement.setBigDecimal(
                    6,
                    item.getLineTotal()
            );

            statement.executeUpdate();
        }
    }

    
    
    

    // ==========================================
    // DEDUCT PRODUCT STOCK SAFELY
    // ==========================================

    public boolean deductProductStock(
            Connection connection,
            int productId,
            int quantity
    ) throws SQLException {

        String sql =
                "UPDATE products "
                + "SET quantity = quantity - ? "
                + "WHERE product_id = ? "
                + "AND status = 'ACTIVE' "
                + "AND quantity >= ?";


        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    quantity
            );

            statement.setInt(
                    2,
                    productId
            );

            statement.setInt(
                    3,
                    quantity
            );


            return statement.executeUpdate() == 1;
        }
    }


    // ==========================================
    // RECORD STOCK TRANSACTION
    // ==========================================

    public void addSaleStockTransaction(
            Connection connection,
            int productId,
            int quantity,
            String invoiceNumber
    ) throws SQLException {

        String sql =
                "INSERT INTO stock_transactions "
                + "(product_id, transaction_type, "
                + "quantity, reference_note) "
                + "VALUES (?, 'SALE', ?, ?)";


        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    productId
            );

            statement.setInt(
                    2,
                    quantity
            );

            statement.setString(
                    3,
                    invoiceNumber
            );


            statement.executeUpdate();
        }
    }


    // ==========================================
    // INSERT PAYMENT
    // ==========================================

    public void addPayment(
            Connection connection,
            Payment payment
    ) throws SQLException {

        String sql =
                "INSERT INTO payments "
                + "(sale_id, payment_method, amount) "
                + "VALUES (?, ?, ?)";


        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    payment.getSaleId()
            );

            statement.setString(
                    2,
                    payment.getPaymentMethod()
            );

            statement.setBigDecimal(
                    3,
                    payment.getAmount()
            );


            statement.executeUpdate();
        }
    }
    
    public List<Object[]> getSalesHistory()
            throws SQLException {

        List<Object[]> sales =
                new ArrayList<>();

        String sql =
                """
                SELECT
                    s.sale_id,
                    s.invoice_number,
                    s.created_at,
                    COALESCE(c.name, 'Walk-in Customer')
                        AS customer_name,
                    COALESCE(e.name, '-')
                        AS employee_name,
                                                 
                    s.subtotal,
                    s.discount,
                    s.total_amount,
                    COALESCE(p.payment_method, '-')
                        AS payment_method,
                    s.status
                                
                               
                FROM sales s
                LEFT JOIN customers c
                    ON s.customer_id = c.customer_id
                LEFT JOIN employees e
                    ON s.employee_id = e.employee_id
                LEFT JOIN payments p
                    ON s.sale_id = p.sale_id
                ORDER BY s.created_at DESC,
                         s.sale_id DESC
                """;

        try (Connection connection =
                DBConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(sql);

             ResultSet resultSet =
                statement.executeQuery()) {

            while (resultSet.next()) {

                sales.add(
                        new Object[]{
                            resultSet.getInt(
                                    "sale_id"
                            ),

                            resultSet.getString(
                                    "invoice_number"
                            ),

                            resultSet.getTimestamp(
                                    "created_at"
                            ),

                            resultSet.getString(
                                    "customer_name"
                            ),

                            resultSet.getString(
                                    "employee_name"
                            ),

                            resultSet.getBigDecimal(
                                    "subtotal"
                            ),

                            resultSet.getBigDecimal(
                                    "discount"
                            ),

                            resultSet.getBigDecimal(
                                    "total_amount"
                            ),

                            resultSet.getString(
                                    "payment_method"
                            ),

                            resultSet.getString(
                                    "status"
                            )
                        }
                );
            }
        }

        return sales;
    }
    
    
    
    
    public List<Object[]> getSaleItemsBySaleId(
            int saleId
    ) throws SQLException {

        List<Object[]> items =
                new ArrayList<>();

        String sql =
                """
                SELECT
                    si.product_id,
                    p.name AS product_name,
                    si.quantity,
                    si.unit_price,
                    si.unit_cost,
                    si.line_total
                FROM sale_items si
                JOIN products p
                    ON si.product_id = p.product_id
                WHERE si.sale_id = ?
                ORDER BY si.sale_item_id
                """;

        try (Connection connection =
                DBConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    saleId
            );

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                while (resultSet.next()) {

                    items.add(
                            new Object[]{
                                resultSet.getInt(
                                        "product_id"
                                ),

                                resultSet.getString(
                                        "product_name"
                                ),

                                resultSet.getInt(
                                        "quantity"
                                ),

                                resultSet.getBigDecimal(
                                        "unit_price"
                                ),

                                resultSet.getBigDecimal(
                                        "unit_cost"
                                ),

                                resultSet.getBigDecimal(
                                        "line_total"
                                )
                            }
                    );
                }
            }
        }

        return items;
    }
    
    
    
    public Object[] getSaleSummaryById(
            int saleId
    ) throws SQLException {

        String sql =
                """
                SELECT
                    s.subtotal,
                    s.discount,
                    s.total_amount
                FROM sales s
                WHERE s.sale_id = ?
                """;

        try (Connection connection =
                DBConnection.getConnection();

             PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    saleId
            );

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Object[]{
                        resultSet.getBigDecimal(
                                "subtotal"
                        ),

                        resultSet.getBigDecimal(
                                "discount"
                        ),

                        resultSet.getBigDecimal(
                                "total_amount"
                        )
                    };
                }
            }
        }

        return null;
    }
    
}