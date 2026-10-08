/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */

import com.supermart.dao.CustomerDAO;
import com.supermart.dao.EmployeeDAO;
import com.supermart.dao.ProductDAO;
import com.supermart.model.Customer;
import com.supermart.model.Employee;
import com.supermart.model.Product;
import com.supermart.view.component.PlaceholderTextField;
import com.supermart.util.AppearanceManager;

import com.supermart.exception.InsufficientStockException;
import com.supermart.model.Payment;
import com.supermart.model.Sale;
import com.supermart.model.SaleItem;
import com.supermart.service.SaleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;


public class SalesPanel extends JPanel {

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

    private static final Color PRIMARY =
            new Color(28, 28, 30);


    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private final SaleService saleService;


    private final List<Product> activeProducts;
    private final Map<Integer, CartItem> cartItems;

    private JComboBox<CustomerOption> customerComboBox;
    private JComboBox<Employee> cashierComboBox;

    private PlaceholderTextField productSearchField;

    private JPanel productsListPanel;
    private JPanel cartItemsPanel;

    private JLabel subtotalValueLabel;
    private JLabel totalValueLabel;
    private JLabel changeValueLabel;

    private PlaceholderTextField discountField;
    private PlaceholderTextField amountReceivedField;
    
    private JComboBox<String> discountTypeComboBox;
    private JComboBox<String> paymentMethodComboBox;

    private JButton completeSaleButton;
    private Runnable saleCompletedListener;

    public SalesPanel() {

        productDAO =
                new ProductDAO();

        customerDAO =
                new CustomerDAO();

        employeeDAO =
                new EmployeeDAO();
        
        saleService =
                new SaleService();

        activeProducts =
                new ArrayList<>();
        cartItems =
                new LinkedHashMap<>();

        setLayout(
                new BorderLayout()
        );

        setBackground(
                BACKGROUND
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        38,
                        42,
                        38,
                        42
                )
        );


        add(
                createHeader(),
                BorderLayout.NORTH
        );

        add(
                createMainContent(),
                BorderLayout.CENTER
        );


