/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */

import com.supermart.model.Supplier;
import com.supermart.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public List<Supplier> getAllSuppliers()
            throws SQLException {

        List<Supplier> suppliers =
                new ArrayList<>();

        String sql = """
                SELECT supplier_id, name
                FROM suppliers
                ORDER BY name
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

                Supplier supplier =
                        new Supplier(
                                resultSet.getInt("supplier_id"),
                                resultSet.getString("name")
                        );

                suppliers.add(supplier);
            }
        }

        return suppliers;
    }
}
