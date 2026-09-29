/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

import com.supermart.dao.ProductDAO;
import com.supermart.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author pabodini
 */



public class ProductsPanel extends JPanel {

    private static final Color BACKGROUND =
            new Color(247, 248, 250);

    private static final Color CARD_BACKGROUND =
            Color.WHITE;

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color BORDER =
            new Color(230, 232, 235);

    private final ProductDAO productDAO;

    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductsPanel() {

        productDAO = new ProductDAO();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        38, 42, 38, 42
                )
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createTableSection(), BorderLayout.CENTER);

        loadProducts();
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 28, 0
                )
        );

        // ---------- Title area ----------
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel("Products");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(TEXT_PRIMARY);

        JLabel subtitle =
                new JLabel(
                        "Manage products, prices and inventory information."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(TEXT_SECONDARY);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(subtitle);

        // ---------- Add Product button ----------
        JButton addProductButton =
                new JButton("+  Add Product");

        addProductButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        addProductButton.setForeground(Color.WHITE);
        addProductButton.setBackground(
                new Color(25, 25, 28)
        );

        addProductButton.setFocusPainted(false);

        addProductButton.setBorder(
                BorderFactory.createEmptyBorder(
                        11, 18, 11, 18
                )
        );

        addProductButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
        
        addProductButton.addActionListener(e -> {

            JFrame parentFrame =
                    (JFrame) SwingUtilities.getWindowAncestor(this);

            AddProductDialog dialog =
                    new AddProductDialog(parentFrame);

            dialog.setVisible(true);

            if (dialog.isProductAdded()) {
                loadProducts();
            }
        });
        
        
        
        //
        
        JButton deactivateProductButton =
                new JButton("Deactivate");

        deactivateProductButton.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        deactivateProductButton.setFocusPainted(false);

        deactivateProductButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
        
        
        
        deactivateProductButton.addActionListener(e -> {

        int selectedRow =
                productTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a product to deactivate.",
                    "No Product Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int productId =
                (int) tableModel.getValueAt(
                        selectedRow,
                        0
                );

        String productName =
                tableModel.getValueAt(
                        selectedRow,
                        2
                ).toString();

        String currentStatus =
                tableModel.getValueAt(
                        selectedRow,
                        7
                ).toString();

        if ("INACTIVE".equals(currentStatus)) {

            JOptionPane.showMessageDialog(
                    this,
                    productName + " is already inactive.",
                    "Already Inactive",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deactivate \"" + productName + "\"?\n\n"
                                + "The product will remain in the database "
                                + "but will no longer be active.",
                        "Confirm Deactivation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean deactivated =
                    productDAO.deactivateProduct(
                            productId
                    );

            if (deactivated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product deactivated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadProducts();
            }

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to deactivate product.\n"
                            + exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        });

        
        
        
        
        
        
        
        
        
        
        
        
        //
        
        
        
        JButton editProductButton =
                new JButton("Edit Product");

        editProductButton.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );
        
        
        

        editProductButton.setFocusPainted(false);

        editProductButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
        
        
        
        
        
        
        
        
        
        
        
        editProductButton.addActionListener(e -> {

            int selectedRow =
                    productTable.getSelectedRow();

            if (selectedRow == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a product to edit.",
                        "No Product Selected",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int productId =
                    (int) tableModel.getValueAt(
                            selectedRow,
                            0
                    );

            Product selectedProduct = null;

            try {

                for (Product product
                        : productDAO.getAllProducts()) {

                    if (product.getProductId()
                            == productId) {

                        selectedProduct = product;
                        break;
                    }
                }

            } catch (SQLException exception) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to load product.\n"
                                + exception.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (selectedProduct == null) {
                return;
            }

            JFrame parentFrame =
                    (JFrame)
                            SwingUtilities
                                    .getWindowAncestor(this);

            AddProductDialog dialog =
                    new AddProductDialog(
                            parentFrame,
                            selectedProduct
                    );

            dialog.setVisible(true);

            if (dialog.isProductUpdated()) {
                loadProducts();
            }
        });
        
        
        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        actionPanel.setOpaque(false);
        
        
        actionPanel.add(deactivateProductButton);
        actionPanel.add(editProductButton);
        actionPanel.add(addProductButton);

        
        
        
        
        
        
        
        
        
        
        
        

        header.add(titlePanel, BorderLayout.WEST);
        //header.add(addProductButton, BorderLayout.EAST);
        header.add(actionPanel,BorderLayout.EAST);
        return header;
    }

    private JPanel createTableSection() {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(CARD_BACKGROUND);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                18, 18, 18, 18
                        )
                )
        );

        // ---------- Table model ----------
        String[] columns = {
                "ID",
                "Barcode",
                "Product",
                "Cost Price",
                "Selling Price",
                "Stock",
                "Reorder Level",
                "Status"
        };

        tableModel = new DefaultTableModel(
                columns,
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        productTable = new JTable(tableModel);

        productTable.setRowHeight(42);
        productTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        productTable.setForeground(TEXT_PRIMARY);
        productTable.setBackground(Color.WHITE);

        productTable.setGridColor(
                new Color(240, 241, 243)
        );

        productTable.setShowVerticalLines(false);
        productTable.setShowHorizontalLines(true);

        productTable.setSelectionBackground(
                new Color(239, 240, 243)
        );

        productTable.setSelectionForeground(
                TEXT_PRIMARY
        );

        JTableHeader tableHeader =
                productTable.getTableHeader();

        tableHeader.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        tableHeader.setForeground(TEXT_SECONDARY);
        tableHeader.setBackground(
                new Color(250, 250, 251)
        );

        tableHeader.setPreferredSize(
                new Dimension(
                        tableHeader.getPreferredSize().width,
                        42
                )
        );

        productTable.setFillsViewportHeight(true);

        JScrollPane scrollPane =
                new JScrollPane(productTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport()
                .setBackground(Color.WHITE);

        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    public final void loadProducts() {

        tableModel.setRowCount(0);

        try {

            List<Product> products =
                    productDAO.getAllProducts();

            for (Product product : products) {

                String stockDisplay =
                        product.isLowStock()
                                ? product.getQuantity()
                                  + "  • Low"
                                : String.valueOf(
                                        product.getQuantity()
                                );

                tableModel.addRow(
                        new Object[]{
                                product.getProductId(),
                                product.getBarcode(),
                                product.getName(),
                                "Rs. " + product.getCostPrice(),
                                "Rs. " + product.getSellingPrice(),
                                stockDisplay,
                                product.getReorderLevel(),
                                product.getStatus()
                        }
                );
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
}