/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */
import com.supermart.dao.SaleDAO;
import com.supermart.view.component.SuperMartTableStyle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.SQLException;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;


public class SaleDetailsDialog extends JDialog {

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


    private final int saleId;
    private final String invoiceNumber;
    private final String date;
    private final String customer;
    private final String cashier;
    private final BigDecimal subtotal;
    private final BigDecimal discount;
    private final BigDecimal total;
    private final String paymentMethod;
    private final String status;

    private final SaleDAO saleDAO;

    private DefaultTableModel tableModel;


    public SaleDetailsDialog(
            Window parent,
            int saleId,
            String invoiceNumber,
            String date,
            String customer,
            String cashier,
            BigDecimal subtotal,
            BigDecimal discount,
            BigDecimal total,
            String paymentMethod,
            String status
    ) {

        super(
                parent,
                "Sale Details",
                ModalityType.APPLICATION_MODAL
        );

        this.saleId =
                saleId;

        this.invoiceNumber =
                invoiceNumber;

        this.date =
                date;

        this.customer =
                customer;

        this.cashier =
                cashier;
        
        this.subtotal =
                subtotal;

        this.discount =
                discount;

        this.total =
                total;

        this.paymentMethod =
                paymentMethod;

        this.status =
                status;

        this.saleDAO =
                new SaleDAO();


        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setSize(
                850,
                600
        );

        setMinimumSize(
                new Dimension(
                        720,
                        480
                )
        );

        setLocationRelativeTo(
                parent
        );


        createInterface();

        loadItems();
    }


    private void createInterface() {

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        root.setBackground(
                BACKGROUND
        );

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        28,
                        32,
                        28,
                        32
                )
        );


        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        root.add(
                createItemsCard(),
                BorderLayout.CENTER
        );

        root.add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );


        setContentPane(
                root
        );
    }


    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        panel.setOpaque(false);


        JPanel information =
                new JPanel();

        information.setOpaque(false);

        information.setLayout(
                new javax.swing.BoxLayout(
                        information,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Sale Details"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(
                TEXT_PRIMARY
        );


        JLabel invoiceLabel =
                createInformationLabel(
                        "Invoice: "
                        + invoiceNumber
                );


        JLabel dateLabel =
                createInformationLabel(
                        "Date: "
                        + date
                );


        JLabel customerLabel =
                createInformationLabel(
                        "Customer: "
                        + customer
                );


        JLabel cashierLabel =
                createInformationLabel(
                        "Cashier: "
                        + cashier
                );


        information.add(
                title
        );

        information.add(
                javax.swing.Box.createVerticalStrut(
                        10
                )
        );

        information.add(
                invoiceLabel
        );

        information.add(
                javax.swing.Box.createVerticalStrut(
                        3
                )
        );

        information.add(
                dateLabel
        );

        information.add(
                javax.swing.Box.createVerticalStrut(
                        3
                )
        );

        information.add(
                customerLabel
        );

        information.add(
                javax.swing.Box.createVerticalStrut(
                        3
                )
        );

        information.add(
                cashierLabel
        );


        JButton closeButton =
                new JButton(
                        "Close"
                );

        closeButton.addActionListener(
                e -> dispose()
        );


        JPanel closeWrapper =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        closeWrapper.setOpaque(false);

        closeWrapper.add(
                closeButton
        );


        panel.add(
                information,
                BorderLayout.CENTER
        );

        panel.add(
                closeWrapper,
                BorderLayout.EAST
        );


        return panel;
    }


    private JPanel createItemsCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        card.setBackground(
                CARD_BACKGROUND
        );

        card.setBorder(
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


        JLabel title =
                new JLabel(
                        "Purchased Items"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );


        String[] columns = {
            "Product",
            "Qty",
            "Unit Price",
            "Line Total"
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
                };


        JTable table =
                new JTable(
                        tableModel
                );


        SuperMartTableStyle.apply(
                table
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        table
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        card.add(
                title,
                BorderLayout.NORTH
        );

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );


        return card;
    }
    
    private JPanel createBottomPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        panel.setOpaque(false);


        JLabel payment =
                createInformationLabel(
                        "Payment: "
                        + paymentMethod
                        + "    •    Status: "
                        + status
                );


        JPanel totalsPanel =
                new JPanel();

        totalsPanel.setOpaque(false);

        totalsPanel.setLayout(
                new javax.swing.BoxLayout(
                        totalsPanel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );


        JLabel subtotalLabel =
                new JLabel(
                        "Subtotal: "
                        + formatMoney(
                                subtotal
                        )
                );

        subtotalLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        JLabel discountLabel =
                new JLabel(
                        "Discount: "
                        + formatMoney(
                                discount
                        )
                );

        discountLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );


        JLabel totalLabel =
                new JLabel(
                        "Total: "
                        + formatMoney(
                                total
                        )
                );

        totalLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        totalLabel.setForeground(
                TEXT_PRIMARY
        );


        subtotalLabel.setAlignmentX(
                RIGHT_ALIGNMENT
        );

        discountLabel.setAlignmentX(
                RIGHT_ALIGNMENT
        );

        totalLabel.setAlignmentX(
                RIGHT_ALIGNMENT
        );


        totalsPanel.add(
                subtotalLabel
        );

        totalsPanel.add(
                javax.swing.Box.createVerticalStrut(
                        3
                )
        );

        totalsPanel.add(
                discountLabel
        );

        totalsPanel.add(
                javax.swing.Box.createVerticalStrut(
                        5
                )
        );

        totalsPanel.add(
                totalLabel
        );


        panel.add(
                payment,
                BorderLayout.WEST
        );

        panel.add(
                totalsPanel,
                BorderLayout.EAST
        );


        return panel;
    }
    
    private void loadItems() {

        try {

            List<Object[]> items =
                    saleDAO.getSaleItemsBySaleId(
                            saleId
                    );


            tableModel.setRowCount(
                    0
            );


            for (Object[] item : items) {

                tableModel.addRow(
                        new Object[]{
                            item[1],
                            item[2],
                            formatMoney(
                                    (BigDecimal) item[3]
                            ),
                            formatMoney(
                                    (BigDecimal) item[5]
                            )
                        }
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load sale items."
                    + "\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private JLabel createInformationLabel(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
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


        return label;
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
}