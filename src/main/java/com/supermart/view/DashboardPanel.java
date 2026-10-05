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

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    private JLabel productsValueLabel;
    private JLabel lowStockValueLabel;

    private JPanel lowStockContentPanel;

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

        statisticsPanel.add(
                createStatCard(
                        "Today's Sales",
                        "Rs. 0.00",
                        "Total revenue today"
                )
        );

        statisticsPanel.add(
                createStatCard(
                        "Transactions",
                        "0",
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
        JPanel lowerSection =
                new JPanel(new GridLayout(1, 2, 18, 0));

        lowerSection.setOpaque(false);
        lowerSection.setAlignmentX(LEFT_ALIGNMENT);

        lowerSection.add(
                createSectionCard(
                        "Recent Sales",
                        "No sales have been recorded yet."
                )
        );     
        
        lowerSection.add(
                createLowStockSection()
        );
        
        
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

            // ---------------------------------
            // Statistics
            // ---------------------------------

            int productCount =
                    dashboardDAO
                            .getActiveProductCount();

            int lowStockCount =
                    dashboardDAO
                            .getLowStockCount();


            productsValueLabel.setText(
                    String.valueOf(productCount)
            );

            lowStockValueLabel.setText(
                    String.valueOf(lowStockCount)
            );


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

                    JLabel productLabel =
                            new JLabel(
                                    product.getName()
                                            + "     "
                                            + product.getQuantity()
                                            + " remaining"
                            );

                    productLabel.setFont(
                            new Font(
                                    "SansSerif",
                                    Font.PLAIN,
                                    13
                            )
                    );

                    productLabel.setForeground(
                            TEXT_SECONDARY
                    );

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