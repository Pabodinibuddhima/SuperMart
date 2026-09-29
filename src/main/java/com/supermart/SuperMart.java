/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
/*
package com.supermart;

import com.supermart.util.DBConnection;
import java.sql.Connection;

public class SuperMart {

    public static void main(String[] args) {

        System.out.println("Starting SuperMart...");

        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                System.out.println("MySQL connection successful!");
            }

        } catch (Exception e) {

            System.out.println("MySQL connection failed!");
            System.out.println(e.getMessage());
        }
    }
}

*/

package com.supermart;

import com.supermart.dao.ProductDAO;
import com.supermart.model.Product;

public class SuperMart {

    public static void main(String[] args) {

        System.out.println("Starting SuperMart...");

        ProductDAO productDAO = new ProductDAO();

        try {

            for (Product product : productDAO.getAllProducts()) {

                System.out.println(
                        product.getName()
                        + " | Rs. "
                        + product.getSellingPrice()
                        + " | Stock: "
                        + product.getQuantity()
                        + " | Low Stock: "
                        + product.isLowStock()
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Unable to load products: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}