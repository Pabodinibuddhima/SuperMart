/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */
import com.supermart.model.Return;
import com.supermart.model.ReturnItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ReturnDAO {

    // ==========================================
    // CREATE RETURN
    // ==========================================

    public int addReturn(
            Connection connection,
            Return returnTransaction
    ) throws SQLException {

        String sql =
                """
                INSERT INTO returns
                    (sale_id, employee_id, refund_amount, reason)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            statement.setInt(
                    1,
                    returnTransaction.getSaleId()
            );

            if (returnTransaction.getEmployeeId() == null) {

                statement.setNull(
                        2,
                        java.sql.Types.INTEGER
                );

            } else {

                statement.setInt(
                        2,
                        returnTransaction.getEmployeeId()
                );
            }

            statement.setBigDecimal(
                    3,
                    returnTransaction.getRefundAmount()
            );

            statement.setString(
                    4,
                    returnTransaction.getReason()
            );


            int affectedRows =
                    statement.executeUpdate();


            if (affectedRows == 0) {

                throw new SQLException(
                        "Creating return failed. "
                        + "No database row was created."
                );
            }


            try (ResultSet generatedKeys =
                    statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    return generatedKeys.getInt(
                            1
                    );
                }
            }


            throw new SQLException(
                    "Creating return failed. "
                    + "No return ID was generated."
            );
        }
    }


    // ==========================================
    // CREATE RETURN ITEM
    // ==========================================

    public void addReturnItem(
            Connection connection,
            ReturnItem item
    ) throws SQLException {

        String sql =
                """
                INSERT INTO return_items
                    (return_id, sale_item_id,
                     quantity, refund_amount)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setInt(
                    1,
                    item.getReturnId()
            );

            statement.setInt(
                    2,
                    item.getSaleItemId()
            );

            statement.setInt(
                    3,
                    item.getQuantity()
            );

            statement.setBigDecimal(
                    4,
                    item.getRefundAmount()
            );


            statement.executeUpdate();
        }
    }


    // ==========================================
    // GET ORIGINAL SALE ITEM INFORMATION
    // ==========================================

    public Object[] getSaleItemForReturn(
            Connection connection,
            int saleItemId
    ) throws SQLException {

        String sql =
                """
                SELECT
                    si.sale_item_id,
                    si.sale_id,
                    si.product_id,
                    si.quantity,
                    si.unit_price,
                    s.subtotal,
                    s.discount,
                    s.total_amount,
                    s.status
                FROM sale_items si
                JOIN sales s
                    ON si.sale_id = s.sale_id
                WHERE si.sale_item_id = ?
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setInt(
                    1,
                    saleItemId
            );


            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Object[]{
                        resultSet.getInt(
                                "sale_item_id"
                        ),

                        resultSet.getInt(
                                "sale_id"
                        ),

                        resultSet.getInt(
                                "product_id"
                        ),

                        resultSet.getInt(
                                "quantity"
                        ),

                        resultSet.getBigDecimal(
                                "unit_price"
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
                                "status"
                        )
                    };
                }
            }
        }

        return null;
    }


    // ==========================================
    // GET QUANTITY ALREADY RETURNED
    // ==========================================

    public int getReturnedQuantity(
            Connection connection,
            int saleItemId
    ) throws SQLException {

        String sql =
                """
                SELECT
                    COALESCE(SUM(ri.quantity), 0)
                        AS returned_quantity
                FROM return_items ri
                WHERE ri.sale_item_id = ?
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setInt(
                    1,
                    saleItemId
            );


            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(
                            "returned_quantity"
                    );
                }
            }
        }

        return 0;
    }


    // ==========================================
    // RESTORE PRODUCT STOCK
    // ==========================================

    public void restoreProductStock(
            Connection connection,
            int productId,
            int quantity
    ) throws SQLException {

        String sql =
                """
                UPDATE products
                SET quantity = quantity + ?
                WHERE product_id = ?
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setInt(
                    1,
                    quantity
            );

            statement.setInt(
                    2,
                    productId
            );


            int affectedRows =
                    statement.executeUpdate();


            if (affectedRows != 1) {

                throw new SQLException(
                        "Unable to restore stock "
                        + "for product ID "
                        + productId
                        + "."
                );
            }
        }
    }


    // ==========================================
    // RECORD RETURN STOCK MOVEMENT
    // ==========================================

    public void addReturnStockTransaction(
            Connection connection,
            int productId,
            int quantity,
            int returnId
    ) throws SQLException {

        String sql =
                """
                INSERT INTO stock_transactions
                    (product_id,
                     transaction_type,
                     quantity,
                     reference_note)
                VALUES (?, 'RETURN', ?, ?)
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

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
                    "Return #"
                    + returnId
            );


            statement.executeUpdate();
        }
    }


    // ==========================================
    // CHECK WHETHER ENTIRE SALE IS RETURNED
    // ==========================================

    public boolean isSaleFullyReturned(
            Connection connection,
            int saleId
    ) throws SQLException {

        String sql =
                """
                SELECT
                    COALESCE(SUM(si.quantity), 0)
                        AS sold_quantity,

                    COALESCE((
                        SELECT SUM(ri.quantity)
                        FROM return_items ri
                        JOIN sale_items returned_si
                            ON ri.sale_item_id =
                               returned_si.sale_item_id
                        WHERE returned_si.sale_id = ?
                    ), 0)
                        AS returned_quantity

                FROM sale_items si
                WHERE si.sale_id = ?
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setInt(
                    1,
                    saleId
            );

            statement.setInt(
                    2,
                    saleId
            );


            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (resultSet.next()) {

                    int sold =
                            resultSet.getInt(
                                    "sold_quantity"
                            );

                    int returned =
                            resultSet.getInt(
                                    "returned_quantity"
                            );


                    return sold > 0
                            && returned >= sold;
                }
            }
        }

        return false;
    }


    // ==========================================
    // UPDATE ORIGINAL SALE STATUS
    // ==========================================

    public void updateSaleStatus(
            Connection connection,
            int saleId,
            String status
    ) throws SQLException {

        String sql =
                """
                UPDATE sales
                SET status = ?
                WHERE sale_id = ?
                """;

        try (PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setString(
                    1,
                    status
            );

            statement.setInt(
                    2,
                    saleId
            );


            if (statement.executeUpdate() != 1) {

                throw new SQLException(
                        "Unable to update sale status."
                );
            }
        }
    }

    public java.util.List<Object[]> getReturnableItems(
            int saleId
    ) throws SQLException {

        java.util.List<Object[]> items =
                new java.util.ArrayList<>();


        String sql =
                """
                SELECT
                    si.sale_item_id,
                    si.product_id,
                    p.name AS product_name,
                    si.quantity AS sold_quantity,
                    COALESCE(
                        SUM(ri.quantity),
                        0
                    ) AS returned_quantity,
                    si.unit_price
                FROM sale_items si

                JOIN products p
                    ON si.product_id =
                       p.product_id

                LEFT JOIN return_items ri
                    ON si.sale_item_id =
                       ri.sale_item_id

                WHERE si.sale_id = ?

                GROUP BY
                    si.sale_item_id,
                    si.product_id,
                    p.name,
                    si.quantity,
                    si.unit_price

                ORDER BY
                    si.sale_item_id
                """;


        try (java.sql.Connection connection =
                com.supermart.util.DBConnection
                        .getConnection();

             java.sql.PreparedStatement statement =
                connection.prepareStatement(
                        sql
                )) {

            statement.setInt(
                    1,
                    saleId
            );


            try (java.sql.ResultSet resultSet =
                    statement.executeQuery()) {

                while (resultSet.next()) {

                    int soldQuantity =
                            resultSet.getInt(
                                    "sold_quantity"
                            );

                    int returnedQuantity =
                            resultSet.getInt(
                                    "returned_quantity"
                            );


                    items.add(
                            new Object[]{
                                resultSet.getInt(
                                        "sale_item_id"
                                ),
                                resultSet.getInt(
                                        "product_id"
                                ),
                                resultSet.getString(
                                        "product_name"
                                ),
                                soldQuantity,
                                returnedQuantity,
                                soldQuantity
                                    - returnedQuantity,
                                resultSet.getBigDecimal(
                                        "unit_price"
                                )
                            }
                    );
                }
            }
        }
        
        return items;
    }
}