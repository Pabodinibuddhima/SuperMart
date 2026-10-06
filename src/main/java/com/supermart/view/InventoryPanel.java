/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */

import com.supermart.view.component.SuperMartTableStyle;
import com.supermart.dao.InventoryDAO;
import com.supermart.model.Product;
import com.supermart.model.StockTransaction;
import com.supermart.view.component.PlaceholderTextField;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class InventoryPanel extends JPanel {

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

    private final InventoryDAO inventoryDAO;

    private JTable inventoryTable;
    private DefaultTableModel tableModel;

    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    private CardLayout inventoryCardLayout;
    private JPanel inventoryContentPanel;

    private JButton currentStockButton;
    private JButton stockHistoryButton;
    
    private JTextField inventorySearchField;
    private JComboBox<String> stockFilterComboBox;

    private JTextField historySearchField;
    private JComboBox<String> historyTypeComboBox;

    private TableRowSorter<DefaultTableModel> inventorySorter;
    private TableRowSorter<DefaultTableModel> historySorter;

    
    

    public InventoryPanel() {

        inventoryDAO = new InventoryDAO();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        38, 42, 38, 42
                )
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createInventoryContent(), BorderLayout.CENTER);

        loadInventory();
        loadStockHistory();
        
        
    }

    private JPanel createHeader() {

        JPanel header =
                new JPanel(new BorderLayout());

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 28, 0
                )
        );

        JPanel titlePanel = new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("Inventory");

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
                        "Monitor stock levels and manage inventory movements."
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

        titlePanel.add(
                Box.createVerticalStrut(7)
        );

        titlePanel.add(subtitle);


        JButton stockEntryButton =
                new JButton("+ Stock Entry");

        stockEntryButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        stockEntryButton.setForeground(Color.WHITE);

        stockEntryButton.setBackground(
                new Color(25, 25, 28)
        );

        stockEntryButton.setFocusPainted(false);

        stockEntryButton.setBorder(
                BorderFactory.createEmptyBorder(
                        11, 18, 11, 18
                )
        );

        stockEntryButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        stockEntryButton.addActionListener(e -> {

            JFrame parentFrame =
                    (JFrame) SwingUtilities.getWindowAncestor(this);

            StockEntryDialog dialog =
                    new StockEntryDialog(parentFrame);

            dialog.setVisible(true);

            if (dialog.isStockAdded()) {

                // Refresh both inventory views
                loadInventory();
                loadStockHistory();
            }
        });
        

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        actionPanel.setOpaque(false);

        actionPanel.add(stockEntryButton);

        header.add(
                actionPanel,
                BorderLayout.EAST
        );

        return header;
        }
        
    private void styleTabButton(
            JButton button,
            boolean selected
    ) {

        button.setFont(
                new Font(
                        "SansSerif",
                        selected
                                ? Font.BOLD
                                : Font.PLAIN,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        9, 15, 9, 15
                )
        );

        if (selected) {

            button.setBackground(
                    new Color(25, 25, 28)
            );

            button.setForeground(
                    Color.WHITE
            );

        } else {

            button.setBackground(
                    Color.WHITE
            );

            button.setForeground(
                    TEXT_PRIMARY
            );
        }
    }

    private JPanel createInventoryContent() {

        JPanel container =
                new JPanel(new BorderLayout());

        container.setOpaque(false);

        // -----------------------------------------
        // Navigation buttons
        // -----------------------------------------

        JPanel navigationPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                ));

        navigationPanel.setOpaque(false);

        navigationPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 16, 0
                )
        );

        currentStockButton =
                new JButton("Current Stock");

        stockHistoryButton =
                new JButton("Stock History");

        styleTabButton(
                currentStockButton,
                true
        );

        styleTabButton(
                stockHistoryButton,
                false
        );

        navigationPanel.add(currentStockButton);

        navigationPanel.add(
                Box.createHorizontalStrut(8)
        );

        navigationPanel.add(stockHistoryButton);


        // -----------------------------------------
        // Card area
        // -----------------------------------------

        inventoryCardLayout =
                new CardLayout();

        inventoryContentPanel =
                new JPanel(inventoryCardLayout);

        inventoryContentPanel.setOpaque(false);

        inventoryContentPanel.add(
                createTableSection(),
                "CURRENT_STOCK"
        );

        inventoryContentPanel.add(
                createHistorySection(),
                "STOCK_HISTORY"
        );


        // -----------------------------------------
        // Button events
        // -----------------------------------------

        currentStockButton.addActionListener(e -> {

            inventoryCardLayout.show(
                    inventoryContentPanel,
                    "CURRENT_STOCK"
            );

            styleTabButton(
                    currentStockButton,
                    true
            );

            styleTabButton(
                    stockHistoryButton,
                    false
            );
        });


        stockHistoryButton.addActionListener(e -> {

            loadStockHistory();

            inventoryCardLayout.show(
                    inventoryContentPanel,
                    "STOCK_HISTORY"
            );

            styleTabButton(
                    currentStockButton,
                    false
            );

            styleTabButton(
                    stockHistoryButton,
                    true
            );
        });


        container.add(
                navigationPanel,
                BorderLayout.NORTH
        );

        container.add(
                inventoryContentPanel,
                BorderLayout.CENTER
        );

        return container;
    }
    
    private JPanel createTableSection() {

        JPanel card =
                new JPanel(new BorderLayout());

        card.setBackground(CARD_BACKGROUND);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 18, 18, 18
                        )
                )
        );

        String[] columns = {
                "ID",
                "Barcode",
                "Product",
                "Quantity",
                "Reorder Level",
                "Stock Status"
        };
        
        
        tableModel =
        new DefaultTableModel(
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
                    case 3: // Quantity
                    case 4: // Reorder Level
                        return Integer.class;

                    default:
                        return String.class;
                }
            }
        };

        inventoryTable =
                new JTable(tableModel);
        
        SuperMartTableStyle.apply(
                inventoryTable
        );
        
        
        inventorySorter =
                new TableRowSorter<>(tableModel);

        inventoryTable.setRowSorter(inventorySorter);
        
        inventoryTable.setShowVerticalLines(false);
        inventoryTable.setShowHorizontalLines(true);

        inventoryTable.setSelectionBackground(
                new Color(239, 240, 243)
        );

        inventoryTable.setSelectionForeground(
                TEXT_PRIMARY
        );

        JTableHeader tableHeader =
                inventoryTable.getTableHeader();

        tableHeader.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        tableHeader.setForeground(
                TEXT_SECONDARY
        );

        tableHeader.setBackground(
                new Color(250, 250, 251)
        );

        tableHeader.setPreferredSize(
                new Dimension(
                        tableHeader
                                .getPreferredSize()
                                .width,
                        42
                )
        );

        inventoryTable.setFillsViewportHeight(true);

        JScrollPane scrollPane =
                new JScrollPane(
                        inventoryTable
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane
                .getViewport()
                .setBackground(Color.WHITE);
        
        
        
        card.add(
                createInventoryFilterPanel(),
                BorderLayout.NORTH
        );

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    private JPanel createInventoryFilterPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 15, 0
                )
        );

        inventorySearchField =
            new PlaceholderTextField(
                    "Search product or barcode...",
                    22
            );
        

        inventorySearchField.setToolTipText(
                "Search inventory by product name or barcode"
        );

        stockFilterComboBox =
                new JComboBox<>(
                        new String[]{
                                "All Stock",
                                "In Stock",
                                "Low Stock",
                                "Out of Stock"
                        }
                );

        panel.add(inventorySearchField);
        panel.add(stockFilterComboBox);

        inventorySearchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                applyInventoryFilters();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                applyInventoryFilters();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                applyInventoryFilters();
                            }
                        }
                );

        stockFilterComboBox.addActionListener(
                e -> applyInventoryFilters()
        );

        return panel;
    }
    
    private void applyInventoryFilters() {

        String searchText =
                inventorySearchField
                        .getText()
                        .trim();

        String stockFilter =
                (String)
                        stockFilterComboBox
                                .getSelectedItem();

        RowFilter<DefaultTableModel, Object>
                searchRowFilter =
                new RowFilter<>() {

                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                                    ? extends Object> entry
                    ) {

                        if (searchText.isEmpty()) {
                            return true;
                        }

                        String barcode =
                                entry.getStringValue(1);

                        String productName =
                                entry.getStringValue(2);

                        String search =
                                searchText.toLowerCase();

                        return barcode
                                .toLowerCase()
                                .contains(search)
                                ||
                                productName
                                        .toLowerCase()
                                        .contains(search);
                    }
                };


        RowFilter<DefaultTableModel, Object>
                stockRowFilter =
                new RowFilter<>() {

                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                                    ? extends Object> entry
                    ) {

                        if (stockFilter == null
                                || stockFilter.equals(
                                        "All Stock"
                                )) {

                            return true;
                        }

                        String status =
                                entry.getStringValue(5);

                        return switch (stockFilter) {

                            case "In Stock" ->
                                    status.equals(
                                            "IN STOCK"
                                    );

                            case "Low Stock" ->
                                    status.equals(
                                            "LOW STOCK"
                                    );

                            case "Out of Stock" ->
                                    status.equals(
                                            "OUT OF STOCK"
                                    );

                            default -> true;
                        };
                    }
                };


        inventorySorter.setRowFilter(
                RowFilter.andFilter(
                        List.of(
                                searchRowFilter,
                                stockRowFilter
                        )
                )
        );
    }
    
    private JPanel createHistorySection() {

        JPanel card =
                new JPanel(new BorderLayout());

        card.setBackground(CARD_BACKGROUND);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 18, 18, 18
                        )
                )
        );

        String[] columns = {
                "Date & Time",
                "Product",
                "Type",
                "Quantity",
                "Reference"
        };

        historyTableModel =
                new DefaultTableModel(
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

        historyTable =
                new JTable(historyTableModel);
        
        SuperMartTableStyle.apply(
        historyTable
);
        
        
        historySorter =
                new TableRowSorter<>(
                        historyTableModel
                );

        historyTable.setRowSorter(
                historySorter
        );
        
        historyTable.setShowVerticalLines(false);
        historyTable.setShowHorizontalLines(true);

        historyTable.setSelectionBackground(
                new Color(239, 240, 243)
        );

        historyTable.setSelectionForeground(
                TEXT_PRIMARY
        );

        JTableHeader header =
                historyTable.getTableHeader();

        header.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        header.setForeground(
                TEXT_SECONDARY
        );

        header.setBackground(
                new Color(250, 250, 251)
        );

        header.setPreferredSize(
                new Dimension(
                        header.getPreferredSize().width,
                        42
                )
        );

        historyTable.setFillsViewportHeight(true);

        JScrollPane scrollPane =
                new JScrollPane(historyTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport()
                .setBackground(Color.WHITE);
        
        card.add(
                createHistoryFilterPanel(),
                BorderLayout.NORTH
        );


        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return card;
    }
    
    private JPanel createHistoryFilterPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 15, 0
                )
        );
        
        historySearchField =
                new PlaceholderTextField(
                        "Search product or reference...",
                        22
                );

        historySearchField.setToolTipText(
                "Search stock history by product name or reference note"
        );
        

        historyTypeComboBox =
                new JComboBox<>(
                        new String[]{
                                "All Types",
                                "STOCK_IN",
                                "SALE",
                                "RETURN",
                                "DAMAGE",
                                "ADJUSTMENT"
                        }
                );

        panel.add(historySearchField);
        panel.add(historyTypeComboBox);


        historySearchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                applyHistoryFilters();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                applyHistoryFilters();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                applyHistoryFilters();
                            }
                        }
                );


        historyTypeComboBox.addActionListener(
                e -> applyHistoryFilters()
        );

        return panel;
    }
    private void applyHistoryFilters() {

        String searchText =
                historySearchField
                        .getText()
                        .trim()
                        .toLowerCase();

        String selectedType =
                (String)
                        historyTypeComboBox
                                .getSelectedItem();


        RowFilter<DefaultTableModel, Object>
                searchFilter =
                new RowFilter<>() {

                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                                    ? extends Object> entry
                    ) {

                        if (searchText.isEmpty()) {
                            return true;
                        }

                        String product =
                                entry.getStringValue(1)
                                        .toLowerCase();

                        String reference =
                                entry.getStringValue(4)
                                        .toLowerCase();

                        return product.contains(
                                searchText
                        )
                                ||
                                reference.contains(
                                        searchText
                                );
                    }
                };


        RowFilter<DefaultTableModel, Object>
                typeFilter =
                new RowFilter<>() {

                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                                    ? extends Object> entry
                    ) {

                        if (selectedType == null
                                || selectedType.equals(
                                        "All Types"
                                )) {

                            return true;
                        }

                        String type =
                                entry.getStringValue(2);

                        return type.equals(
                                selectedType
                        );
                    }
                };


        historySorter.setRowFilter(
                RowFilter.andFilter(
                        List.of(
                                searchFilter,
                                typeFilter
                        )
                )
        );
    }
    
    public void refreshPanel() {

        loadInventory();
        loadStockHistory();

        if (inventoryTable != null) {
            inventoryTable.clearSelection();
        }

        if (historyTable != null) {
            historyTable.clearSelection();
        }
    }

    public final void loadInventory() {

        tableModel.setRowCount(0);

        try {

            List<Product> products =
                    inventoryDAO.getInventory();

            for (Product product : products) {

                String stockStatus;

                if (product.getQuantity() == 0) {

                    stockStatus =
                            "OUT OF STOCK";

                } else if (product.isLowStock()) {

                    stockStatus =
                            "LOW STOCK";

                } else {

                    stockStatus =
                            "IN STOCK";
                }

                tableModel.addRow(
                        new Object[]{
                                product.getProductId(),
                                product.getBarcode(),
                                product.getName(),
                                product.getQuantity(),
                                product.getReorderLevel(),
                                stockStatus
                        }
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load inventory.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    public final void loadStockHistory() {

    historyTableModel.setRowCount(0);

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                    "dd MMM yyyy  HH:mm"
            );

    try {

        List<StockTransaction> transactions =
                inventoryDAO.getStockHistory();

        for (StockTransaction transaction
                : transactions) {

            String quantityDisplay;

            switch (
                    transaction.getTransactionType()
            ) {

                case "STOCK_IN", "RETURN" ->
                        quantityDisplay =
                                "+"
                                + transaction.getQuantity();

                case "SALE", "DAMAGE" ->
                        quantityDisplay =
                                "-"
                                + transaction.getQuantity();

                default ->
                        quantityDisplay =
                                String.valueOf(
                                        transaction.getQuantity()
                                );
            }

            String reference =
                    transaction.getReferenceNote();

            if (reference == null
                    || reference.isBlank()) {

                reference = "—";
            }

            historyTableModel.addRow(
                    new Object[]{
                            transaction
                                    .getCreatedAt()
                                    .format(formatter),

                            transaction
                                    .getProductName(),

                            transaction
                                    .getTransactionType(),

                            quantityDisplay,

                            reference
                    }
            );
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Unable to load stock history.\n"
                        + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
}