package com.supermart.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

    // ---------- Application colours ----------
    private static final Color BACKGROUND = new Color(247, 248, 250);
    private static final Color SIDEBAR_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(25, 25, 28);
    private static final Color TEXT_SECONDARY = new Color(110, 113, 120);
    private static final Color ACTIVE_BACKGROUND = new Color(239, 240, 243);
    private static final Color BORDER = new Color(230, 232, 235);

    // ---------- Main content switching ----------
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final DashboardPanel dashboardPanel;
    private final ProductsPanel productsPanel;
    private final InventoryPanel inventoryPanel;
    private final CustomersPanel customersPanel;
    private final SuppliersPanel suppliersPanel;
    private final EmployeesPanel employeesPanel;
    
    private JButton selectedNavigationButton;
    

    public MainFrame() {

        setTitle("SuperMart - Sales & Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(1050, 650));
        setSize(1280, 760); 
        setMinimumSize(
                new Dimension(
                        1050, //900
                        650
                )
        );
        
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Main content
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND);
        
        
        // ==========================================
        // CREATE APPLICATION PANELS
        // ==========================================

        dashboardPanel =
                new DashboardPanel();

        productsPanel =
                new ProductsPanel();

        inventoryPanel =
                new InventoryPanel();

        customersPanel =
                new CustomersPanel();
        
        suppliersPanel =
                new SuppliersPanel();
        employeesPanel =
                new EmployeesPanel();


        // ==========================================
        // REGISTER PANELS WITH CARDLAYOUT
        // ==========================================

        contentPanel.add(
                dashboardPanel,
                "DASHBOARD"
        );

        contentPanel.add(
                productsPanel,
                "PRODUCTS"
        );

        contentPanel.add(
                inventoryPanel,
                "INVENTORY"
        );

        contentPanel.add(
                customersPanel,
                "CUSTOMERS"
        );
        
        
        contentPanel.add(
                suppliersPanel,
                "SUPPLIERS"
        );
        
        contentPanel.add(
                employeesPanel,
                "EMPLOYEES"
        );
  
        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(220, 0));

        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 0, 1, BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                28, 18, 24, 18
                        )
                )
        );

        // ---------- Logo ----------
        JLabel logo = new JLabel("SUPERMART");
        logo.setFont(new Font("SansSerif", Font.BOLD, 21));
        logo.setForeground(TEXT_PRIMARY);
        logo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel logoSubtitle = new JLabel("Retail Management");
        logoSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        logoSubtitle.setForeground(TEXT_SECONDARY);
        logoSubtitle.setAlignmentX(LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(3));
        sidebar.add(logoSubtitle);

        sidebar.add(Box.createVerticalStrut(35));

        // ---------- Navigation ----------
        JButton dashboardButton =
                createNavigationButton("Dashboard");
        
        JButton salesButton =
                createNavigationButton("Sales");
        
        

        JButton productsButton =
                createNavigationButton("Products");

        JButton inventoryButton =
                createNavigationButton("Inventory");

        JButton customersButton =
                createNavigationButton("Customers");

        JButton suppliersButton =
                createNavigationButton("Suppliers");

        JButton employeesButton =
                createNavigationButton("Employees");

        JButton reportsButton =
                createNavigationButton("Reports");

        JButton settingsButton =
                createNavigationButton("Settings");
        
        // ==========================================
        // ADD BUTTON ACTIONS 
        // ==========================================
        
        dashboardButton.addActionListener(e -> {
            
            dashboardPanel.loadDashboardData();
            
            cardLayout.show(
                    contentPanel,
                    "DASHBOARD"
            );

            selectNavigationButton(
                    dashboardButton
            );
        });

        inventoryButton.addActionListener(e -> {

            inventoryPanel.refreshPanel();

            cardLayout.show(
                    contentPanel,
                    "INVENTORY"
            );

            selectNavigationButton(
                    inventoryButton
            );
        });
        
        productsButton.addActionListener(e -> {

            productsPanel.refreshPanel();

            cardLayout.show(
                    contentPanel,
                    "PRODUCTS"
            );

            selectNavigationButton(
                    productsButton
            );
        });        
        
        customersButton.addActionListener(e -> {

            customersPanel.refreshPanel();

            cardLayout.show(
                    contentPanel,
                    "CUSTOMERS"
            );

            selectNavigationButton(
                    customersButton
            );
        });
    
        suppliersButton.addActionListener(e -> {

            suppliersPanel.refreshPanel();

            cardLayout.show(
                    contentPanel,
                    "SUPPLIERS"
            );

            selectNavigationButton(
                    suppliersButton
            );
        });
        
        employeesButton.addActionListener(e -> {

            employeesPanel.refreshPanel();

            cardLayout.show(
                    contentPanel,
                    "EMPLOYEES"
            );

            selectNavigationButton(
                    employeesButton
            );
        });
        
        


        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(salesButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(productsButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(inventoryButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(customersButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(suppliersButton);
        sidebar.add(Box.createVerticalStrut(5));

        sidebar.add(employeesButton);

        // Push Reports/Settings toward bottom
        sidebar.add(Box.createVerticalGlue());

        sidebar.add(reportsButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(settingsButton);
        
        // ==============================
        // DEFAULT SELECTED BUTTON
        // ==============================
        selectNavigationButton(
                dashboardButton
        );

        return sidebar;
    }

    private JButton createNavigationButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        button.setPreferredSize(
                new Dimension(
                        180,
                        42
                )
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setBackground(
                SIDEBAR_BACKGROUND
        );

        button.setForeground(
                TEXT_SECONDARY
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setMargin(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );

        return button;
    }
    
    private void selectNavigationButton(JButton button) {

        // Reset the previously selected button
        if (selectedNavigationButton != null) {

            selectedNavigationButton.setBackground(
                    SIDEBAR_BACKGROUND
            );

            selectedNavigationButton.setForeground(
                    TEXT_SECONDARY
            );

            selectedNavigationButton.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            14
                    )
            );
        }

        // Highlight the newly selected button
        button.setBackground(
                ACTIVE_BACKGROUND
        );

        button.setForeground(
                TEXT_PRIMARY
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        selectedNavigationButton = button;
    }

}