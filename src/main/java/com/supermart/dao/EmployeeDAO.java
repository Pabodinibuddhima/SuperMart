/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */
import com.supermart.model.Employee;
import com.supermart.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // ==========================================
    // GET ALL EMPLOYEES
    // ==========================================

    public List<Employee> getAllEmployees()
            throws SQLException {

        List<Employee> employees =
                new ArrayList<>();

        String sql = """
                SELECT
                    employee_id,
                    name,
                    phone,
                    email,
                    role,
                    status,
                    created_at
                FROM employees
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
                employees.add(
                        mapEmployee(resultSet)
                );
            }
        }

        return employees;
    }


    // ==========================================
    // ADD EMPLOYEE
    // ==========================================

    public boolean addEmployee(Employee employee)
            throws SQLException {

        String sql = """
                INSERT INTO employees (
                    name,
                    phone,
                    email,
                    role,
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
                    employee.getName()
            );

            statement.setString(
                    2,
                    emptyToNull(employee.getPhone())
            );

            statement.setString(
                    3,
                    emptyToNull(employee.getEmail())
            );

            statement.setString(
                    4,
                    employee.getRole()
            );

            statement.setString(
                    5,
                    employee.getStatus()
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // UPDATE EMPLOYEE
    // ==========================================

    public boolean updateEmployee(Employee employee)
            throws SQLException {

        String sql = """
                UPDATE employees
                SET
                    name = ?,
                    phone = ?,
                    email = ?,
                    role = ?
                WHERE employee_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    employee.getName()
            );

            statement.setString(
                    2,
                    emptyToNull(employee.getPhone())
            );

            statement.setString(
                    3,
                    emptyToNull(employee.getEmail())
            );

            statement.setString(
                    4,
                    employee.getRole()
            );

            statement.setInt(
                    5,
                    employee.getEmployeeId()
            );

            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // UPDATE EMPLOYEE STATUS
    // ==========================================

    public boolean updateEmployeeStatus(
            int employeeId,
            String status
    ) throws SQLException {

        String sql = """
                UPDATE employees
                SET status = ?
                WHERE employee_id = ?
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);
            statement.setInt(2, employeeId);

            return statement.executeUpdate() > 0;
        }
    }


    // ==========================================
    // CHECK EMAIL EXISTS
    // Used when adding an employee
    // ==========================================

    public boolean employeeEmailExists(String email)
            throws SQLException {

        if (email == null || email.isBlank()) {
            return false;
        }

        String sql = """
                SELECT 1
                FROM employees
                WHERE LOWER(TRIM(email))
                      = LOWER(TRIM(?))
                LIMIT 1
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {
                return resultSet.next();
            }
        }
    }


    // ==========================================
    // CHECK EMAIL EXISTS EXCEPT CURRENT EMPLOYEE
    // Used when editing an employee
    // ==========================================

    public boolean employeeEmailExistsExcept(
            String email,
            int employeeId
    ) throws SQLException {

        if (email == null || email.isBlank()) {
            return false;
        }

        String sql = """
                SELECT 1
                FROM employees
                WHERE LOWER(TRIM(email))
                      = LOWER(TRIM(?))
                  AND employee_id <> ?
                LIMIT 1
                """;

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);
            statement.setInt(2, employeeId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {
                return resultSet.next();
            }
        }
    }


    // ==========================================
    // MAP RESULTSET -> EMPLOYEE
    // ==========================================

    private Employee mapEmployee(ResultSet resultSet)
            throws SQLException {

        Employee employee =
                new Employee();

        employee.setEmployeeId(
                resultSet.getInt("employee_id")
        );

        employee.setName(
                resultSet.getString("name")
        );

        employee.setPhone(
                resultSet.getString("phone")
        );

        employee.setEmail(
                resultSet.getString("email")
        );

        employee.setRole(
                resultSet.getString("role")
        );

        employee.setStatus(
                resultSet.getString("status")
        );

        if (resultSet.getTimestamp("created_at")
                != null) {

            employee.setCreatedAt(
                    resultSet
                            .getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        return employee;
    }


    // ==========================================
    // EMPTY STRING -> NULL
    // ==========================================

    private String emptyToNull(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}