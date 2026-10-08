/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package com.supermart.view;

/**
 *
 * @author pabodini
 */

package com.supermart.view;

import com.supermart.dao.DashboardDAO;
import com.supermart.model.Product;
import com.supermart.util.AppearanceManager;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DashboardPanel extends JPanel {

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
    
    
    private final DashboardDAO dashboardDAO;

    private JLabel todaySalesValueLabel;
    private JLabel transactionsValueLabel;
    
    private JLabel productsValueLabel;
    private JLabel lowStockValueLabel;
    private JPanel recentSalesContentPanel;

    private JPanel lowStockContentPanel;
    private JPanel outOfStockContentPanel;

    public DashboardPanel() {
        
        dashboardDAO = new DashboardDAO();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        38, 42, 38, 42
                )
        );

        JPanel dashboardContent = new JPanel();
        dashboardContent.setOpaque(false);

        dashboardContent.setLayout(
                new BoxLayout(
                        dashboardContent,
                        BoxLayout.Y_AXIS
                )
        );

        // ---------- Header ----------
        JLabel title = new JLabel("Dashboard");
        title.setFont(
                new Font("SansSerif", Font.BOLD, 30)
        );
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle =
                new JLabel(
                        "Here's what's happening in your store today."
                );

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        dashboardContent.add(title);
        dashboardContent.add(Box.createVerticalStrut(7));
        dashboardContent.add(subtitle);

        dashboardContent.add(
                Box.createVerticalStrut(32)
        );

        // ---------- Statistics ----------
        JPanel statisticsPanel =
                new JPanel(new GridLayout(1, 4, 16, 0));

        statisticsPanel.setOpaque(false);
        statisticsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        125
                )
        );

        statisticsPanel.setAlignmentX(LEFT_ALIGNMENT);

        todaySalesValueLabel =
                createValueLabel("Rs. 0.00");

        statisticsPanel.add(
                createDynamicStatCard(
                        "Today's Sales",
                        todaySalesValueLabel,
                        "Net revenue today"
                )
        );


        transactionsValueLabel =
                createValueLabel("0");

        statisticsPanel.add(
                createDynamicStatCard(
                        "Transactions",
                        transactionsValueLabel,
                        "Sales completed today"
                )
        );
        
        
        productsValueLabel = createValueLabel("0");

        statisticsPanel.add(
                createDynamicStatCard(
                        "Active Products",
                        productsValueLabel,
                        "Available for sale" //Products in inventory
                )
        );


        lowStockValueLabel = createValueLabel("0");

        statisticsPanel.add(
                createDynamicStatCard(
                        "Low Stock",
                        lowStockValueLabel,
                        "Needs attention"
                )
        );

        

        dashboardContent.add(statisticsPanel);

        dashboardContent.add(
                Box.createVerticalStrut(28)
        );

        // ---------- Lower dashboard ----------
        
        JPanel lowerSection = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 2.0;
        gbc.insets = new Insets(0, 0, 0, 16);
        lowerSection.add(createRecentSalesSection(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 16);
        lowerSection.add(createLowStockSection(), gbc);

        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        lowerSection.add(createOutOfStockSection(), gbc);

        lowerSection.setOpaque(false);
        lowerSection.setAlignmentX(LEFT_ALIGNMENT);

        dashboardContent.add(lowerSection);

        add(dashboardContent, BorderLayout.NORTH);
        
        loadDashboardData();
    }

    private JPanel createStatCard(
            String title,
            String value,
            String description
    ) {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(CARD_BACKGROUND);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                18, 20, 18, 20
                        )
                )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        titleLabel.setForeground(TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(
                new Font("SansSerif", Font.BOLD, 24)
        );
        valueLabel.setForeground(TEXT_PRIMARY);

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 11)
        );

        descriptionLabel.setForeground(TEXT_SECONDARY);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(descriptionLabel);

        return card;
    }

    private JPanel createRecentSalesSection() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                CARD_BACKGROUND
        );

        card.setPreferredSize(
                new Dimension(0, 300) //220
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 22, 20, 22
                        )
                )
        );


        JLabel titleLabel =
                new JLabel("Recent Sales");

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setForeground(
                TEXT_PRIMARY
        );


        JPanel header =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        header.setOpaque(false);

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 10, 0
                )
        );

        header.add(titleLabel);


        recentSalesContentPanel =
                new JPanel();

        recentSalesContentPanel.setOpaque(false);
        
        
        recentSalesContentPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12
                )
        );

        recentSalesContentPanel.setLayout(
                new BoxLayout(
                        recentSalesContentPanel,
                        BoxLayout.Y_AXIS
                )
        );


        card.add(
                header,
                BorderLayout.NORTH
        );

        JScrollPane recentSalesScrollPane =
                new JScrollPane(recentSalesContentPanel);

        recentSalesScrollPane.setBorder(null);
        recentSalesScrollPane.setOpaque(false);
        recentSalesScrollPane.getViewport().setOpaque(false);

        recentSalesScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        recentSalesScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        recentSalesScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(
                recentSalesScrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    private JPanel createLowStockSection() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                CARD_BACKGROUND
        );

        card.setPreferredSize(
                new Dimension(0, 220)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 22, 20, 22
                        )
                )
        );


        JLabel titleLabel =
                new JLabel("Low Stock");

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setForeground(
                TEXT_PRIMARY
        );


        JPanel header =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        header.setOpaque(false);

        header.add(titleLabel);


        lowStockContentPanel =
                new JPanel();

        lowStockContentPanel.setOpaque(false);

        lowStockContentPanel.setLayout(
                new BoxLayout(
                        lowStockContentPanel,
                        BoxLayout.Y_AXIS
                )
        );


        card.add(
                header,
                BorderLayout.NORTH
        );

        card.add(
                lowStockContentPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    private JPanel createOutOfStockSection() {

        JPanel card = new JPanel(new BorderLayout());

        card.setBackground(CARD_BACKGROUND);
        card.setPreferredSize(new Dimension(0, 220));

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                20, 22, 20, 22
                        )
                )
        );

        JLabel titleLabel = new JLabel("Out of Stock");

        titleLabel.setFont(
                new Font("SansSerif", Font.BOLD, 16)
        );

        titleLabel.setForeground(TEXT_PRIMARY);

        JPanel header = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        header.setOpaque(false);
        header.add(titleLabel);

        outOfStockContentPanel = new JPanel();

        outOfStockContentPanel.setOpaque(false);

        outOfStockContentPanel.setLayout(
                new BoxLayout(
                        outOfStockContentPanel,
                        BoxLayout.Y_AXIS
                )
        );

        card.add(header, BorderLayout.NORTH);
        card.add(
                outOfStockContentPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    
    private JLabel createValueLabel(
            String initialValue
    ) {

        JLabel label =
                new JLabel(initialValue);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        label.setForeground(TEXT_PRIMARY);

        return label;
    }
    private JPanel createDynamicStatCard(
            String title,
            JLabel valueLabel,
            String description
    ) {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(CARD_BACKGROUND);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                18, 20, 18, 20
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        titleLabel.setForeground(
                TEXT_SECONDARY
        );


        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        descriptionLabel.setForeground(
                TEXT_SECONDARY
        );


        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(valueLabel);

        card.add(
                Box.createVerticalStrut(7)
        );

        card.add(descriptionLabel);

        return card;
    }
    private JPanel createSectionCard(
            String title,
            String content
    ) {

        JPanel card = new JPanel(
                new BorderLayout()
        );

        card.setBackground(CARD_BACKGROUND);

        card.setPreferredSize(
                new Dimension(0, 220)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                20, 22, 20, 22
                        )
                )
        );

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
                new Font("SansSerif", Font.BOLD, 16)
        );

        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel contentLabel = new JLabel(content);

        contentLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        contentLabel.setForeground(TEXT_SECONDARY);

        JPanel header =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                ));

        header.setOpaque(false);
        header.add(titleLabel);

        card.add(header, BorderLayout.NORTH);
        card.add(contentLabel, BorderLayout.CENTER);

        return card;
    }

    public final void loadDashboardData() {

        try {
            
            
            java.math.BigDecimal todaySales =
                    dashboardDAO.getTodaySalesTotal();

            int todayTransactions =
                    dashboardDAO.getTodayTransactionCount();

            List<String> recentSales =
                    dashboardDAO.getRecentSales();

            // ---------------------------------
            // Statistics
            // ---------------------------------

            int productCount =
                    dashboardDAO
                            .getActiveProductCount();

            int lowStockCount =
                    dashboardDAO
                            .getLowStockCount();

            
            
            todaySalesValueLabel.setText(
                    "Rs. "
                    + todaySales.setScale(
                            2,
                            java.math.RoundingMode.HALF_UP
                    )
            );

            transactionsValueLabel.setText(
                    String.valueOf(todayTransactions)
            );
            
            

            productsValueLabel.setText(
                    String.valueOf(productCount)
            );

            lowStockValueLabel.setText(
                    String.valueOf(lowStockCount)
            );

            // ---------------------------------
            // Recent sales
            // ---------------------------------

            recentSalesContentPanel.removeAll();

            if (recentSales.isEmpty()) {

                JLabel emptyLabel =
                        new JLabel(
                                "No sales have been recorded yet."
                        );

                emptyLabel.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                13
                        )
                );

                emptyLabel.setForeground(
                        AppearanceManager.isDarkMode()
                                ? AppearanceManager.DARK_SECONDARY
                                : TEXT_SECONDARY
                );

                emptyLabel.setBorder(
                        BorderFactory.createEmptyBorder(
                                18, 0, 0, 0
                        )
                );

                recentSalesContentPanel.add(
                        emptyLabel
                );

            } else {

                for (String sale : recentSales) {

                    String[] parts = sale.split("\\s+•\\s+", 4);

                    JPanel saleRow = new JPanel();
                    saleRow.setOpaque(false);
                    saleRow.setLayout(
                            new BoxLayout(saleRow, BoxLayout.Y_AXIS)
                    );
                    saleRow.setAlignmentX(LEFT_ALIGNMENT);

                    Color primary = AppearanceManager.isDarkMode()
                            ? AppearanceManager.DARK_TEXT
                            : TEXT_PRIMARY;

                    Color secondary = AppearanceManager.isDarkMode()
                            ? AppearanceManager.DARK_SECONDARY
                            : TEXT_SECONDARY;

                    String firstLine;
                    String secondLine;

                    if (parts.length == 4) {
                        firstLine = parts[0] + "  •  " + parts[2];
                        secondLine = parts[1] + "  •  " + parts[3];
                    } else {
                        firstLine = sale;
                        secondLine = "";
                    }

                    JLabel mainLabel = new JLabel(firstLine);
                    mainLabel.setFont(
                            new Font("SansSerif", Font.BOLD, 12)
                    );
                    mainLabel.setForeground(primary);

                    JLabel detailLabel = new JLabel(secondLine);
                    detailLabel.setFont(
                            new Font("SansSerif", Font.PLAIN, 11)
                    );
                    detailLabel.setForeground(secondary);

                    saleRow.add(mainLabel);

                    if (!secondLine.isEmpty()) {
                        saleRow.add(Box.createVerticalStrut(3));
                        saleRow.add(detailLabel);
                    }

                    recentSalesContentPanel.add(saleRow);
                    recentSalesContentPanel.add(
                            Box.createVerticalStrut(10)
                    );
                }
            
            }

            recentSalesContentPanel.revalidate();
            recentSalesContentPanel.repaint();

            // ---------------------------------
            // Low-stock products
            // ---------------------------------

            List<Product> lowStockProducts =
                    dashboardDAO
                            .getLowStockProducts();


            lowStockContentPanel.removeAll();


            if (lowStockProducts.isEmpty()) {

                JLabel emptyLabel =
                        new JLabel(
                                "All products are sufficiently stocked."
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

                emptyLabel.setBorder(
                        BorderFactory.createEmptyBorder(
                                18, 0, 0, 0
                        )
                );

                lowStockContentPanel.add(
                        emptyLabel
                );

            } else {

                lowStockContentPanel.add(
                        Box.createVerticalStrut(14)
                );

                for (Product product
                        : lowStockProducts) {

                    boolean outOfStock =
                            product.getQuantity() <= 0;

                    String stockText =
                            outOfStock
                                    ? "OUT OF STOCK"
                                    : product.getQuantity()
                                            + " remaining";

                    JLabel productLabel =
                            new JLabel(
                                    product.getName()
                                            + "     •     "
                                            + stockText
                            );

                    productLabel.setFont(
                            new Font(
                                    "SansSerif",
                                    Font.PLAIN,
                                    13
                            )
                    );
                    
                    if (outOfStock) {

                        productLabel.setForeground(
                                new Color(210, 82, 82)
                        );

                        productLabel.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.BOLD,
                                        13
                                )
                        );

                    } else {

                        productLabel.setForeground(
                                AppearanceManager.isDarkMode()
                                        ? AppearanceManager.DARK_SECONDARY
                                        : TEXT_SECONDARY
                        );

                        productLabel.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.PLAIN,
                                        13
                                )
                        );
                    }

                    lowStockContentPanel.add(
                            productLabel
                    );

                    lowStockContentPanel.add(
                            Box.createVerticalStrut(8)
                    );
                }
            }


            lowStockContentPanel.revalidate();
            lowStockContentPanel.repaint();
            
            // ---------- Out-of-stock products ----------

            List<Product> outOfStockProducts =
                    dashboardDAO.getOutOfStockProducts();

            outOfStockContentPanel.removeAll();

            if (outOfStockProducts.isEmpty()) {

                JLabel emptyLabel =
                        new JLabel("No products are out of stock.");

                emptyLabel.setFont(
                        new Font("SansSerif", Font.PLAIN, 13)
                );

                emptyLabel.setForeground(
                        AppearanceManager.isDarkMode()
                                ? AppearanceManager.DARK_SECONDARY
                                : TEXT_SECONDARY
                );

                emptyLabel.setBorder(
                        BorderFactory.createEmptyBorder(
                                18, 0, 0, 0
                        )
                );

                outOfStockContentPanel.add(emptyLabel);

            } else {

                outOfStockContentPanel.add(
                        Box.createVerticalStrut(14)
                );

                for (Product product : outOfStockProducts) {

                    JLabel productLabel = new JLabel(
                            product.getName() + "  •  OUT OF STOCK"
                    );

                    productLabel.setFont(
                            new Font(
                                    "SansSerif",
                                    Font.BOLD,
                                    13
                            )
                    );

                    productLabel.setForeground(
                            AppearanceManager.isDarkMode()
                                    ? new Color(235, 125, 125)
                                    : new Color(175, 55, 55)
                    );

                    outOfStockContentPanel.add(productLabel);

                    outOfStockContentPanel.add(
                            Box.createVerticalStrut(9)
                    );
                }
            }

            outOfStockContentPanel.revalidate();
            outOfStockContentPanel.repaint();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load dashboard data.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}