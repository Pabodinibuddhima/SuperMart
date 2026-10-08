/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */
import com.supermart.dao.EmployeeDAO;
import com.supermart.dao.ReturnDAO;
import com.supermart.exception.InvalidReturnException;
import com.supermart.model.Employee;
import com.supermart.model.Return;
import com.supermart.model.ReturnItem;
import com.supermart.service.ReturnService;
import com.supermart.view.component.PlaceholderTextField;
import com.supermart.view.component.SuperMartTableStyle;
import com.supermart.util.AppearanceManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.SQLException;

import java.util.ArrayList;
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
import javax.swing.SwingUtilities;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.event.TableModelEvent;

import javax.swing.table.DefaultTableModel;


public class ReturnRefundDialog extends JDialog {

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


    private final int saleId;
    private final String invoiceNumber;

    private final ReturnDAO returnDAO;
    private final ReturnService returnService;
    private final EmployeeDAO employeeDAO;


    private JTable itemsTable;
    private DefaultTableModel tableModel;

    private JComboBox<EmployeeOption> employeeComboBox;

    private PlaceholderTextField reasonField;

    private JLabel refundValueLabel;

    private JButton processReturnButton;

    private boolean returnCompleted;

    private boolean updatingTable;


    public ReturnRefundDialog(
            Window parent,
            int saleId,
            String invoiceNumber
    ) {

        super(
                parent instanceof Frame
                        ? (Frame) parent
                        : null,
                "Return / Refund",
                ModalityType.APPLICATION_MODAL
        );


        this.saleId =
                saleId;

        this.invoiceNumber =
                invoiceNumber;

        this.returnDAO =
                new ReturnDAO();

        this.returnService =
                new ReturnService();

        this.employeeDAO =
                new EmployeeDAO();


        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setSize(
                920,
                650
        );

        setMinimumSize(
                new Dimension(
                        800,
                        540
                )
        );


        createInterface();

        loadEmployees();

        loadReturnableItems();

        setLocationRelativeTo(
                parent
        );
    }


