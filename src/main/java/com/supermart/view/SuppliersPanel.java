/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */

import com.supermart.dao.SupplierDAO;
import com.supermart.model.Supplier;
import com.supermart.view.component.PlaceholderTextField;
import com.supermart.view.component.SuperMartTableStyle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.SwingUtilities;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;


public class SuppliersPanel extends JPanel {

    private static final Color BACKGROUND =
            new Color(247, 248, 250);

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color BORDER =
            new Color(230, 232, 235);

    private static final Color PRIMARY_BUTTON =
            new Color(25, 25, 28);


    private final SupplierDAO supplierDAO;


    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> tableSorter;

    private PlaceholderTextField searchField;

    private JComboBox<String> statusFilter;
    private JComboBox<String> sortByCombo;
    private JComboBox<String> sortOrderCombo;

    private JButton editButton;
    private JButton statusButton;


    public SuppliersPanel() {

        supplierDAO =
                new SupplierDAO();

        setLayout(
                new BorderLayout()
        );

        setBackground(
                BACKGROUND
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        35,
                        45,
                        35,
                        45
                )
        );


        add(
                createHeader(),
                BorderLayout.NORTH
        );

        add(
                createContent(),
                BorderLayout.CENTER
        );


        loadSuppliers();
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
                        25,
                        0
                )
        );


        // ---------- Title ----------
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
                new JLabel(
                        "Suppliers"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                TEXT_PRIMARY
        );


        JLabel subtitle =
                new JLabel(
                        "Manage supplier information and product sources."
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


        titlePanel.add(title);
        titlePanel.add(
                Box.createVerticalStrut(6)
        );
        titlePanel.add(subtitle);


        // ---------- Actions ----------
        JPanel actionPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        actionPanel.setOpaque(false);


        editButton =
                createSecondaryButton(
                        "Edit"
                );

        editButton.setEnabled(false);


        statusButton =
                createSecondaryButton(
                        "Deactivate"
                );

        statusButton.setEnabled(false);


        JButton addButton =
                createPrimaryButton(
                        "+ Add Supplier"
                );


        editButton.addActionListener(
                e -> editSelectedSupplier()
        );

        statusButton.addActionListener(
                e -> changeSelectedSupplierStatus()
        );

        addButton.addActionListener(
                e -> addSupplier()
        );


        actionPanel.add(editButton);
        actionPanel.add(statusButton);
        actionPanel.add(addButton);


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
    // CONTENT
    // ==========================================

    private JPanel createContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(
                Color.WHITE
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
                createTableSection(),
                BorderLayout.CENTER
        );


        return content;
    }


    // ==========================================
    // FILTER PANEL
    // ==========================================

    private JPanel createFilterPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                12
                        )
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        12,
                        12,
                        12
                )
        );


        searchField =
                new PlaceholderTextField(
                        "Search supplier, phone or email...",
                        22
                );

        searchField.setToolTipText(
                "Search suppliers by name, phone or email"
        );


        statusFilter =
                new JComboBox<>(
                        new String[]{
                            "Status: All",
                            "ACTIVE",
                            "INACTIVE"
                        }
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


        sortOrderCombo =
                new JComboBox<>(
                        new String[]{
                            "Ascending",
                            "Descending"
                        }
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


    // ==========================================
    // TABLE
    // ==========================================

    private JScrollPane createTableSection() {

        String[] columns = {
            "ID",
            "Supplier",
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


        supplierTable =
                new JTable(
                        tableModel
                );


        tableSorter =
                new TableRowSorter<>(
                        tableModel
                );

        supplierTable.setRowSorter(
                tableSorter
        );


        SuperMartTableStyle.apply(
                supplierTable
        );


        supplierTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        supplierTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> {

                    if (!e.getValueIsAdjusting()) {
                        updateActionButtons();
                    }
                });


        supplierTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60); //ID

        supplierTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180); //Supplier

        supplierTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(120); //Phone

        supplierTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(200); //Email

        supplierTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(180); //Address

        supplierTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(100); //Status
        
        supplierTable
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(120);     // Joined

        JScrollPane scrollPane =
                new JScrollPane(
                        supplierTable
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        12,
                        12,
                        12
                )
        );
        
        scrollPane.setBackground(
                Color.WHITE
        );

        scrollPane.getViewport()
                .setBackground(
                        Color.WHITE
                );
        
        scrollPane.getVerticalScrollBar().setUnitIncrement(
                16
        );

        return scrollPane;
    }


    // ==========================================
    // LOAD SUPPLIERS
    // ==========================================

    public void loadSuppliers() {

        tableModel.setRowCount(0);


        try {

            List<Supplier> suppliers =
                    supplierDAO
                            .getAllSuppliers();


            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd MMM yyyy"
                    );


            for (Supplier supplier
                    : suppliers) {

                String joined = "—";

                if (supplier.getCreatedAt()
                        != null) {

                    joined =
                            supplier
                                    .getCreatedAt()
                                    .format(
                                            formatter
                                    );
                }

                tableModel.addRow(
                        new Object[]{
                            supplier.getSupplierId(),
                            supplier.getName(),
                            displayValue(supplier.getPhone()),
                            displayValue(supplier.getEmail()),
                            displayValue(supplier.getAddress()),
                            supplier.getStatus(),
                            joined
                        }
                );
                
                
            }


            supplierTable.clearSelection();
            updateActionButtons();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load suppliers.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ==========================================
    // FILTERS
    // ==========================================

    private void applyFilters() {

        if (tableSorter == null) {
            return;
        }


        String searchText =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String selectedStatus =
                statusFilter
                        .getSelectedItem()
                        .toString();


        RowFilter<DefaultTableModel, Integer>
                filter =
                new RowFilter<>() {

            @Override
            public boolean include(
                    Entry<? extends DefaultTableModel,
                            ? extends Integer> entry
            ) {

                String name =
                        entry.getStringValue(1)
                                .toLowerCase();

                String phone =
                        entry.getStringValue(2)
                                .toLowerCase();

                String email =
                        entry.getStringValue(3)
                                .toLowerCase();

                String address =
                        entry.getStringValue(4)
                                .toLowerCase();
                
                String status =
                        entry.getStringValue(5);


                boolean matchesSearch =
                        searchText.isEmpty()
                        || name.contains(searchText)
                        || phone.contains(searchText)
                        || email.contains(searchText)
                        || address.contains(searchText);
                


                boolean matchesStatus =
                        "Status: All"
                                .equals(
                                        selectedStatus
                                )
                        || status.equals(
                                selectedStatus
                        );


                return matchesSearch
                        && matchesStatus;
            }
        };


        tableSorter.setRowFilter(
                filter
        );
    }


    // ==========================================
    // SORTING
    // ==========================================

    private void applySorting() {

        if (tableSorter == null) {
            return;
        }


        int column;

        switch (
                sortByCombo
                        .getSelectedIndex()
        ) {

            case 1:
                column = 1; // Name
                break;

            case 2:
                column = 2; // Phone
                break;           
           
            case 3:
                column = 5; // Status
                break;

            case 4:
                column = 6; // Joined
                break;

            default:
                column = 0; // ID
                break;
        }


        javax.swing.SortOrder order =
                sortOrderCombo
                        .getSelectedIndex()
                        == 0
                        ? javax.swing.SortOrder.ASCENDING
                        : javax.swing.SortOrder.DESCENDING;


        tableSorter.setSortKeys(
                List.of(
                        new javax.swing.RowSorter.SortKey(
                                column,
                                order
                        )
                )
        );
    }


    // ==========================================
    // ACTION BUTTON STATE
    // ==========================================

    private void updateActionButtons() {

        int selectedViewRow =
                supplierTable
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
                supplierTable
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


        statusButton.setText(
                "ACTIVE".equals(status)
                        ? "Deactivate"
                        : "Activate"
        );
    }


    // ==========================================
    // ADD
    // ==========================================


    private void addSupplier() {

        AddSupplierDialog dialog =
                new AddSupplierDialog(
                        SwingUtilities.getWindowAncestor(
                                this
                        )
                );

        dialog.setVisible(
                true
        );


        if (dialog.isSupplierSaved()) {

            loadSuppliers();
        }
    }
  
    // ==========================================
    // GET SELECTED SUPPLIER
    // ==========================================

    private Supplier getSelectedSupplier() {

        int selectedViewRow =
                supplierTable
                        .getSelectedRow();


        if (selectedViewRow < 0) {

            return null;
        }


        int modelRow =
                supplierTable
                        .convertRowIndexToModel(
                                selectedViewRow
                        );


        int supplierId =
                (Integer)
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                );


        try {

            List<Supplier> suppliers =
                    supplierDAO
                            .getAllSuppliers();


            for (Supplier supplier
                    : suppliers) {

                if (supplier.getSupplierId()
                        == supplierId) {

                    return supplier;
                }
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load supplier.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }


        return null;
    }


    // ==========================================
    // EDIT
    // ==========================================
   
    
    private void editSelectedSupplier() {

        Supplier supplier =
                getSelectedSupplier();


        if (supplier == null) {

            return;
        }


        AddSupplierDialog dialog =
                new AddSupplierDialog(
                        SwingUtilities
                                .getWindowAncestor(
                                        this
                                ),
                        supplier
                );


        dialog.setVisible(
                true
        );


        if (dialog.isSupplierSaved()) {

            loadSuppliers();
        }
    }

    // ==========================================
    // ACTIVATE / DEACTIVATE
    // ==========================================

    private void changeSelectedSupplierStatus() {

        int selectedViewRow =
                supplierTable
                        .getSelectedRow();


        if (selectedViewRow < 0) {
            return;
        }


        int modelRow =
                supplierTable
                        .convertRowIndexToModel(
                                selectedViewRow
                        );


        int supplierId =
                (Integer)
                        tableModel
                                .getValueAt(
                                        modelRow,
                                        0
                                );


        String supplierName =
                tableModel
                        .getValueAt(
                                modelRow,
                                1
                        )
                        .toString();


        String currentStatus =
                tableModel
                        .getValueAt(
                                modelRow,
                                5
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
                                + supplierName
                                + "\"?",
                        "Confirm Supplier Status",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (choice
                != JOptionPane.YES_OPTION) {

            return;
        }


        try {

            boolean updated =
                    supplierDAO
                            .updateSupplierStatus(
                                    supplierId,
                                    newStatus
                            );


            if (updated) {

                loadSuppliers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Supplier status could not be updated.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update supplier status.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ==========================================
    // REFRESH WHEN RETURNING TO TAB
    // ==========================================

    public void refreshPanel() {

        loadSuppliers();

        supplierTable.clearSelection();

        updateActionButtons();
    }


    // ==========================================
    // HELPERS
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
                PRIMARY_BUTTON
        );

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        130,
                        38
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

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(
                        105,
                        38
                )
        );


        return button;
    }
}