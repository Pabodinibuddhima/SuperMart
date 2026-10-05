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
import com.supermart.dao.CustomerDAO;
import com.supermart.model.Customer;
import com.supermart.view.component.PlaceholderTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class CustomersPanel extends JPanel {

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

    private static final Color PRIMARY_BUTTON =
            new Color(25, 25, 28);


    private final CustomerDAO customerDAO;

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;

    private JScrollPane customerScrollPane;
    private JLabel emptyStateLabel;
    
    private JButton editButton;
    private JButton statusButton;

    private PlaceholderTextField searchField;
    private JComboBox<String> statusFilter;
    
    private JComboBox<String> sortByCombo;
    private JComboBox<String> sortOrderCombo;


    public CustomersPanel() {

        customerDAO = new CustomerDAO();

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        38, 42, 38, 42
                )
        );


        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );


        content.add(createHeader());

        content.add(
                Box.createVerticalStrut(24)
        );
        
        content.add(createCustomerContent());
        add(
                content,
                BorderLayout.CENTER
        );


        loadCustomers();
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

        header.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        72
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
                new JLabel("Customers");

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
                        "Manage customer information and account status."
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
                Box.createVerticalStrut(6)
        );

        titlePanel.add(subtitle);


        JButton addButton =
                new JButton(
                        "+ Add Customer"
                );

        addButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        addButton.setForeground(
                Color.WHITE
        );

        addButton.setBackground(
                PRIMARY_BUTTON
        );

        addButton.setFocusPainted(
                false
        );

        addButton.setBorder(
                BorderFactory.createEmptyBorder(
                        11, 18, 11, 18
                )
        );

        
        addButton.addActionListener(e -> {

            AddCustomerDialog dialog =
                    new AddCustomerDialog(
                            javax.swing.SwingUtilities
                                    .getWindowAncestor(this)
                    );

            dialog.setVisible(true);


            if (dialog.isCustomerSaved()) {

                loadCustomers();
            }
        });

        // ---------- Customer actions ----------

        editButton =
                new JButton(
                        "Edit"
                );

        editButton.setFocusPainted(
                false
        );

        editButton.setEnabled(
                false
        );

        editButton.setToolTipText(
                "Edit the selected customer"
        );


        statusButton =
                new JButton(
                        "Deactivate"
                );

        statusButton.setFocusPainted(
                false
        );

        statusButton.setEnabled(
                false
        );

        statusButton.setToolTipText(
                "Activate or deactivate the selected customer"
        );


        editButton.addActionListener(
                e -> editSelectedCustomer()
        );

        statusButton.addActionListener(
                e -> changeSelectedCustomerStatus()
        );


        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        actionPanel.setOpaque(
                false
        );

        actionPanel.add(
                editButton
        );

        actionPanel.add(
                statusButton
        );

        actionPanel.add(
                addButton
        );

        
        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                actionPanel,
                BorderLayout.EAST
        );


        return header;
    }


    // ==========================================
    // FILTERS
    // ==========================================

    private JPanel createFilterPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);
        
        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12
                )
        );

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        56
                )
        );


        searchField =
                new PlaceholderTextField(
                        "Search name, phone or email...",
                        25
                );

        searchField.setToolTipText(
                "Search customers by name, phone number or email address"
        );

        statusFilter =
                new JComboBox<>(
                        new String[]{
                            "Status: All",
                            "ACTIVE",
                            "INACTIVE"
                        }
                );

        statusFilter.setPreferredSize(
                new Dimension(
                        140,
                        32
                )
        );
        
       sortByCombo =
                new JComboBox<>(
                        new String[]{
                            "Sort: ID",
                            "Sort: Name",
                            "Sort: Phone",
                            "Sort: Status",
                            "Sort: Joined"
                        }
                );

        sortByCombo.setPreferredSize(
                new Dimension(
                        130,
                        32
                )
        );

        sortByCombo.setToolTipText(
                "Choose which customer field to sort by"
        );


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


        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                    @Override
                    public void insertUpdate(
                            DocumentEvent e
                    ) {
                        applyFilters();
                    }

                    @Override
                    public void removeUpdate(
                            DocumentEvent e
                    ) {
                        applyFilters();
                    }

                    @Override
                    public void changedUpdate(
                            DocumentEvent e
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

    private JPanel createCustomerContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(
                CARD_BACKGROUND
        );

        content.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        content.add(
                createFilterPanel(),
                BorderLayout.NORTH
        );

        content.add(
                createTablePanel(),
                BorderLayout.CENTER
        );

        return content;
    }
    

    // ==========================================
    // TABLE
    // ==========================================

    private JPanel createTablePanel() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                CARD_BACKGROUND
        );
        
        card.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 8, 8, 8
                )
        );


        String[] columns = {
            "ID",
            "Name",
            "Phone",
            "Email",
            "Address",
            "Status",
            "Joined"
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

                if (columnIndex == 0) {
                    return Integer.class;
                }

                return String.class;
            }

        };
         
        customerTable =
                new JTable(
                        tableModel
                );
        
        SuperMartTableStyle.apply(
                customerTable
        );


        customerTable.setSelectionMode(
                ListSelectionModel
                        .SINGLE_SELECTION
        );
        
        
        
        customerTable
                .getSelectionModel()
                .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                updateActionButtons();
            }
        });
 
        customerTable.setShowVerticalLines(
                false
        );

        // ------------------------------------------
        // Sorting and filtering
        // ------------------------------------------

        tableSorter =
                new TableRowSorter<>(
                        tableModel
                );

        customerTable.setRowSorter(
                tableSorter
        );

        applySorting();

        // ------------------------------------------
        // Scroll pane
        // ------------------------------------------

        customerScrollPane =
                new JScrollPane(
                        customerTable
                );

        customerScrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );


        // ------------------------------------------
        // Empty-state message
        // ------------------------------------------

        emptyStateLabel =
                new JLabel(
                        "No customer records yet.",
                        JLabel.CENTER
                );

        emptyStateLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        emptyStateLabel.setForeground(
                TEXT_SECONDARY
        );

        emptyStateLabel.setOpaque(
                true
        );

        emptyStateLabel.setBackground(
                CARD_BACKGROUND
        );


        card.add(
                customerScrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    
    // ==========================================
    // UPDATE ACTION BUTTONS
    // ==========================================

    private void updateActionButtons() {

        int selectedViewRow =
                customerTable
                        .getSelectedRow();


        boolean selected =
                selectedViewRow >= 0;


        editButton.setEnabled(
                selected
        );

        statusButton.setEnabled(
                selected
        );


        if (!selected) {

            statusButton.setText(
                    "Deactivate"
            );

            return;
        }


        int modelRow =
                customerTable
                        .convertRowIndexToModel(
                                selectedViewRow
                        );


        String status =
                tableModel
                        .getValueAt(
                                modelRow,
                                5
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
    // GET SELECTED CUSTOMER
    // ==========================================

    private Customer getSelectedCustomer() {

        int selectedViewRow =
                customerTable
                        .getSelectedRow();


        if (selectedViewRow < 0) {
            return null;
        }


        int modelRow =
                customerTable
                        .convertRowIndexToModel(
                                selectedViewRow
                        );


        int customerId =
                (Integer)
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                );


        try {

            List<Customer> customers =
                    customerDAO
                            .getAllCustomers();


            for (Customer customer
                    : customers) {

                if (customer.getCustomerId()
                        == customerId) {

                    return customer;
                }
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load customer.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }


        return null;
    }

    // ==========================================
    // EDIT CUSTOMER
    // ==========================================

    private void editSelectedCustomer() {

        Customer customer =
                getSelectedCustomer();


        if (customer == null) {
            return;
        }


        AddCustomerDialog dialog =
                new AddCustomerDialog(
                        javax.swing.SwingUtilities
                                .getWindowAncestor(this),
                        customer
                );


        dialog.setVisible(true);


        if (dialog.isCustomerSaved()) {

            loadCustomers();
        }
    }
    
    
    // ==========================================
    // CHANGE CUSTOMER STATUS
    // ==========================================

    private void changeSelectedCustomerStatus() {

        Customer customer =
                getSelectedCustomer();


        if (customer == null) {
            return;
        }


        boolean currentlyActive =
                "ACTIVE".equals(
                        customer.getStatus()
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
                                + " "
                                + customer.getName()
                                + "?",
                        "Confirm Customer Status",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (choice
                != JOptionPane.YES_OPTION) {

            return;
        }


        try {

            boolean updated =
                    customerDAO
                            .updateCustomerStatus(
                                    customer.getCustomerId(),
                                    newStatus
                            );


            if (updated) {

                loadCustomers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Customer status could not be updated.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update customer status.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    
    
    
    public void refreshPanel() {

        loadCustomers();

        customerTable.clearSelection();

        updateActionButtons();
    }

    
    
    

    // ==========================================
    // LOAD CUSTOMERS
    // ==========================================

    public final void loadCustomers() {

        try {

            List<Customer> customers =
                    customerDAO
                            .getAllCustomers();


            tableModel.setRowCount(0);


            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd MMM yyyy"
                    );


            for (Customer customer
                    : customers) {

                String joined = "";

                if (customer.getCreatedAt()
                        != null) {

                    joined =
                            customer
                                    .getCreatedAt()
                                    .format(formatter);
                }


                tableModel.addRow(
                        new Object[]{
                            customer.getCustomerId(),
                            customer.getName(),

                            displayValue(
                                    customer.getPhone()
                            ),

                            displayValue(
                                    customer.getEmail()
                            ),

                            displayValue(
                                    customer.getAddress()
                            ),

                            customer.getStatus(),
                            joined
                        }
                );
            }


            applyFilters();
            updateEmptyState();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load customers.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ==========================================
    // FILTERING
    // ==========================================

    private void applyFilters() {

        if (tableSorter == null) {
            return;
        }


        String search =
                searchField == null
                        ? ""
                        : searchField
                                .getText()
                                .trim()
                                .toLowerCase();


        String selectedStatus =
                statusFilter == null
                        ? "Status: All"
                        : (String)
                                statusFilter
                                        .getSelectedItem();


        tableSorter.setRowFilter(
                new RowFilter<DefaultTableModel, Integer>() {

            @Override
            public boolean include(
                    Entry<? extends DefaultTableModel,
                            ? extends Integer> entry
            ) {

                String name =
                        entry
                                .getStringValue(1)
                                .toLowerCase();

                String phone =
                        entry
                                .getStringValue(2)
                                .toLowerCase();

                String email =
                        entry
                                .getStringValue(3)
                                .toLowerCase();


                boolean matchesSearch =
                        search.isEmpty()
                                || name.contains(search)
                                || phone.contains(search)
                                || email.contains(search);


                String rowStatus =
                        entry.getStringValue(5);


                boolean matchesStatus =
                        "Status: All"
                                .equals(selectedStatus)
                                || rowStatus.equals(
                                        selectedStatus
                                );


                return matchesSearch
                        && matchesStatus;
            }
        });
       
    }
 
    // ==========================================
    // SORTING
    // ==========================================

    private void applySorting() {

        if (tableSorter == null
                || sortByCombo == null
                || sortOrderCombo == null) {

            return;
        }


        String selectedField =
                (String) sortByCombo
                        .getSelectedItem();

        String selectedOrder =
                (String) sortOrderCombo
                        .getSelectedItem();


        int columnIndex;

        switch (selectedField) {

            case "Sort: Name":
                columnIndex = 1;
                break;

            case "Sort: Phone":
                columnIndex = 2;
                break;

            case "Sort: Status":
                columnIndex = 5;
                break;

            case "Sort: Joined":
                columnIndex = 6;
                break;

            case "Sort: ID":
            default:
                columnIndex = 0;
                break;
        }


        javax.swing.SortOrder order;

        if ("Descending".equals(
                selectedOrder
        )) {

            order =
                    javax.swing.SortOrder
                            .DESCENDING;

        } else {

            order =
                    javax.swing.SortOrder
                            .ASCENDING;
        }


        java.util.List<
                javax.swing.RowSorter.SortKey
                > sortKeys =
                java.util.List.of(
                        new javax.swing.RowSorter.SortKey(
                                columnIndex,
                                order
                        )
                );


        tableSorter.setSortKeys(
                sortKeys
        );

        tableSorter.sort();
    }

    // ==========================================
    // EMPTY STATE
    // ==========================================

    private void updateEmptyState() {

        if (tableModel.getRowCount() == 0) {

            customerScrollPane.setViewportView(
                    emptyStateLabel
            );

        } else {

            customerScrollPane.setViewportView(
                    customerTable
            );
        }
    }


    // ==========================================
    // DISPLAY NULL VALUES CLEANLY
    // ==========================================

    private String displayValue(
            String value
    ) {

        if (value == null
                || value.isBlank()) {

            return "—";
        }

        return value;
    }
}