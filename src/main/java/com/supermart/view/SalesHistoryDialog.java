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
import com.supermart.view.component.PlaceholderTextField;
import com.supermart.view.component.SuperMartTableStyle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class SalesHistoryDialog extends JDialog {

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


    private final SaleDAO saleDAO;

    private PlaceholderTextField searchField;

    private JComboBox<String> paymentFilter;
    private JComboBox<String> statusFilter;

    private JTable salesTable;

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;

    private JButton viewSaleButton;


    public SalesHistoryDialog(
            Frame parent
    ) {

        super(
                parent,
                "Sales History",
                true
        );

        saleDAO =
                new SaleDAO();


        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setSize(
                1050,
                650
        );

        setMinimumSize(
                new Dimension(
                        850,
                        500
                )
        );

        setLocationRelativeTo(
                parent
        );


        createInterface();

        loadSalesHistory();
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
                createTableCard(),
                BorderLayout.CENTER
        );


        setContentPane(
                root
        );
    }


    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);


        JPanel titlePanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                3
                        )
                );

        titlePanel.setOpaque(false);


        JLabel title =
                new JLabel(
                        "Sales History"
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


        JLabel subtitle =
                new JLabel(
                        "Review completed sales and transaction details."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                TEXT_SECONDARY
        );


        titlePanel.add(
                title,
                BorderLayout.NORTH
        );

        titlePanel.add(
                subtitle,
                BorderLayout.SOUTH
        );


        JButton closeButton =
                new JButton(
                        "Close"
                );

        closeButton.setFocusPainted(
                false
        );

        closeButton.addActionListener(
                e -> dispose()
        );


        panel.add(
                titlePanel,
                BorderLayout.WEST
        );

        panel.add(
                closeButton,
                BorderLayout.EAST
        );


        return panel;
    }


    private JPanel createTableCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                14
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


        card.add(
                createFilters(),
                BorderLayout.NORTH
        );


        String[] columns = {
            "Sale ID",
            "Invoice",
            "Date",
            "Customer",
            "Cashier",
            "Total",
            "Payment",
            "Status"
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


        salesTable =
                new JTable(
                        tableModel
                );


        SuperMartTableStyle.apply(
                salesTable
        );


        tableSorter =
                new TableRowSorter<>(
                        tableModel
                );

        salesTable.setRowSorter(
                tableSorter
        );


        salesTable.getSelectionModel()
                .addListSelectionListener(
                        e -> updateViewButton()
                );


        JScrollPane scrollPane =
                new JScrollPane(
                        salesTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        scrollPane.getViewport()
                .setBackground(
                        Color.WHITE
                );


        card.add(
                scrollPane,
                BorderLayout.CENTER
        );


        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        bottom.setOpaque(false);


        viewSaleButton =
                new JButton(
                        "View Sale"
                );

        viewSaleButton.setEnabled(
                false
        );

        /*
         * We will connect the detailed receipt
         * in the next checkpoint.
         */
        viewSaleButton.addActionListener(
                e -> viewSelectedSale()
        );


        bottom.add(
                viewSaleButton
        );


        card.add(
                bottom,
                BorderLayout.SOUTH
        );


        return card;
    }


    private JPanel createFilters() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);


        searchField =
                new PlaceholderTextField(
                        "Search invoice, customer or cashier...",
                        24
                );

        searchField.setPreferredSize(
                new Dimension(
                        280,
                        34
                )
        );


        paymentFilter =
                new JComboBox<>(
                        new String[]{
                            "All Payments",
                            "CASH",
                            "CARD"
                        }
                );

        paymentFilter.setPreferredSize(
                new Dimension(
                        145,
                        34
                )
        );


        statusFilter =
                new JComboBox<>(
                        new String[]{
                            "All Statuses",
                            "COMPLETED",
                            "PARTIALLY_REFUNDED",
                            "REFUNDED"
                        }
                );

        statusFilter.setPreferredSize(
                new Dimension(
                        190,
                        34
                )
        );


        searchField.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            private void changed() {
                                applyFilters();
                            }

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                changed();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                changed();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e
                            ) {
                                changed();
                            }
                        }
                );


        paymentFilter.addActionListener(
                e -> applyFilters()
        );

        statusFilter.addActionListener(
                e -> applyFilters()
        );


        panel.add(
                searchField
        );

        panel.add(
                paymentFilter
        );

        panel.add(
                statusFilter
        );


        return panel;
    }


    private void loadSalesHistory() {

        try {

            List<Object[]> sales =
                    saleDAO.getSalesHistory();


            tableModel.setRowCount(
                    0
            );


            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd  HH:mm"
                    );


            for (Object[] sale : sales) {

                Timestamp timestamp =
                        (Timestamp)
                        sale[2];


                String formattedDate =
                        timestamp == null
                        ? "-"
                        : timestamp
                                .toLocalDateTime()
                                .format(
                                        formatter
                                );

                tableModel.addRow(
                        new Object[]{
                            sale[0],
                            sale[1],
                            formattedDate,
                            sale[3],
                            sale[4],
                            sale[7],
                            sale[8],
                            sale[9]
                        }
                );

            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load sales history."
                    + "\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void applyFilters() {

        if (tableSorter == null) {
            return;
        }


        String search =
                searchField.getText()
                        .trim();


        String payment =
                (String)
                paymentFilter.getSelectedItem();


        String status =
                (String)
                statusFilter.getSelectedItem();


        List<RowFilter<Object, Object>>
                filters =
                new java.util.ArrayList<>();


        if (!search.isEmpty()) {

            filters.add(
                    RowFilter.regexFilter(
                            "(?i)"
                            + java.util.regex.Pattern.quote(
                                    search
                            ),
                            1,
                            3,
                            4
                    )
            );
        }


        if (!"All Payments".equals(
                payment
        )) {

            filters.add(
                    RowFilter.regexFilter(
                            "^"
                            + java.util.regex.Pattern.quote(
                                    payment
                            )
                            + "$",
                            6
                    )
            );
        }


        if (!"All Statuses".equals(
                status
        )) {

            filters.add(
                    RowFilter.regexFilter(
                            "^"
                            + java.util.regex.Pattern.quote(
                                    status
                            )
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


    private void updateViewButton() {

        if (viewSaleButton == null) {
            return;
        }

        viewSaleButton.setEnabled(
                salesTable.getSelectedRow()
                >= 0
        );
    }
    
    private void viewSelectedSale() {

        // Get the selected row from the visible table
        int viewRow =
                salesTable.getSelectedRow();

        if (viewRow < 0) {
            return;
        }


        // Convert visible row number to the actual model row
        // because the table can be sorted and filtered.
        int modelRow =
                salesTable.convertRowIndexToModel(
                        viewRow
                );


        // ------------------------------------------
        // Get Sale ID
        // ------------------------------------------

        int saleId =
                ((Number)
                tableModel.getValueAt(
                        modelRow,
                        0
                )).intValue();


        // ------------------------------------------
        // Load the financial information
        // directly from the sales table
        // ------------------------------------------

        Object[] summary;

        try {

            summary =
                    saleDAO.getSaleSummaryById(
                            saleId
                    );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load sale totals."
                    + "\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        if (summary == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "The selected sale could not be found.",
                    "Sale Not Found",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // These values come directly from the sales table.
        BigDecimal subtotal =
                (BigDecimal)
                summary[0];

        BigDecimal discount =
                (BigDecimal)
                summary[1];

        BigDecimal total =
                (BigDecimal)
                summary[2];


        // ------------------------------------------
        // Get the information already displayed
        // in the Sales History table
        // ------------------------------------------

        String invoice =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                1
                        )
                );


        String date =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                2
                        )
                );


        String customer =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                3
                        )
                );


        String cashier =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                4
                        )
                );


        String payment =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                6
                        )
                );


        String status =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                7
                        )
                );


        // ------------------------------------------
        // Open Sale Details
        // ------------------------------------------

        SaleDetailsDialog dialog =
                new SaleDetailsDialog(
                        this,
                        saleId,
                        invoice,
                        date,
                        customer,
                        cashier,
                        subtotal,
                        discount,
                        total,
                        payment,
                        status
                );


        dialog.setVisible(
                true
        );
    }
    
}