    // =====================================================
    // INTERFACE
    // =====================================================

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
                createContentCard(),
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
                        new BorderLayout()
                );

        panel.setOpaque(
                false
        );


        JPanel titlePanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                4
                        )
                );

        titlePanel.setOpaque(
                false
        );


        JLabel title =
                new JLabel(
                        "Return / Refund"
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
                        "Invoice "
                        + invoiceNumber
                        + "  •  Sale #"
                        + saleId
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


    private JPanel createContentCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                16
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
                createInfoPanel(),
                BorderLayout.NORTH
        );


        String[] columns = {
            "Sale Item ID",
            "Product",
            "Sold",
            "Returned",
            "Available",
            "Unit Price",
            "Return Qty"
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

                        return column == 6;
                    }

                    @Override
                    public Class<?> getColumnClass(
                            int columnIndex
                    ) {

                        if (columnIndex == 0
                                || columnIndex == 2
                                || columnIndex == 3
                                || columnIndex == 4
                                || columnIndex == 6) {

                            return Integer.class;
                        }

                        if (columnIndex == 5) {

                            return BigDecimal.class;
                        }

                        return String.class;
                    }
                };


        itemsTable =
                new JTable(
                        tableModel
                );


        SuperMartTableStyle.apply(
                itemsTable
        );
        
        
        
        itemsTable.setEnabled(
                true
        );

        itemsTable.setFocusable(
                true
        );

        itemsTable.setRowSelectionAllowed(
                true
        );

        itemsTable.setCellSelectionEnabled(
                true
        );

        itemsTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        /*
         * Sale Item ID is an internal database value.
         * We need it for processing but the cashier
         * does not need to see it.
         */
        itemsTable.removeColumn(
                itemsTable.getColumnModel()
                        .getColumn(0)
        );
        
        // ------------------------------------------
        // RETURN QUANTITY INPUT
        // ------------------------------------------

        JTextField quantityEditorField =
                new JTextField();
        
        quantityEditorField.setHorizontalAlignment(
                JTextField.CENTER
        );
        
        quantityEditorField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppearanceManager.isDarkMode()
                                        ? AppearanceManager.DARK_BORDER
                                        : new Color(190, 194, 200)
                        ),
                        BorderFactory.createEmptyBorder(
                                3,
                                6,
                                3,
                                6
                        )
                )
        );

        quantityEditorField.setBackground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_INPUT
                        : Color.WHITE
        );

        quantityEditorField.setForeground(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );

        quantityEditorField.setCaretColor(
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );


        quantityEditorField.setToolTipText(
                "Enter the quantity to return"
        );


        DefaultCellEditor quantityEditor =
                new DefaultCellEditor(
                        quantityEditorField
                );

        quantityEditor.setClickCountToStart(
                1
        );


        /*
         * Sale Item ID is hidden, so Return Qty
         * is visible column index 5.
         */
        itemsTable.getColumnModel()
                .getColumn(5)
                .setCellEditor(
                        quantityEditor
                );


        // ------------------------------------------
        // RETURN QUANTITY DISPLAY
        // ------------------------------------------

        DefaultTableCellRenderer quantityRenderer =
                new DefaultTableCellRenderer() {

                    @Override
                    public java.awt.Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        JLabel label =
                                (JLabel)
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );


                        label.setHorizontalAlignment(
                                JLabel.CENTER
                        );

                        label.setOpaque(
                                true
                        );


                        /*
                         * Slightly different background tells
                         * the user this is an input field.
                         */

                        if (AppearanceManager.isDarkMode()) {

                            label.setBackground(
                                    isSelected
                                            ? AppearanceManager.DARK_SELECTED
                                            : AppearanceManager.DARK_INPUT
                            );

                            label.setForeground(
                                    AppearanceManager.DARK_TEXT
                            );

                        } else {

                            label.setBackground(
                                    new Color(250, 250, 250)
                            );

                            label.setForeground(
                                    TEXT_PRIMARY
                            );
                        }

                        
                        label.setBorder(
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createEmptyBorder(
                                                6,
                                                12,
                                                6,
                                                12
                                        ),
                                        BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(
                                                        AppearanceManager.isDarkMode()
                                                                ? AppearanceManager.DARK_BORDER
                                                                : new Color(190, 194, 200)
                                                ),
                                                BorderFactory.createEmptyBorder(
                                                        2,
                                                        8,
                                                        2,
                                                        8
                                                )
                                        )
                                )
                        );

                        return label;
                    }
                };


        itemsTable.getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        quantityRenderer
                );


        itemsTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(
                        110
                );

        itemsTable.getColumnModel()
                .getColumn(5)
                .setMinWidth(
                        100
                );


        tableModel.addTableModelListener(
                e -> {

                    if (updatingTable) {
                        return;
                    }

                    if (e.getType()
                            == TableModelEvent.UPDATE) {

                        validateEditedQuantities();

                        updateRefundPreview();
                    }
                }
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        itemsTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        
        scrollPane.getViewport()
                .setBackground(
                        AppearanceManager.isDarkMode()
                                ? AppearanceManager.DARK_SURFACE
                                : Color.WHITE
                );

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );


        return card;
    }


    private JPanel createInfoPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                16,
                                0
                        )
                );

        panel.setOpaque(
                false
        );


        // ------------------------------------------
        // EMPLOYEE
        // ------------------------------------------

        JPanel employeePanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                6
                        )
                );

        employeePanel.setOpaque(
                false
        );


        JLabel employeeLabel =
                new JLabel(
                        "Processed By"
                );

        employeeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        employeeComboBox =
                new JComboBox<>();

        employeeComboBox.setPreferredSize(
                new Dimension(
                        240,
                        34
                )
        );

        employeeComboBox.setToolTipText(
                "Employee processing this return or refund"
        );


        employeePanel.add(
                employeeLabel,
                BorderLayout.NORTH
        );

        employeePanel.add(
                employeeComboBox,
                BorderLayout.CENTER
        );


        // ------------------------------------------
        // REASON
        // ------------------------------------------

        JPanel reasonPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                6
                        )
                );

        reasonPanel.setOpaque(
                false
        );


        JLabel reasonLabel =
                new JLabel(
                        "Reason"
                );

        reasonLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );


        reasonField =
                new PlaceholderTextField(
                        "e.g. Damaged item or customer return",
                        24
                );

        reasonField.setPreferredSize(
                new Dimension(
                        280,
                        34
                )
        );

        reasonField.setToolTipText(
                "Optional reason for the return"
        );


        reasonPanel.add(
                reasonLabel,
                BorderLayout.NORTH
        );

        reasonPanel.add(
                reasonField,
                BorderLayout.CENTER
        );


        panel.add(
                employeePanel
        );

        panel.add(
                reasonPanel
        );


        return panel;
    }


    private JPanel createBottomPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(
                false
        );


        // ------------------------------------------
        // REFUND TOTAL
        // ------------------------------------------

        JPanel refundPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                6,
                                0
                        )
                );

        refundPanel.setOpaque(
                false
        );


        JLabel refundLabel =
                new JLabel(
                        "Estimated Refund:"
                );

        refundLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );


        refundValueLabel =
                new JLabel(
                        "Rs. 0.00"
                );

        refundValueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        refundValueLabel.setForeground(
                TEXT_PRIMARY
        );


        refundPanel.add(
                refundLabel
        );

        refundPanel.add(
                refundValueLabel
        );


        // ------------------------------------------
        // PROCESS BUTTON
        // ------------------------------------------

        processReturnButton =
                new JButton(
                        "Process Return"
                );

        processReturnButton.setFocusPainted(
                false
        );

        processReturnButton.setForeground(
                Color.WHITE
        );

        processReturnButton.setBackground(
                PRIMARY
        );

        processReturnButton.setBorder(
                BorderFactory.createEmptyBorder(
                        11,
                        18,
                        11,
                        18
                )
        );

        processReturnButton.setEnabled(
                false
        );

        processReturnButton.addActionListener(
                e -> processReturn()
        );


        panel.add(
                refundPanel,
                BorderLayout.WEST
        );

        panel.add(
                processReturnButton,
                BorderLayout.EAST
        );


        return panel;
    }


    // =====================================================
    // LOAD DATA
    // =====================================================

    private void loadEmployees() {

        try {

            List<Employee> employees =
                    employeeDAO.getAllEmployees();


            employeeComboBox.removeAllItems();


            for (Employee employee : employees) {

                if (!"ACTIVE".equals(
                        employee.getStatus()
                )) {
                    continue;
                }


                String role =
                        employee.getRole();


                if (!"CASHIER".equals(role)
                        && !"MANAGER".equals(role)) {
                    continue;
                }


                employeeComboBox.addItem(
                        new EmployeeOption(
                                employee.getEmployeeId(),
                                employee.getName(),
                                role
                        )
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load employees."
                    + "\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void loadReturnableItems() {

        try {

            List<Object[]> items =
                    returnDAO.getReturnableItems(
                            saleId
                    );


            tableModel.setRowCount(
                    0
            );


            for (Object[] item : items) {

                int available =
                        (Integer)
                        item[5];


                tableModel.addRow(
                        new Object[]{
                            item[0],
                            item[2],
                            item[3],
                            item[4],
                            available,
                            item[6],
                            0
                        }
                );
            }


            updateRefundPreview();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load returnable items."
                    + "\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            processReturnButton.setEnabled(
                    false
            );
        }
    }


    // =====================================================
    // QUANTITY VALIDATION
    // =====================================================

    private void validateEditedQuantities() {

        updatingTable =
                true;


        try {

            for (int row = 0;
                    row < tableModel.getRowCount();
                    row++) {

                int available =
                        getIntegerValue(
                                tableModel.getValueAt(
                                        row,
                                        4
                                )
                        );


                int requested =
                        getIntegerValue(
                                tableModel.getValueAt(
                                        row,
                                        6
                                )
                        );


                if (requested < 0) {

                    requested =
                            0;
                }


                if (requested > available) {

                    requested =
                            available;
                }


                tableModel.setValueAt(
                        requested,
                        row,
                        6
                );
            }


        } finally {

            updatingTable =
                    false;
        }
    }


    // =====================================================
    // REFUND PREVIEW
    // =====================================================

    private void updateRefundPreview() {

        BigDecimal estimatedRefund =
                calculateEstimatedRefund();


        refundValueLabel.setText(
                "Rs. "
                + estimatedRefund
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
                        .toPlainString()
        );


        boolean hasReturnQuantity =
                hasSelectedReturnQuantity();


        boolean hasEmployee =
                employeeComboBox != null
                && employeeComboBox.getSelectedItem()
                        != null;


        processReturnButton.setEnabled(
                hasReturnQuantity
                && hasEmployee
        );
    }


    private BigDecimal calculateEstimatedRefund() {

        /*
         * This is only a UI preview.
         *
         * ReturnService calculates the authoritative
         * refund again from the database before commit.
         *
         * Because the final calculation belongs in the
         * business layer, this preview does not control
         * how much is actually refunded.
         */

        BigDecimal total =
                BigDecimal.ZERO;


        try {

            Object[] summary =
                    new com.supermart.dao.SaleDAO()
                            .getSaleSummaryById(
                                    saleId
                            );


            if (summary == null) {

                return BigDecimal.ZERO;
            }


            BigDecimal subtotal =
                    (BigDecimal)
                    summary[0];

            BigDecimal discount =
                    (BigDecimal)
                    summary[1];


            BigDecimal discountRatio =
                    BigDecimal.ZERO;


            if (subtotal != null
                    && subtotal.compareTo(
                            BigDecimal.ZERO
                    ) > 0) {

                discountRatio =
                        discount.divide(
                                subtotal,
                                10,
                                RoundingMode.HALF_UP
                        );
            }


            for (int row = 0;
                    row < tableModel.getRowCount();
                    row++) {

                int quantity =
                        getIntegerValue(
                                tableModel.getValueAt(
                                        row,
                                        6
                                )
                        );


                if (quantity <= 0) {
                    continue;
                }


                BigDecimal unitPrice =
                        (BigDecimal)
                        tableModel.getValueAt(
                                row,
                                5
                        );


                BigDecimal gross =
                        unitPrice.multiply(
                                BigDecimal.valueOf(
                                        quantity
                                )
                        );


                BigDecimal itemDiscount =
                        gross.multiply(
                                discountRatio
                        );


                BigDecimal refund =
                        gross.subtract(
                                itemDiscount
                        );


                total =
                        total.add(
                                refund
                        );
            }


        } catch (SQLException e) {

            return BigDecimal.ZERO;
        }


        return total.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }


    private boolean hasSelectedReturnQuantity() {

        for (int row = 0;
                row < tableModel.getRowCount();
                row++) {

            int quantity =
                    getIntegerValue(
                            tableModel.getValueAt(
                                    row,
                                    6
                            )
                    );


            if (quantity > 0) {

                return true;
            }
        }


        return false;
    }


    // =====================================================
    // PROCESS RETURN
    // =====================================================

    private void processReturn() {

        if (itemsTable.isEditing()) {

            itemsTable.getCellEditor()
                    .stopCellEditing();
        }


        validateEditedQuantities();


        EmployeeOption employee =
                (EmployeeOption)
                employeeComboBox.getSelectedItem();


        if (employee == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select the employee processing the return.",
                    "Employee Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        List<ReturnItem> returnItems =
                new ArrayList<>();


        for (int row = 0;
                row < tableModel.getRowCount();
                row++) {

            int quantity =
                    getIntegerValue(
                            tableModel.getValueAt(
                                    row,
                                    6
                            )
                    );


            if (quantity <= 0) {
                continue;
            }


            int saleItemId =
                    getIntegerValue(
                            tableModel.getValueAt(
                                    row,
                                    0
                            )
                    );


            ReturnItem item =
                    new ReturnItem();

            item.setSaleItemId(
                    saleItemId
            );

            item.setQuantity(
                    quantity
            );


            returnItems.add(
                    item
            );
        }


        if (returnItems.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a return quantity for at least one item.",
                    "No Items Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        BigDecimal preview =
                calculateEstimatedRefund();


        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Process this return?"
                        + "\n\n"
                        + "Invoice: "
                        + invoiceNumber
                        + "\n"
                        + "Estimated refund: Rs. "
                        + preview.toPlainString()
                        + "\n"
                        + "Processed by: "
                        + employee.name,
                        "Confirm Return",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );


        if (confirmation
                != JOptionPane.YES_OPTION) {

            return;
        }


        Return returnTransaction =
                new Return();

        returnTransaction.setSaleId(
                saleId
        );

        returnTransaction.setEmployeeId(
                employee.employeeId
        );


        String reason =
                reasonField.getText()
                        .trim();


        returnTransaction.setReason(
                reason.isEmpty()
                        ? null
                        : reason
        );


        try {

            int returnId =
                    returnService.processReturn(
                            returnTransaction,
                            returnItems
                    );


            returnCompleted =
                    true;


            JOptionPane.showMessageDialog(
                    this,
                    "Return completed successfully."
                    + "\n\n"
                    + "Return ID: "
                    + returnId
                    + "\n"
                    + "Refund: Rs. "
                    + returnTransaction
                            .getRefundAmount()
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            )
                            .toPlainString(),
                    "Return Completed",
                    JOptionPane.INFORMATION_MESSAGE
            );


            dispose();


        } catch (InvalidReturnException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Return",
                    JOptionPane.WARNING_MESSAGE
            );


            loadReturnableItems();


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to process the return."
                    + "\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =====================================================
    // HELPERS
    // =====================================================

    private int getIntegerValue(
            Object value
    ) {

        if (value instanceof Number number) {

            return number.intValue();
        }


        try {

            return Integer.parseInt(
                    String.valueOf(
                            value
                    ).trim()
            );


        } catch (NumberFormatException e) {

            return 0;
        }
    }


    public boolean isReturnCompleted() {

        return returnCompleted;
    }


    private static class EmployeeOption {

        private final int employeeId;
        private final String name;
        private final String role;


        private EmployeeOption(
                int employeeId,
                String name,
                String role
        ) {

            this.employeeId =
                    employeeId;

            this.name =
                    name;

            this.role =
                    role;
        }


        @Override
        public String toString() {

            return name
                    + " — "
                    + role;
        }
    }
}