        loadSalesData();
    }


    // ==========================================
    // HEADER
    // ==========================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        24,
                        0
                )
        );


        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel("Sales");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(
                TEXT_PRIMARY
        );


        JLabel subtitle =
                new JLabel(
                        "Create bills and process customer purchases."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                TEXT_SECONDARY
        );


        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(4)
        );

        titlePanel.add(subtitle);


        JButton historyButton =
                createSecondaryButton(
                        "Sales History"
                );
        
        Dimension historySize =
                historyButton.getPreferredSize();

        historyButton.setPreferredSize(
                new Dimension(
                        historySize.width + 24,
                        36
                )
        );
        
        historyButton.addActionListener(
                e -> openSalesHistory()
        );


        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JPanel historyButtonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        historyButtonPanel.setOpaque(false);

        historyButtonPanel.add(
                historyButton
        );

        header.add(
                historyButtonPanel,
                BorderLayout.EAST
        );


        return header;
    }


    // ==========================================
    // MAIN CONTENT
    // ==========================================

    private JPanel createMainContent() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        wrapper.setOpaque(false);


        wrapper.add(
                createSaleInformationPanel(),
                BorderLayout.NORTH
        );


        JPanel columns =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                18,
                                0
                        )
                );

        columns.setOpaque(false);


        columns.add(
                createProductsPanel()
        );

        columns.add(
                createCartPanel()
        );


        wrapper.add(
                columns,
                BorderLayout.CENTER
        );


        return wrapper;
    }
    // ==========================================
    // CUSTOMER / CASHIER
    // ==========================================

    private JPanel createSaleInformationPanel() {

        JPanel panel =
                createCard();

        panel.setLayout(
                new FlowLayout(
                        FlowLayout.LEFT,
                        14,
                        14
                )
        );


        JLabel customerLabel =
                createFieldLabel(
                        "Customer"
                );


        customerComboBox =
                new JComboBox<>();

        customerComboBox.setPreferredSize(
                new Dimension(
                        220,
                        34
                )
        );


        JLabel cashierLabel =
                createFieldLabel(
                        "Cashier"
                );


        cashierComboBox =
                new JComboBox<>();

        cashierComboBox.setPreferredSize(
                new Dimension(
                        220,
                        34
                )
        );


        panel.add(customerLabel);
        panel.add(customerComboBox);

        panel.add(
                Box.createHorizontalStrut(12)
        );

        panel.add(cashierLabel);
        panel.add(cashierComboBox);


        return panel;
    }


    // ==========================================
    // PRODUCTS
    // ==========================================

    private JPanel createProductsPanel() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout(
                        0,
                        14
                )
        );


        JPanel top =
                new JPanel(
                        new BorderLayout(
                                0,
                                10
                        )
                );

        top.setOpaque(false);


        JLabel title =
                createSectionTitle(
                        "Products"
                );


        productSearchField =
                new PlaceholderTextField(
                        "Search by product name or barcode...",
                        20
                );

        productSearchField.setPreferredSize(
                new Dimension(
                        200,
                        34
                )
        );


        productSearchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                filterProducts();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                filterProducts();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                filterProducts();
                            }
                        }
                );


        top.add(
                title,
                BorderLayout.NORTH
        );

        top.add(
                productSearchField,
                BorderLayout.CENTER
        );


        productsListPanel =
                new JPanel();

        productsListPanel.setOpaque(false);

        productsListPanel.setLayout(
                new BoxLayout(
                        productsListPanel,
                        BoxLayout.Y_AXIS
                )
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        productsListPanel
                );

        scrollPane.setBorder(null);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(14);

        scrollPane.getViewport()
                .setBackground(
                        CARD_BACKGROUND
                );


        card.add(
                top,
                BorderLayout.NORTH
        );

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );


        return card;
    }


    // ==========================================
    // CART
    // ==========================================

    private JPanel createCartPanel() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout(
                        0,
                        14
                )
        );


        JLabel title =
                createSectionTitle(
                        "Current Sale"
                );


        cartItemsPanel =
                new JPanel();

        cartItemsPanel.setOpaque(false);

        cartItemsPanel.setLayout(
                new BoxLayout(
                        cartItemsPanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel emptyLabel =
                new JLabel(
                        "No products added yet.",
                        SwingConstants.CENTER
                );

        emptyLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        emptyLabel.setForeground(
                TEXT_SECONDARY
        );

        emptyLabel.setAlignmentX(
                CENTER_ALIGNMENT
        );


        cartItemsPanel.add(
                Box.createVerticalStrut(30)
        );

        cartItemsPanel.add(
                emptyLabel
        );


        JScrollPane cartScrollPane =
                new JScrollPane(
                        cartItemsPanel
                );

        cartScrollPane.setBorder(null);

        cartScrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(14);

        cartScrollPane.getViewport()
                .setBackground(
                        CARD_BACKGROUND
                );


        JPanel summaryPanel =
                createSummaryPanel();


        card.add(
                title,
                BorderLayout.NORTH
        );

        card.add(
                cartScrollPane,
                BorderLayout.CENTER
        );

        card.add(
                summaryPanel,
                BorderLayout.SOUTH
        );


        return card;
    }


    // ==========================================
    // SUMMARY
    // ==========================================
    
    private JPanel createSummaryPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createMatteBorder(
                        1,
                        0,
                        0,
                        0,
                        BORDER
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        7,
                        4,
                        7,
                        4
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;


        // ==========================================
        // SUBTOTAL
        // ==========================================

        subtotalValueLabel =
                createMoneyLabel(
                        "Rs. 0.00"
                );

        addSummaryRow(
                panel,
                gbc,
                0,
                "Subtotal",
                subtotalValueLabel
        );


        // ==========================================
        // DISCOUNT
        // ==========================================

        JLabel discountLabel =
                new JLabel("Discount");

        discountLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        discountLabel.setForeground(
                TEXT_SECONDARY
        );
        
        discountTypeComboBox =
                new JComboBox<>(
                        new String[]{
                            "Amount",
                            "Percent"
                        }
                );

        discountTypeComboBox.setPreferredSize(
                new Dimension(
                        105,//95
                        34 //32
                )
        );


        discountField =
                new PlaceholderTextField(
                        "0.00",
                        7
                );

        discountField.setPreferredSize(
                new Dimension(
                        105, //100
                        34 //32
                )
        );

        discountField.setHorizontalAlignment(
                SwingConstants.RIGHT
        );


        JPanel discountInputPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                6,
                                0
                        )
                );

        discountInputPanel.setOpaque(false);

        discountInputPanel.add(
                discountTypeComboBox
        );

        discountInputPanel.add(
                discountField
        );


        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.42;//1

        panel.add(
                discountLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 0.42; //0

        panel.add(
                discountInputPanel,
                gbc
        );

        // ==========================================
        // TOTAL
        // ==========================================

        totalValueLabel =
                createMoneyLabel(
                        "Rs. 0.00"
                );

        addSummaryRow(
                panel,
                gbc,
                2,
                "Total",
                totalValueLabel
        );


        // ==========================================
        // PAYMENT METHOD
        // ==========================================

        JLabel paymentMethodLabel =
                new JLabel(
                        "Payment Method"
                );

        paymentMethodLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        paymentMethodLabel.setForeground(
                TEXT_SECONDARY
        );


        paymentMethodComboBox =
                new JComboBox<>(
                        new String[]{
                            "CASH",
                            "CARD"
                        }
                );

        paymentMethodComboBox.setPreferredSize(
                new Dimension(
                        190,//130
                        34 //32
                )
        );


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.42;//1

        panel.add(
                paymentMethodLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 0;

        panel.add(
                paymentMethodComboBox,
                gbc
        );


        // ==========================================
        // AMOUNT RECEIVED
        // ==========================================

        JLabel amountLabel =
                new JLabel(
                        "Amount Received"
                );

        amountLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        amountLabel.setForeground(
                TEXT_SECONDARY
        );


        amountReceivedField =
                new PlaceholderTextField(
                        "0.00",
                        10
                );

        amountReceivedField.setPreferredSize(
                new Dimension(
                        190, //130
                        34
                )
        );

        amountReceivedField.setHorizontalAlignment(
                SwingConstants.RIGHT
        );


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.42; //1

        panel.add(
                amountLabel,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 0.42; //0

        panel.add(
                amountReceivedField,
                gbc
        );


        // ==========================================
        // CHANGE
        // ==========================================

        changeValueLabel =
                createMoneyLabel(
                        "Rs. 0.00"
                );

        addSummaryRow(
                panel,
                gbc,
                5,
                "Change",
                changeValueLabel
        );


        // ==========================================
        // COMPLETE SALE
        // ==========================================

        completeSaleButton =
                createPrimaryButton(
                        "Complete Sale"
                );

        completeSaleButton.setEnabled(
                false
        );
        
        completeSaleButton.addActionListener(
                e -> completeSale()
        );


        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        gbc.insets =
                new Insets(
                        14,
                        4,
                        4,
                        4
                );


        panel.add(
                completeSaleButton,
                gbc
        );


        // ==========================================
        // LIVE CALCULATION LISTENERS
        // ==========================================

        javax.swing.event.DocumentListener
                paymentDocumentListener =
                new javax.swing.event.DocumentListener() {

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {
                        updateTotals();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {
                        updateTotals();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e
                    ) {
                        updateTotals();
                    }
                };


        discountField
                .getDocument()
                .addDocumentListener(
                        paymentDocumentListener
                );
        
        discountTypeComboBox
                .addActionListener(
                        e -> updateTotals()
                );


        amountReceivedField
                .getDocument()
                .addDocumentListener(
                        paymentDocumentListener
                );


        paymentMethodComboBox
                .addActionListener(
                        e -> paymentMethodChanged()
                );


        return panel;
    }
    

    private void addSummaryRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JLabel valueLabel
    ) {

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                TEXT_SECONDARY
        );


        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1;

        panel.add(
                label,
                gbc
        );


        gbc.gridx = 1;
        gbc.weightx = 0.58; //0

        panel.add(
                valueLabel,
                gbc
        );
    }


    // ==========================================
    // LOAD DATABASE DATA
    // ==========================================

    public final void loadSalesData() {

        loadProducts();

        loadCustomers();

        loadCashiers();
    }


    private void loadProducts() {

        try {

            activeProducts.clear();


            for (Product product
                    : productDAO.getAllProducts()) {

                if ("ACTIVE".equals(
                        product.getStatus()
                )) {

                    activeProducts.add(
                            product
                    );
                }
            }


            displayProducts(
                    activeProducts
            );


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to load products.",
                    e
            );
        }
    }


    private void loadCustomers() {

        try {

            customerComboBox.removeAllItems();


            customerComboBox.addItem(
                    new CustomerOption(
                            null,
                            "Walk-in Customer"
                    )
            );


            for (Customer customer
                    : customerDAO.getAllCustomers()) {

                if ("ACTIVE".equals(
                        customer.getStatus()
                )) {

                    customerComboBox.addItem(
                            new CustomerOption(
                                    customer,
                                    customer.getName()
                            )
                    );
                }
            }


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to load customers.",
                    e
            );
        }
    }


    private void loadCashiers() {

        try {

            cashierComboBox.removeAllItems();


            for (Employee employee
                    : employeeDAO.getAllEmployees()) {

                boolean active =
                        "ACTIVE".equals(
                                employee.getStatus()
                        );

                boolean canProcessSales =
                        "CASHIER".equals(
                                employee.getRole()
                        )
                        || "MANAGER".equals(
                                employee.getRole()
                        );


                if (active
                        && canProcessSales) {

                    cashierComboBox.addItem(
                            employee
                    );
                }
            }


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to load cashiers.",
                    e
            );
        }
    }


    // ==========================================
    // PRODUCT SEARCH / DISPLAY
    // ==========================================

    private void filterProducts() {

        String search =
                productSearchField
                        .getText()
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            displayProducts(
                    activeProducts
            );

            return;
        }


        List<Product> filtered =
                new ArrayList<>();


        for (Product product
                : activeProducts) {

            String name =
                    product.getName() == null
                    ? ""
                    : product.getName()
                            .toLowerCase();

            String barcode =
                    product.getBarcode() == null
                    ? ""
                    : product.getBarcode()
                            .toLowerCase();


            if (name.contains(search)
                    || barcode.contains(search)) {

                filtered.add(
                        product
                );
            }
        }


        displayProducts(
                filtered
        );
    }


    private void displayProducts(
            List<Product> products
    ) {

        productsListPanel.removeAll();


        if (products.isEmpty()) {

            JLabel empty =
                    new JLabel(
                            "No matching products."
                    );

            empty.setForeground(
                    TEXT_SECONDARY
            );

            empty.setBorder(
                    BorderFactory.createEmptyBorder(
                            20,
                            4,
                            20,
                            4
                    )
            );


            productsListPanel.add(
                    empty
            );

        } else {

            for (Product product
                    : products) {

                productsListPanel.add(
                        createProductRow(
                                product
                        )
                );

                productsListPanel.add(
                        Box.createVerticalStrut(8)
                );
            }
        }


        productsListPanel.revalidate();

        productsListPanel.repaint();
    }


    private JPanel createProductRow(
            Product product
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        row.setBackground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_INPUT
                        : CARD_BACKGROUND
        );

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                14,
                                12,
                                14
                        )
                )
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        72
                )
        );


        JPanel information =
                new JPanel();

        information.setOpaque(false);

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel name =
                new JLabel(
                        product.getName()
                );

        name.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        name.setForeground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );       

        boolean outOfStock =
                product.getQuantity() <= 0;

        JLabel details =
                new JLabel(
                        outOfStock
                        ? "Rs. " + product.getSellingPrice()
                            + "    •    OUT OF STOCK"
                        : "Rs. " + product.getSellingPrice()
                            + "    •    Stock: "
                            + product.getQuantity()
                );
        
        

        details.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        details.setForeground(
                outOfStock
                ? new Color(220, 95, 95)
                : AppearanceManager.isDarkMode()
                    ? AppearanceManager.DARK_SECONDARY
                    : TEXT_SECONDARY
        );



        information.add(name);

        information.add(
                Box.createVerticalStrut(4)
        );

        information.add(details);


        JButton addButton =
                createSecondaryButton(
                        "Add"
                );
        
        
        boolean dark =
                AppearanceManager.isDarkMode();

        addButton.setForeground(
                dark
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );

        addButton.setBackground(
                dark
                        ? AppearanceManager.DARK_RAISED
                        : Color.WHITE
        );

        addButton.setOpaque(true);
        addButton.setFocusPainted(false);

        addButton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                dark
                                        ? AppearanceManager.DARK_BORDER
                                        : BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                14,
                                7,
                                14
                        )
                )
        );

        addButton.setEnabled(
                product.getQuantity() > 0
        );

        // Cart behaviour comes in the next checkpoint.
        addButton.addActionListener(
                e -> addProductToCart(product)
        );


        row.add(
                information,
                BorderLayout.CENTER
        );

        row.add(
                addButton,
                BorderLayout.EAST
        );


        return row;
    }


    // ==========================================
    // CART PLACEHOLDER
    // ==========================================   
    
    private void addProductToCart(
            Product product
    ) {

        CartItem existingItem =
                cartItems.get(
                        product.getProductId()
                );


        if (existingItem == null) {

            if (product.getQuantity() <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "This product is out of stock.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            cartItems.put(
                    product.getProductId(),
                    new CartItem(
                            product,
                            1
                    )
            );

        } else {

            if (existingItem.getQuantity()
                    >= product.getQuantity()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Only "
                        + product.getQuantity()
                        + " unit(s) of "
                        + product.getName()
                        + " are available.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            existingItem.increaseQuantity();
        }


        refreshCart();
    }
    
    
    // ==========================================
    // REFRESH CART
    // ==========================================

    private void refreshCart() {

        cartItemsPanel.removeAll();


        if (cartItems.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "No products added yet.",
                            SwingConstants.CENTER
                    );

            emptyLabel.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            13
                    )
            );

            emptyLabel.setForeground(
                    TEXT_SECONDARY
            );

            emptyLabel.setAlignmentX(
                    CENTER_ALIGNMENT
            );


            cartItemsPanel.add(
                    Box.createVerticalStrut(30)
            );

            cartItemsPanel.add(
                    emptyLabel
            );

        } else {

            for (CartItem item
                    : cartItems.values()) {

                cartItemsPanel.add(
                        createCartItemRow(
                                item
                        )
                );

                cartItemsPanel.add(
                        Box.createVerticalStrut(8)
                );
            }
        }


        updateTotals();

        cartItemsPanel.revalidate();

        cartItemsPanel.repaint();
    }


    private JPanel createCartItemRow(
            CartItem item
    ) {

        Product product =
                item.getProduct();


        JPanel row =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        row.setBackground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_INPUT
                        : CARD_BACKGROUND
        );

        row.setBorder(
                BorderFactory.createCompoundBorder(

                        
                        BorderFactory.createLineBorder(
                                AppearanceManager.isDarkMode()
                                        ? AppearanceManager.DARK_BORDER
                                        : BORDER
                        ),
                        
                        
                        BorderFactory.createEmptyBorder(
                                12,
                                14,
                                12,
                                14
                        )
                )
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        78
                )
        );


        // ------------------------------------------
        // Product information
        // ------------------------------------------

        JPanel information =
                new JPanel();

        information.setOpaque(false);

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel nameLabel =
                new JLabel(
                        product.getName()
                );

        nameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        nameLabel.setForeground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );


        BigDecimal lineTotal =
                calculateLineTotal(
                        item
                );


        JLabel priceLabel =
                new JLabel(
                        formatMoney(
                                product.getSellingPrice()
                        )
                        + " each"
                );

        priceLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );


        priceLabel.setForeground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_SECONDARY
                        : TEXT_SECONDARY
        );

        information.add(
                nameLabel
        );

        information.add(
                Box.createVerticalStrut(4)
        );

        information.add(
                priceLabel
        );


        // ------------------------------------------
        // Quantity controls
        // ------------------------------------------

        JPanel quantityPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                6,
                                0
                        )
                );

        quantityPanel.setOpaque(false);


        JButton decreaseButton =
                createSmallButton("-");


        JLabel quantityLabel =
                new JLabel(
                        String.valueOf(
                                item.getQuantity()
                        ),
                        SwingConstants.CENTER
                );

        quantityLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        quantityLabel.setPreferredSize(
                new Dimension(
                        28,
                        30
                )
        );
        
        quantityLabel.setForeground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );


        JButton increaseButton =
                createSmallButton("+");


        JLabel lineTotalLabel =
                new JLabel(
                        formatMoney(
                                lineTotal
                        )
                );

        lineTotalLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        lineTotalLabel.setForeground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );

        lineTotalLabel.setPreferredSize(
                new Dimension(
                        100,
                        30
                )
        );

        lineTotalLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );


        JButton removeButton =
                createSmallButton(
                        "Remove"
                );


        decreaseButton.addActionListener(
                e -> decreaseCartQuantity(
                        item
                )
        );


        increaseButton.addActionListener(
                e -> increaseCartQuantity(
                        item
                )
        );


        removeButton.addActionListener(
                e -> removeCartItem(
                        item
                )
        );


        quantityPanel.add(
                decreaseButton
        );

        quantityPanel.add(
                quantityLabel
        );

        quantityPanel.add(
                increaseButton
        );

        quantityPanel.add(
                Box.createHorizontalStrut(6)
        );

        quantityPanel.add(
                lineTotalLabel
        );

        quantityPanel.add(
                Box.createHorizontalStrut(4)
        );

        quantityPanel.add(
                removeButton
        );


        row.add(
                information,
                BorderLayout.CENTER
        );

        row.add(
                quantityPanel,
                BorderLayout.EAST
        );


        return row;
    }


    private void increaseCartQuantity(
            CartItem item
    ) {

        Product product =
                item.getProduct();


        if (item.getQuantity()
                >= product.getQuantity()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only "
                    + product.getQuantity()
                    + " unit(s) of "
                    + product.getName()
                    + " are available.",
                    "Insufficient Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        item.increaseQuantity();

        refreshCart();
    }


    private void decreaseCartQuantity(
            CartItem item
    ) {

        if (item.getQuantity() <= 1) {

            removeCartItem(
                    item
            );

            return;
        }


        item.decreaseQuantity();

        refreshCart();
    }


    private void removeCartItem(
            CartItem item
    ) {

        cartItems.remove(
                item.getProduct()
                        .getProductId()
        );

        refreshCart();
    }

    private BigDecimal calculateDiscount(
            BigDecimal subtotal
    ) {

        BigDecimal enteredValue =
                readMoneyField(
                        discountField
                );


        if (enteredValue.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            return BigDecimal.ZERO;
        }


        String discountType =
                (String)
                discountTypeComboBox
                        .getSelectedItem();


        if ("Percent".equals(
                discountType
        )) {

            // Maximum percentage discount = 100%
            if (enteredValue.compareTo(
                    new BigDecimal("100")
            ) > 0) {

                enteredValue =
                        new BigDecimal("100");
            }


            return subtotal
                    .multiply(
                            enteredValue
                    )
                    .divide(
                            new BigDecimal("100"),
                            2,
                            RoundingMode.HALF_UP
                    );
        }


        // Amount discount
        if (enteredValue.compareTo(
                subtotal
        ) > 0) {

            return subtotal;
        }


        return enteredValue.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    private void updateTotals() {

        BigDecimal subtotal =
                calculateSubtotal();
        
        BigDecimal discount =
                calculateDiscount(
                        subtotal
                );


        BigDecimal total =
                subtotal.subtract(
                        discount
                );


        subtotalValueLabel.setText(
                formatMoney(
                        subtotal
                )
        );


        totalValueLabel.setText(
                formatMoney(
                        total
                )
        );


        updatePaymentDisplay(
                total
        );


        updateCompleteSaleButton(
                total
        );
    }
 
    private BigDecimal calculateSubtotal() {

        BigDecimal subtotal =
                BigDecimal.ZERO;


        for (CartItem item
                : cartItems.values()) {

            subtotal =
                    subtotal.add(
                            calculateLineTotal(
                                    item
                            )
                    );
        }


        return subtotal.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }


    private BigDecimal readMoneyField(
            PlaceholderTextField field
    ) {

        if (field == null) {
            return BigDecimal.ZERO;
        }


        String text =
                field.getText()
                        .trim();


        if (text.isEmpty()) {
            return BigDecimal.ZERO;
        }


        try {

            BigDecimal value =
                    new BigDecimal(text);


            if (value.compareTo(
                    BigDecimal.ZERO
            ) < 0) {

                return BigDecimal.ZERO;
            }


            return value;


        } catch (NumberFormatException e) {

            return BigDecimal.ZERO;
        }
    }


    private void updatePaymentDisplay(
            BigDecimal total
    ) {

        if (paymentMethodComboBox == null
                || amountReceivedField == null
                || changeValueLabel == null) {

            return;
        }


        String paymentMethod =
                (String)
                paymentMethodComboBox
                        .getSelectedItem();


        if ("CARD".equals(
                paymentMethod
        )) {

            amountReceivedField.setText(
                    total.setScale(
                            2,
                            RoundingMode.HALF_UP
                    ).toPlainString()
            );

            amountReceivedField.setEditable(
                    false
            );

            changeValueLabel.setText(
                    formatMoney(
                            BigDecimal.ZERO
                    )
            );

            return;
        }


        amountReceivedField.setEditable(
                true
        );


        BigDecimal amountReceived =
                readMoneyField(
                        amountReceivedField
                );


        BigDecimal change =
                amountReceived.subtract(
                        total
                );


        if (change.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            change =
                    BigDecimal.ZERO;
        }


        changeValueLabel.setText(
                formatMoney(
                        change
                )
        );
    }


    private void paymentMethodChanged() {

        if (paymentMethodComboBox == null) {
            return;
        }


        String paymentMethod =
                (String)
                paymentMethodComboBox
                        .getSelectedItem();


        if ("CASH".equals(
                paymentMethod
        )) {

            amountReceivedField.setEditable(
                    true
            );

            amountReceivedField.setText("");

        } else {

            amountReceivedField.setEditable(
                    false
            );
        }


        updateTotals();
    }


    private void updateCompleteSaleButton(
            BigDecimal total
    ) {

        if (completeSaleButton == null) {
            return;
        }


        if (cartItems.isEmpty()
                || total.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            completeSaleButton.setEnabled(
                    false
            );

            return;
        }


        String paymentMethod =
                (String)
                paymentMethodComboBox
                        .getSelectedItem();


        BigDecimal amount =
                readMoneyField(
                        amountReceivedField
                );


        boolean validPayment;


        if ("CARD".equals(
                paymentMethod
        )) {

            validPayment =
                    amount.compareTo(
                            total
                    ) == 0;

        } else {

            validPayment =
                    amount.compareTo(
                            total
                    ) >= 0;
        }


        completeSaleButton.setEnabled(
                validPayment
        );
    }
 
    private BigDecimal calculateLineTotal(
            CartItem item
    ) {

        return item.getProduct()
                .getSellingPrice()
                .multiply(
                        BigDecimal.valueOf(
                                item.getQuantity()
                        )
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }


    private String formatMoney(
            BigDecimal amount
    ) {

        if (amount == null) {
            amount =
                    BigDecimal.ZERO;
        }


        return "Rs. "
                + amount.setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }
 
    // ==========================================
    // COMPLETE SALE
    // ==========================================

    private void completeSale() {

        if (cartItems.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Add at least one product before completing the sale.",
                    "Empty Sale",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        Employee cashier =
                (Employee)
                cashierComboBox.getSelectedItem();

        if (cashier == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a cashier.",
                    "Cashier Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        BigDecimal subtotal =
                calculateSubtotal();

        BigDecimal discount =
                calculateDiscount(
                        subtotal
                );

        BigDecimal total =
                subtotal.subtract(
                        discount
                );


        String paymentMethod =
                (String)
                paymentMethodComboBox
                        .getSelectedItem();

        BigDecimal amountReceived =
                readMoneyField(
                        amountReceivedField
                );


        // ------------------------------------------
        // Validate payment
        // ------------------------------------------

        if ("CASH".equals(paymentMethod)
                && amountReceived.compareTo(total) < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Amount received is less than the sale total.",
                    "Insufficient Payment",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if ("CARD".equals(paymentMethod)
                && amountReceived.compareTo(total) != 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Card payment must match the sale total.",
                    "Invalid Payment",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // ------------------------------------------
        // Customer
        // ------------------------------------------

        CustomerOption selectedCustomer =
                (CustomerOption)
                customerComboBox.getSelectedItem();

        Integer customerId = null;

        if (selectedCustomer != null
                && selectedCustomer.getCustomer() != null) {

            customerId =
                    selectedCustomer
                            .getCustomer()
                            .getCustomerId();
        }


        // ------------------------------------------
        // Generate invoice number
        // ------------------------------------------

        String invoiceNumber =
                generateInvoiceNumber();


        // ------------------------------------------
        // Build Sale
        // ------------------------------------------

        Sale sale =
                new Sale();

        sale.setInvoiceNumber(
                invoiceNumber
        );

        sale.setCustomerId(
                customerId
        );

        sale.setEmployeeId(
                cashier.getEmployeeId()
        );

        sale.setSubtotal(
                subtotal
        );

        sale.setDiscount(
                discount
        );

        sale.setTotalAmount(
                total
        );

        sale.setStatus(
                "COMPLETED"
        );


        // ------------------------------------------
        // Build Sale Items
        // ------------------------------------------

        List<SaleItem> saleItems =
                new ArrayList<>();


        for (CartItem cartItem
                : cartItems.values()) {

            Product product =
                    cartItem.getProduct();


            SaleItem saleItem =
                    new SaleItem();

            saleItem.setProductId(
                    product.getProductId()
            );

            saleItem.setQuantity(
                    cartItem.getQuantity()
            );


            // Historical selling-price snapshot
            saleItem.setUnitPrice(
                    product.getSellingPrice()
            );


            // Historical cost-price snapshot
            saleItem.setUnitCost(
                    product.getCostPrice()
            );


            saleItem.setLineTotal(
                    product.getSellingPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            )
            );


            saleItems.add(
                    saleItem
            );
        }


        // ------------------------------------------
        // Build Payment
        // ------------------------------------------

        Payment payment =
                new Payment();

        payment.setPaymentMethod(
                paymentMethod
        );


        /*
         * We store the amount belonging to the sale,
         * not the customer's cash tendered.
         *
         * Example:
         * Total = Rs. 850
         * Customer gives = Rs. 1000
         * Payment recorded = Rs. 850
         * Change = Rs. 150
         */
        payment.setAmount(
                total
        );


        // ------------------------------------------
        // Final confirmation
        // ------------------------------------------

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Invoice: "
                        + invoiceNumber
                        + "\n\nSubtotal: "
                        + formatMoney(subtotal)
                        + "\nDiscount: "
                        + formatMoney(discount)
                        + "\nTotal: "
                        + formatMoney(total)
                        + "\nPayment: "
                        + paymentMethod
                        + "\n\nComplete this sale?",
                        "Confirm Sale",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (confirmation
                != JOptionPane.YES_OPTION) {

            return;
        }


        // ------------------------------------------
        // Process transaction
        // ------------------------------------------

        try {

            int saleId =
                    saleService.processSale(
                            sale,
                            saleItems,
                            payment
                    );


            JOptionPane.showMessageDialog(
                    this,
                    "Sale completed successfully."
                    + "\n\nInvoice: "
                    + invoiceNumber
                    + "\nSale ID: "
                    + saleId
                    + "\nTotal: "
                    + formatMoney(total),
                    "Sale Completed",
                    JOptionPane.INFORMATION_MESSAGE
            );


            resetSaleAfterCompletion();


        } catch (InsufficientStockException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
                    + "\n\nProduct stock may have changed "
                    + "while this sale was being prepared.",
                    "Insufficient Stock",
                    JOptionPane.WARNING_MESSAGE
            );


            // Reload current database quantities.
            loadProducts();


        } catch (SQLException e) {

            showDatabaseError(
                    "Unable to complete the sale.",
                    e
            );


        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Sale",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
    
    // ==========================================
    // INVOICE NUMBER GENERATOR
    // ==========================================
    
    private String generateInvoiceNumber() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyyMMddHHmmssSSS"
                );

        return "INV-"
                + LocalDateTime.now()
                        .format(formatter);
    }
    
    // ==========================================
    
    private void resetSaleAfterCompletion() {

        cartItems.clear();

        discountField.setText("");

        discountTypeComboBox.setSelectedItem(
                "Amount"
        );

        paymentMethodComboBox.setSelectedItem(
                "CASH"
        );

        amountReceivedField.setText("");

        productSearchField.setText("");


        // Reload products because stock quantities
        // have changed after the completed sale.
        loadProducts();


        refreshCart();
        if (saleCompletedListener != null) {
            saleCompletedListener.run();
        }
        
    }
    
    // ==========================================
    // REFRESH PANEL
    // ==========================================

    public void refreshPanel() {

        loadSalesData();

        if (productSearchField != null) {
            productSearchField.setText("");
        }
    }
    
    public void setSaleCompletedListener(Runnable listener) {
        this.saleCompletedListener = listener;
    }

    // ==========================================
    // SALES HISTORY
    // ==========================================

    private void openSalesHistory() {

        java.awt.Window window =
                javax.swing.SwingUtilities.getWindowAncestor(
                        this
                );

        java.awt.Frame parent =
                window instanceof java.awt.Frame
                ? (java.awt.Frame) window
                : null;


        SalesHistoryDialog dialog =
                new SalesHistoryDialog(
                        parent
                );

        dialog.setVisible(
                true
        );
    }

    // ==========================================
    // UI HELPERS
    // ==========================================

    private JPanel createCard() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                CARD_BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );


        return panel;
    }


    private JLabel createSectionTitle(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        label.setForeground(
                TEXT_PRIMARY
        );


        return label;
    }


    private JLabel createFieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                TEXT_SECONDARY
        );


        return label;
    }


    private JLabel createMoneyLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                TEXT_PRIMARY
        );


        return label;
    }

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                PRIMARY
        );

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        11,
                        18,
                        11,
                        18
                )
        );


        return button;
    }


    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        button.setForeground(
                TEXT_PRIMARY
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );


        return button;
    }

    private JButton createSmallButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        boolean dark =
                AppearanceManager.isDarkMode();

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        button.setForeground(
                dark
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );

        button.setBackground(
                dark
                        ? AppearanceManager.DARK_RAISED
                        : Color.WHITE
        );

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                dark
                                        ? AppearanceManager.DARK_BORDER
                                        : BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                9,
                                5,
                                9
                        )
                )
        );

        button.setMargin(
                new Insets(
                        4,
                        8,
                        4,
                        8
                )
        );

        return button;
    }


    
    private void showDatabaseError(
            String message,
            SQLException exception
    ) {

        JOptionPane.showMessageDialog(
                this,
                message
                + "\n"
                + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }


    // ==========================================
    // CUSTOMER COMBO OPTION
    // ==========================================

    private static class CustomerOption {

        private final Customer customer;
        private final String displayName;


        public CustomerOption(
                Customer customer,
                String displayName
        ) {

            this.customer =
                    customer;

            this.displayName =
                    displayName;
        }


        public Customer getCustomer() {
            return customer;
        }


        @Override
        public String toString() {
            return displayName;
        }
    }
    
    private static class CartItem {

        private final Product product;
        private int quantity;


        public CartItem(
                Product product,
                int quantity
        ) {

            this.product =
                    product;

            this.quantity =
                    quantity;
        }


        public Product getProduct() {
            return product;
        }


        public int getQuantity() {
            return quantity;
        }


        public void increaseQuantity() {
            quantity++;
        }


        public void decreaseQuantity() {

            if (quantity > 1) {
                quantity--;
            }
        }
    }
}