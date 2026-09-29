/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.dao;

/**
 *
 * @author pabodini
 */

import com.supermart.model.Category;
import com.supermart.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getAllCategories()
            throws SQLException {

        List<Category> categories =
                new ArrayList<>();

        String sql = """
                SELECT category_id, name
                FROM categories
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

                Category category =
                        new Category(
                                resultSet.getInt("category_id"),
                                resultSet.getString("name")
                        );

                categories.add(category);
            }
        }

        return categories;
    }
}
