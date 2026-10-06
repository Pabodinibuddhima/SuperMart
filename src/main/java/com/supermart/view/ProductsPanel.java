/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

import com.supermart.dao.ProductDAO;
import com.supermart.model.Product;
import com.supermart.view.component.SuperMartTableStyle;
import com.supermart.view.component.PlaceholderTextField;
import com.supermart.view.component.ResponsiveFlowPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
    
    
    private TableRowSorter<DefaultTableModel> tableSorter;

    //private JTextField searchField;
    private PlaceholderTextField searchField;
    private JComboBox<String> statusFilter;
    
    private JButton editButton;
    private JButton statusButton;
    
    private JComboBox<String> sortByCombo;
    private JComboBox<String> sortOrderCombo;

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
        
        statusButton =
            new JButton("Deactivate");
        
        statusButton.setEnabled(false);
        
        statusButton.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        statusButton.setFocusPainted(false);

        statusButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
        
        
        
        statusButton.addActionListener(
                e -> changeSelectedProductStatus()
        );
        
        editButton =
        new JButton("Edit");
        
        editButton.setEnabled(false);

        editButton.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );
        editButton.setFocusPainted(false);

        editButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
        
 
        editButton.addActionListener(e -> {

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
            
            
            int modelRow =
                    productTable
                            .convertRowIndexToModel(
                                    selectedRow
                            );

            

            int productId =
                    (int) tableModel.getValueAt(
                            modelRow,
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
        
        
        actionPanel.add(editButton);
        actionPanel.add(statusButton);
        actionPanel.add(addProductButton);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(actionPanel,BorderLayout.EAST);
        return header;
    }

    private JPanel createFilterPanel() {
        JPanel panel = 
                new ResponsiveFlowPanel();
        

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, //12
                        0, //12
                        16, // 12
                        0 //12
                )
        );


        // ---------- Search ----------
        
        searchField =
            new PlaceholderTextField(
                    "Search product or barcode...",
                    22
            );
        
        searchField.setToolTipText(
                "Search by product name or barcode"
        );


        // ---------- Status filter ----------
        statusFilter =
                new JComboBox<>(
                        new String[]{
                            "Status: All",
                            "Status: ACTIVE",
                            "Status: INACTIVE"
                        }
                );

        statusFilter.setPreferredSize(
                new Dimension(
                        145,
                        32
                )
        );


        // ---------- Sort field ----------
        sortByCombo =
                new JComboBox<>(
                        new String[]{
                            "Sort: ID",
                            "Sort: Barcode",
                            "Sort: Name",
                            "Sort: Cost Price",
                            "Sort: Selling Price",
                            "Sort: Stock",
                            "Sort: Status"
                        }
                );

        sortByCombo.setPreferredSize(
                new Dimension(
                        150,
                        32
                )
        );

        sortByCombo.setToolTipText(
                "Choose which product field to sort by"
        );


        // ---------- Sort direction ----------
        sortOrderCombo =
                new JComboBox<>(
                        new String[]{
                            "Ascending",
                            "Descending"
                        }
                );

        sortOrderCombo.setPreferredSize(
                new Dimension(
                        120,
                        32
                )
        );

        sortOrderCombo.setToolTipText(
                "Choose ascending or descending order"
        );


        panel.add(searchField);
        panel.add(statusFilter);
        panel.add(sortByCombo);
        panel.add(sortOrderCombo);


        // ---------- Search listener ----------
        searchField.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate(
                    javax.swing.event.DocumentEvent e
            ) {
                applyFilters();
            }

            @Override
            public void removeUpdate(
                    javax.swing.event.DocumentEvent e
            ) {
                applyFilters();
            }

            @Override
            public void changedUpdate(
                    javax.swing.event.DocumentEvent e
            ) {
                applyFilters();
            }
        });


        statusFilter.addActionListener(
                e -> applyFilters()
        );


        sortByCombo.addActionListener(
                e -> applySorting()
        );

        sortOrderCombo.addActionListener(
                e -> applySorting()
        );


        return panel;
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
            
            @Override
            public Class<?> getColumnClass(
                    int columnIndex
            ) {

                switch (columnIndex) {

                    case 0: // ID
                    case 6: // Reorder Level
                        return Integer.class;

                    default:
                        return String.class;
                }
            }
        };


        productTable = new JTable(tableModel);
        
        tableSorter =
                new TableRowSorter<>(
                        tableModel
                );

        productTable.setRowSorter(
                tableSorter
        );

        SuperMartTableStyle.apply(
                productTable
        );
        
        productTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        productTable
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        updateProductActionButtons();
                    }
                });

        productTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        productTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(45);   // ID

        productTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(100);  // Barcode

        productTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(190);  // Product

        productTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);  // Cost

        productTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(110);  // Selling

        productTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(90);   // Stock
        
        productTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(100);  // Reorder
               
        productTable.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(90);   // Status
   
        

        productTable.setFillsViewportHeight(true);

        JScrollPane scrollPane =
                new JScrollPane(productTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport()
                .setBackground(Color.WHITE);

        card.add(
                createFilterPanel(),
                BorderLayout.NORTH
        );
        
        
        card.add(scrollPane, BorderLayout.CENTER);
        applySorting();

        return card;
    }

    private void updateProductActionButtons() {

        int selectedViewRow =
                productTable.getSelectedRow();

        boolean selected =
                selectedViewRow >= 0;


        editButton.setEnabled(selected);
        statusButton.setEnabled(selected);


        if (!selected) {

            statusButton.setText(
                    "Deactivate"
            );

            return;
        }


        int modelRow =
                productTable
                        .convertRowIndexToModel(
                                selectedViewRow
                        );


        String status =
                tableModel
                        .getValueAt(
                                modelRow,
                                7
                        )
                        .toString();


        if ("ACTIVE".equals(status)) {

            statusButton.setText(
                    "Deactivate"
            );

        } else {

            statusButton.setText(
                    "Activate"
            );
        }
    }
    
    // ==========================================
    // CHANGE PRODUCT STATUS
    // ==========================================

    private void changeSelectedProductStatus() {

        int selectedViewRow =
                productTable.getSelectedRow();


        if (selectedViewRow < 0) {
            return;
        }


        int modelRow =
                productTable
                        .convertRowIndexToModel(
                                selectedViewRow
                        );


        int productId =
                (Integer)
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                );


        String productName =
                tableModel
                        .getValueAt(
                                modelRow,
                                2
                        )
                        .toString();


        String currentStatus =
                tableModel
                        .getValueAt(
                                modelRow,
                                7
                        )
                        .toString();


        boolean currentlyActive =
                "ACTIVE".equals(
                        currentStatus
                );


        String newStatus =
                currentlyActive
                        ? "INACTIVE"
                        : "ACTIVE";


        String action =
                currentlyActive
                        ? "deactivate"
                        : "activate";


        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to "
                                + action
                                + " \""
                                + productName
                                + "\"?",
                        "Confirm Product Status",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (choice
                != JOptionPane.YES_OPTION) {

            return;
        }


        try {

            boolean updated =
                    productDAO
                            .updateProductStatus(
                                    productId,
                                    newStatus
                            );


            if (updated) {

                loadProducts();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Product status could not be updated.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update product status.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void applySorting() {

        if (tableSorter == null
                || sortByCombo == null
                || sortOrderCombo == null) {

            return;
        }


        String selectedField =
                (String)
                        sortByCombo
                                .getSelectedItem();

        String selectedOrder =
                (String)
                        sortOrderCombo
                                .getSelectedItem();


        int columnIndex;

        switch (selectedField) {

            case "Sort: Barcode":
                columnIndex = 1;
                break;

            case "Sort: Name":
                columnIndex = 2;
                break;

            case "Sort: Cost Price":
                columnIndex = 3;
                break;

            case "Sort: Selling Price":
                columnIndex = 4;
                break;

            case "Sort: Stock":
                columnIndex = 5;
                break;

            case "Sort: Status":
                columnIndex = 7;
                break;

            case "Sort: ID":
            default:
                columnIndex = 0;
                break;
        }


        SortOrder order;

        if ("Descending".equals(
                selectedOrder
        )) {

            order =
                    SortOrder.DESCENDING;

        } else {

            order =
                    SortOrder.ASCENDING;
        }


        tableSorter.setSortKeys(
                java.util.List.of(
                        new RowSorter.SortKey(
                                columnIndex,
                                order
                        )
                )
        );

        tableSorter.sort();
    }

    private void applyFilters() {

        if (tableSorter == null
                || searchField == null
                || statusFilter == null) {

            return;
        }


        String searchText =
                searchField
                        .getText()
                        .trim();


        String selectedStatus =
                (String)
                        statusFilter
                                .getSelectedItem();


        java.util.List<RowFilter<Object, Object>>
                filters =
                new java.util.ArrayList<>();


        // Search Product + Barcode
        if (!searchText.isEmpty()) {

            filters.add(
                    RowFilter.regexFilter(
                            "(?i)"
                                    + java.util.regex.Pattern
                                            .quote(
                                                    searchText
                                            ),
                            1,
                            2
                    )
            );
        }


        // Status
        if (selectedStatus != null
                && !"Status: All".equals(
                        selectedStatus
                )) {

            String status =
                    selectedStatus.replace(
                            "Status: ",
                            ""
                    );


            filters.add(
                    RowFilter.regexFilter(
                            "^"
                                    + java.util.regex.Pattern
                                            .quote(status)
                                    + "$",
                            7
                    )
            );
        }


        if (filters.isEmpty()) {

            tableSorter.setRowFilter(
                    null
            );

        } else {

            tableSorter.setRowFilter(
                    RowFilter.andFilter(
                            filters
                    )
            );
        }
    }

    public void refreshPanel() {

        loadProducts();

        productTable.clearSelection();

        updateProductActionButtons();
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
            
            productTable.clearSelection();
            updateProductActionButtons();
            

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