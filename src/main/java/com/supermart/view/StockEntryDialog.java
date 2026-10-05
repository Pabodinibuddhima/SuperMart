/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */


import com.supermart.dao.InventoryDAO;
import com.supermart.dao.ProductDAO;
import com.supermart.model.Product;
import com.supermart.view.component.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StockEntryDialog extends JDialog {

    private final JComboBox<Product> productComboBox =
            new JComboBox<>();
    
    private final PlaceholderTextField quantityField =
            new PlaceholderTextField(
                    "e.g. 20",
                    20
            );

    private final PlaceholderTextField referenceField =
            new PlaceholderTextField(
                    "e.g. Delivery INV-1024",
                    20
            );



    
    private final InventoryDAO inventoryDAO =
            new InventoryDAO();

    private boolean stockAdded = false;

    public StockEntryDialog(JFrame parent) {

        super(parent, "Stock Entry", true);
        
        
        productComboBox.setToolTipText(
                "Select the product receiving new stock"
        );

        quantityField.setToolTipText(
                "Enter the number of units received"
        );

        referenceField.setToolTipText(
                "Optional reference such as a supplier invoice, delivery number or note"
        );
        

        setSize(500, 360);
        setLocationRelativeTo(parent);
        setResizable(false);

        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        loadProducts();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 30, 15, 30
                )
        );

        JLabel title =
                new JLabel("Stock Entry");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Record newly received inventory."
                );

        subtitle.setForeground(
                new Color(110, 113, 120)
        );

        title.setAlignmentX(LEFT_ALIGNMENT);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);

        return panel;
    }

    private JPanel createForm() {

        JPanel form =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                12,
                                15
                        )
                );

        form.setBackground(Color.WHITE);

        form.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 30, 20, 30
                )
        );

        form.add(
                new JLabel("Product")
        );

        form.add(productComboBox);

        form.add(
                new JLabel("Quantity Received")
        );

        form.add(quantityField);

        form.add(
                new JLabel("Reference / Note")
        );

        form.add(referenceField);

        return form;
    }

    private JPanel createButtons() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 30, 20, 30
                )
        );

        JButton cancelButton =
                new JButton("Cancel");

        JButton saveButton =
                new JButton("Add Stock");

        saveButton.setBackground(
                new Color(25, 25, 28)
        );

        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        cancelButton.addActionListener(
                e -> dispose()
        );

        saveButton.addActionListener(
                e -> saveStock()
        );

        panel.add(cancelButton);
        panel.add(saveButton);

        return panel;
    }

    private void loadProducts() {

        ProductDAO productDAO =
                new ProductDAO();

        try {

            for (Product product
                    : productDAO.getAllProducts()) {

                if ("ACTIVE".equals(
                        product.getStatus()
                )) {

                    productComboBox.addItem(product);
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load products.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveStock() {

        Product selectedProduct =
                (Product)
                        productComboBox
                                .getSelectedItem();

        if (selectedProduct == null) {

            showValidationError(
                    "Please select a product."
            );

            return;
        }

        if (quantityField
                .getText()
                .trim()
                .isEmpty()) {

            showValidationError(
                    "Quantity is required."
            );

            return;
        }

        try {

            int quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            if (quantity <= 0) {

                showValidationError(
                        "Quantity must be greater than zero."
                );

                return;
            }

            String referenceNote =
                    referenceField
                            .getText()
                            .trim();

            boolean saved =
                    inventoryDAO.addStock(
                            selectedProduct
                                    .getProductId(),
                            quantity,
                            referenceNote
                    );

            if (saved) {

                stockAdded = true;

                JOptionPane.showMessageDialog(
                        this,
                        "Stock added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
            }

        } catch (NumberFormatException e) {

            showValidationError(
                    "Quantity must be a valid whole number."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to add stock.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showValidationError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public boolean isStockAdded() {
        return stockAdded;
    }
}