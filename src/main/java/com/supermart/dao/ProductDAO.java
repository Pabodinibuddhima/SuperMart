/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

import com.supermart.model.Product;
import com.supermart.util.DBConnection;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() throws SQLException {

        List<Product> products = new ArrayList<>();

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
                ORDER BY name
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Product product = new Product();

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
    
    public boolean addProduct(Product product) throws SQLException {
        /*
        String sql = """
            INSERT INTO products
            (
                barcode,
                name,
                category_id,
                supplier_id,
                cost_price,
                selling_price,
                quantity,
                reorder_level,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """; */
        String sql = """
            UPDATE products
            SET barcode = ?,
                name = ?,
                category_id = ?,
                supplier_id = ?,
                cost_price = ?,
                selling_price = ?,
                quantity = ?,
                reorder_level = ?,
                status = ?
            WHERE product_id = ?
            """;
        

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, product.getBarcode());
            statement.setString(2, product.getName());
            statement.setInt(3, product.getCategoryId());

            
            if (product.getSupplierId() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, product.getSupplierId());
            }

            statement.setBigDecimal(5, product.getCostPrice());
            statement.setBigDecimal(6, product.getSellingPrice());
            statement.setInt(7, product.getQuantity());
            statement.setInt(8, product.getReorderLevel());
            statement.setString(9, product.getStatus());

            return statement.executeUpdate() > 0;
        }
    }
    
    public boolean updateProduct(Product product) throws SQLException {

        String sql = """
            UPDATE products
            SET barcode = ?,
                name = ?,
                category_id = ?,
                supplier_id = ?,
                cost_price = ?,
                selling_price = ?,
                quantity = ?,
                reorder_level = ?,
                status = ?
            WHERE product_id = ?
            """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, product.getBarcode());
            statement.setString(2, product.getName());
            statement.setInt(3, product.getCategoryId());

            if (product.getSupplierId() == null) {
                statement.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            } else {
                statement.setInt(
                        4,
                        product.getSupplierId()
                );
            }

            statement.setBigDecimal(
                    5,
                    product.getCostPrice()
            );

            statement.setBigDecimal(
                    6,
                    product.getSellingPrice()
            );

            statement.setInt(
                    7,
                    product.getQuantity()
            );

            statement.setInt(
                    8,
                    product.getReorderLevel()
            );

            statement.setString(
                    9,
                    product.getStatus()
            );

            statement.setInt(
                    10,
                    product.getProductId()
            );

            return statement.executeUpdate() > 0;
        }
    }
    
    public boolean deactivateProduct(int productId)
        throws SQLException {

        String sql = """
            UPDATE products
            SET status = 'INACTIVE'
            WHERE product_id = ?
            """;

        try (
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setInt(1, productId);

            return statement.executeUpdate() > 0;
        }
    }

    

    
}