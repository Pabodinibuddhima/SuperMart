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


    // ==========================================
    // GET ALL SUPPLIERS
    // ==========================================

    public List<Supplier> getAllSuppliers()
            throws SQLException {

        List<Supplier> suppliers =
                new ArrayList<>();


        String sql = """
            SELECT
                supplier_id,
                name,
                phone,
                email,
                address,
                status,
                created_at
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

                suppliers.add(
                        mapSupplier(resultSet)
                );
            }
        }


        return suppliers;
    }


    // ==========================================
    // ADD SUPPLIER
    // ==========================================

    public boolean addSupplier(
            Supplier supplier
    ) throws SQLException {

        String sql = """
            INSERT INTO suppliers
            (
                name,
                phone,
                email,
                address,
                status
            )
            VALUES (?, ?, ?, ?, ?)
            """;


        try (
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    supplier.getName()
            );

            statement.setString(
                    2,
                    emptyToNull(
                            supplier.getPhone()
                    )
            );

            statement.setString(
                    3,
                    emptyToNull(
                            supplier.getEmail()
                    )
            );

            statement.setString(
                    4,
                    emptyToNull(
                            supplier.getAddress()
                    )
            );

            statement.setString(
                    5,
                    supplier.getStatus()
            );


            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // UPDATE SUPPLIER
    // ==========================================

    public boolean updateSupplier(
            Supplier supplier
    ) throws SQLException {

        String sql = """
            UPDATE suppliers
            SET
                name = ?,
                phone = ?,
                email = ?,
                address = ?
            WHERE supplier_id = ?
            """;


        try (
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    supplier.getName()
            );

            statement.setString(
                    2,
                    emptyToNull(
                            supplier.getPhone()
                    )
            );

            statement.setString(
                    3,
                    emptyToNull(
                            supplier.getEmail()
                    )
            );

            statement.setString(
                    4,
                    emptyToNull(
                            supplier.getAddress()
                    )
            );

            statement.setInt(
                    5,
                    supplier.getSupplierId()
            );


            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // ACTIVATE / DEACTIVATE SUPPLIER
    // ==========================================

    public boolean updateSupplierStatus(
            int supplierId,
            String status
    ) throws SQLException {

        String sql = """
            UPDATE suppliers
            SET status = ?
            WHERE supplier_id = ?
            """;


        try (
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status
            );

            statement.setInt(
                    2,
                    supplierId
            );


            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // CHECK DUPLICATE SUPPLIER NAME
    // ==========================================

    public boolean supplierNameExists(
            String name
    ) throws SQLException {

        String sql = """
            SELECT 1
            FROM suppliers
            WHERE LOWER(TRIM(name))
                  = LOWER(TRIM(?))
            LIMIT 1
            """;


        try (
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    name
            );


            try (
                ResultSet resultSet =
                        statement.executeQuery()
            ) {

                return resultSet.next();
            }
        }
    }
    
    
    // ==========================================
    // CHECK DUPLICATE SUPPLIER NAME
    // EXCLUDING CURRENT SUPPLIER
    // ==========================================

    public boolean supplierNameExistsExcept(
            String name,
            int supplierId
    ) throws SQLException {

        String sql = """
            SELECT 1
            FROM suppliers
            WHERE LOWER(TRIM(name))
                  = LOWER(TRIM(?))
              AND supplier_id <> ?
            LIMIT 1
            """;


        try (
            Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    name
            );

            statement.setInt(
                    2,
                    supplierId
            );


            try (
                ResultSet resultSet =
                        statement.executeQuery()
            ) {

                return resultSet.next();
            }
        }
    }
    // ==========================================
    // MAP DATABASE ROW → SUPPLIER OBJECT
    // ==========================================

    private Supplier mapSupplier(
            ResultSet resultSet
    ) throws SQLException {

        Supplier supplier =
                new Supplier();


        supplier.setSupplierId(
                resultSet.getInt(
                        "supplier_id"
                )
        );

        supplier.setName(
                resultSet.getString(
                        "name"
                )
        );

        supplier.setPhone(
                resultSet.getString(
                        "phone"
                )
        );

        supplier.setEmail(
                resultSet.getString(
                        "email"
                )
        );

        supplier.setAddress(
                resultSet.getString(
                        "address"
                )
        );

        supplier.setStatus(
                resultSet.getString(
                        "status"
                )
        );


        if (resultSet.getTimestamp(
                "created_at"
        ) != null) {

            supplier.setCreatedAt(
                    resultSet
                            .getTimestamp(
                                    "created_at"
                            )
                            .toLocalDateTime()
            );
        }


        return supplier;
    }


    // ==========================================
    // CONVERT BLANK TEXT TO SQL NULL
    // ==========================================

    private String emptyToNull(
            String value
    ) {

        if (value == null
                || value.trim().isEmpty()) {

            return null;
        }


        return value.trim();
    }
}