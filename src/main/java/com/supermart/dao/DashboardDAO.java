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
import com.supermart.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    public int getActiveProductCount()
            throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total
                FROM products
                WHERE status = 'ACTIVE'
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;
        }
    }


    public int getLowStockCount()
            throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total
                FROM products                                  
                WHERE quantity > 0
                AND quantity <= reorder_level
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;
        }
    }


    public List<Product> getLowStockProducts()
            throws SQLException {

        List<Product> products =
                new ArrayList<>();

        String sql = """
                SELECT
                    product_id,
                    barcode,
                    name,
                    category_id,
                    supplier_id,
                    cost_price,
                    selling_price,
                    quantity,
                    reorder_level,
                    status
                     
                FROM products
                WHERE quantity > 0
                AND quantity <= reorder_level
                
                ORDER BY quantity ASC, name ASC
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
                        resultSet.getInt("product_id")
                );

                product.setBarcode(
                        resultSet.getString("barcode")
                );

                product.setName(
                        resultSet.getString("name")
                );

                product.setCategoryId(
                        resultSet.getInt("category_id")
                );

                int supplierId =
                        resultSet.getInt("supplier_id");

                if (resultSet.wasNull()) {
                    product.setSupplierId(null);
                } else {
                    product.setSupplierId(supplierId);
                }

                product.setCostPrice(
                        resultSet.getBigDecimal("cost_price")
                );

                product.setSellingPrice(
                        resultSet.getBigDecimal(
                                "selling_price"
                        )
                );

                product.setQuantity(
                        resultSet.getInt("quantity")
                );

                product.setReorderLevel(
                        resultSet.getInt("reorder_level")
                );

                product.setStatus(
                        resultSet.getString("status")
                );

                products.add(product);
            }
        }

        return products;
    }
    
    
    
    
    
    public int getOutOfStockCount()
            throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total
                FROM products
                WHERE quantity <= 0
                AND status = 'ACTIVE'
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;
        }
    }

    
    public java.math.BigDecimal getTodaySalesTotal()
            throws SQLException {

        String sql = """
                SELECT
                    COALESCE(
                        (
                            SELECT SUM(s.total_amount)
                            FROM sales s
                            WHERE DATE(s.created_at) = CURDATE()
                        ),
                        0
                    )
                    -
                    COALESCE(
                        (
                            SELECT SUM(r.refund_amount)
                            FROM returns r
                            WHERE DATE(r.created_at) = CURDATE()
                        ),
                        0
                    ) AS total
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getBigDecimal("total");
            }

            return java.math.BigDecimal.ZERO;
        }
    }
    


    public int getTodayTransactionCount()
            throws SQLException {

        String sql = """
                SELECT COUNT(*) AS total
                FROM sales
                WHERE DATE(created_at) = CURDATE()
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;
        }
    }


    public List<String> getRecentSales()
            throws SQLException {

        List<String> recentSales =
                new ArrayList<>();

        String sql = """
                SELECT
                    s.invoice_number,
                    COALESCE(c.name, 'Walk-in Customer')
                        AS customer_name,
                    s.total_amount,
                    s.status
                FROM sales s
                LEFT JOIN customers c
                    ON s.customer_id = c.customer_id
                ORDER BY s.created_at DESC
                LIMIT 5
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

                String sale =
                        resultSet.getString("invoice_number")
                        + "   •   "
                        + resultSet.getString("customer_name")
                        + "   •   Rs. "
                        + resultSet
                                .getBigDecimal("total_amount")
                                .setScale(
                                        2,
                                        java.math.RoundingMode.HALF_UP
                                )
                        + "   •   "
                        + resultSet.getString("status");

                recentSales.add(sale);
            }
        }

        return recentSales;
    }
    
    
    
    
    
    public List<Product> getOutOfStockProducts()
            throws SQLException {

        List<Product> products =
                new ArrayList<>();

        String sql = """
                SELECT
                    product_id,
                    barcode,
                    name,
                    category_id,
                    supplier_id,
                    cost_price,
                    selling_price,
                    quantity,
                    reorder_level,
                    status
                FROM products
                WHERE quantity <= 0
                AND status = 'ACTIVE'
                ORDER BY name ASC
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
                        resultSet.getInt("product_id")
                );

                product.setBarcode(
                        resultSet.getString("barcode")
                );

                product.setName(
                        resultSet.getString("name")
                );

                product.setCategoryId(
                        resultSet.getInt("category_id")
                );

                int supplierId =
                        resultSet.getInt("supplier_id");

                if (resultSet.wasNull()) {
                    product.setSupplierId(null);
                } else {
                    product.setSupplierId(supplierId);
                }

                product.setCostPrice(
                        resultSet.getBigDecimal("cost_price")
                );

                product.setSellingPrice(
                        resultSet.getBigDecimal("selling_price")
                );

                product.setQuantity(
                        resultSet.getInt("quantity")
                );

                product.setReorderLevel(
                        resultSet.getInt("reorder_level")
                );

                product.setStatus(
                        resultSet.getString("status")
                );

                products.add(product);
            }
        }

        return products;
    }
    
}
