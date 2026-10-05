/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */


import com.supermart.model.Product;
import com.supermart.model.StockTransaction;
import com.supermart.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // =========================================================
    // READ CURRENT INVENTORY
    // =========================================================

    public List<Product> getInventory()
            throws SQLException {

        List<Product> products =
                new ArrayList<>();
        
        String sql = """
        SELECT
            p.product_id,
            p.barcode,
            p.name,
            p.category_id,
            p.supplier_id,
            p.cost_price,
            p.selling_price,
            p.quantity,
            p.reorder_level,
            p.status
        FROM products p
        ORDER BY p.name
        """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Product product =
                        new Product();

                product.setProductId(
                        resultSet.getInt(
                                "product_id"
                        )
                );

                product.setBarcode(
                        resultSet.getString(
                                "barcode"
                        )
                );

                product.setName(
                        resultSet.getString(
                                "name"
                        )
                );

                product.setCategoryId(
                        resultSet.getInt(
                                "category_id"
                        )
                );

                int supplierId =
                        resultSet.getInt(
                                "supplier_id"
                        );

                if (resultSet.wasNull()) {

                    product.setSupplierId(null);

                } else {

                    product.setSupplierId(
                            supplierId
                    );
                }

                product.setCostPrice(
                        resultSet.getBigDecimal(
                                "cost_price"
                        )
                );

                product.setSellingPrice(
                        resultSet.getBigDecimal(
                                "selling_price"
                        )
                );

                product.setQuantity(
                        resultSet.getInt(
                                "quantity"
                        )
                );

                product.setReorderLevel(
                        resultSet.getInt(
                                "reorder_level"
                        )
                );

                product.setStatus(
                        resultSet.getString(
                                "status"
                        )
                );

                products.add(product);
            }
        }

        return products;
    }
    // =========================================================
    // ADD STOCK
    // =========================================================

    public boolean addStock(
            int productId,
            int quantity,
            String referenceNote
    ) throws SQLException {

        String updateProductSql = """
            UPDATE products
            SET quantity = quantity + ?
            WHERE product_id = ?
              AND status = 'ACTIVE'
            """;

        String insertTransactionSql = """
            INSERT INTO stock_transactions
                (
                    product_id,
                    transaction_type,
                    quantity,
                    reference_note
                )
            VALUES
                (?, 'STOCK_IN', ?, ?)
            """;

        Connection connection = null;

        try {

            connection =
                    DBConnection.getConnection();

            /*
             * By default JDBC normally commits each
             * SQL statement automatically.
             *
             * We disable that because these TWO
             * operations must behave as ONE operation.
             */
            connection.setAutoCommit(false);


            // =============================================
            // 1. Increase products.quantity
            // =============================================

            try (
                    PreparedStatement updateStatement =
                            connection.prepareStatement(
                                    updateProductSql
                            )
            ) {

                updateStatement.setInt(
                        1,
                        quantity
                );

                updateStatement.setInt(
                        2,
                        productId
                );

                int affectedRows =
                        updateStatement.executeUpdate();

                if (affectedRows != 1) {

                    throw new SQLException(
                            "Product could not be updated."
                    );
                }
            }


            // =============================================
            // 2. Record stock transaction
            // =============================================

            try (
                    PreparedStatement transactionStatement =
                            connection.prepareStatement(
                                    insertTransactionSql
                            )
            ) {

                transactionStatement.setInt(
                        1,
                        productId
                );

                transactionStatement.setInt(
                        2,
                        quantity
                );

                transactionStatement.setString(
                        3,
                        referenceNote
                );

                transactionStatement.executeUpdate();
            }


            // =============================================
            // BOTH operations succeeded
            // =============================================

            connection.commit();

            return true;


        } catch (SQLException e) {

            // =============================================
            // Something failed → undo everything
            // =============================================

            if (connection != null) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }
            }

            throw e;


        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);

                } catch (SQLException ignored) {
                }

                try {

                    connection.close();

                } catch (SQLException ignored) {
                }
            }
        }
    }
    
    public List<StockTransaction> getStockHistory()
            throws SQLException {

        List<StockTransaction> transactions =
                new ArrayList<>();

        String sql = """
            SELECT
                st.transaction_id,
                st.product_id,
                p.name AS product_name,
                st.transaction_type,
                st.quantity,
                st.reference_note,
                st.created_at
            FROM stock_transactions st
            JOIN products p
                ON st.product_id = p.product_id
            ORDER BY st.created_at DESC,
                     st.transaction_id DESC
            """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                StockTransaction transaction =
                        new StockTransaction();

                transaction.setTransactionId(
                        resultSet.getInt("transaction_id")
                );

                transaction.setProductId(
                        resultSet.getInt("product_id")
                );

                transaction.setProductName(
                        resultSet.getString("product_name")
                );

                transaction.setTransactionType(
                        resultSet.getString("transaction_type")
                );

                transaction.setQuantity(
                        resultSet.getInt("quantity")
                );

                transaction.setReferenceNote(
                        resultSet.getString("reference_note")
                );

                transaction.setCreatedAt(
                        resultSet
                                .getTimestamp("created_at")
                                .toLocalDateTime()
                );

                transactions.add(transaction);
            }
        }

        return transactions;
    }   
    
}
