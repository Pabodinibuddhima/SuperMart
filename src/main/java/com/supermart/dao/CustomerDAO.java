/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */

import com.supermart.model.Customer;
import com.supermart.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // ==========================================
    // READ
    // ==========================================

    public List<Customer> getAllCustomers()
            throws SQLException {

        List<Customer> customers =
                new ArrayList<>();

        String sql = """
                SELECT
                    customer_id,
                    name,
                    phone,
                    email,
                    address,
                    status,
                    created_at
                FROM customers
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

                Customer customer =
                        mapCustomer(resultSet);

                customers.add(customer);
            }
        }

        return customers;
    }


    // ==========================================
    // CREATE
    // ==========================================

    public boolean addCustomer(Customer customer)
            throws SQLException {

        String sql = """
                INSERT INTO customers
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
                    customer.getName()
            );

            statement.setString(
                    2,
                    emptyToNull(customer.getPhone())
            );

            statement.setString(
                    3,
                    emptyToNull(customer.getEmail())
            );

            statement.setString(
                    4,
                    emptyToNull(customer.getAddress())
            );

            statement.setString(
                    5,
                    customer.getStatus()
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // UPDATE
    // ==========================================

    public boolean updateCustomer(
            Customer customer
    ) throws SQLException {

        String sql = """
                UPDATE customers
                SET
                    name = ?,
                    phone = ?,
                    email = ?,
                    address = ?
                WHERE customer_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    customer.getName()
            );

            statement.setString(
                    2,
                    emptyToNull(customer.getPhone())
            );

            statement.setString(
                    3,
                    emptyToNull(customer.getEmail())
            );

            statement.setString(
                    4,
                    emptyToNull(customer.getAddress())
            );

            statement.setInt(
                    5,
                    customer.getCustomerId()
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // STATUS
    // ==========================================

    public boolean updateCustomerStatus(
            int customerId,
            String status
    ) throws SQLException {

        String sql = """
                UPDATE customers
                SET status = ?
                WHERE customer_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);
            statement.setInt(2, customerId);

            return statement.executeUpdate() > 0;
        }
    }
    

    
    public boolean customerNameExists(String name)
            throws SQLException {

        String sql = """
            SELECT 1
            FROM customers
            WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))
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
    // RESULTSET -> CUSTOMER
    // ==========================================

    private Customer mapCustomer(
            ResultSet resultSet
    ) throws SQLException {

        Customer customer =
                new Customer();

        customer.setCustomerId(
                resultSet.getInt("customer_id")
        );

        customer.setName(
                resultSet.getString("name")
        );

        customer.setPhone(
                resultSet.getString("phone")
        );

        customer.setEmail(
                resultSet.getString("email")
        );

        customer.setAddress(
                resultSet.getString("address")
        );

        customer.setStatus(
                resultSet.getString("status")
        );

        if (resultSet.getTimestamp("created_at")
                != null) {

            customer.setCreatedAt(
                    resultSet
                            .getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        return customer;
    }

    // ==========================================
    // EMPTY STRING -> SQL NULL
    // ==========================================

    private String emptyToNull(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return null;
        }

        return value.trim();
    }
}