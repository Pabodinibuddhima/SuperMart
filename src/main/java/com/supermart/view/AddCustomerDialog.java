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
import com.supermart.model.Customer;
import com.supermart.view.component.PlaceholderTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class AddCustomerDialog extends JDialog {

    private static final Color BACKGROUND =
            new Color(247, 248, 250);

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color BORDER =
            new Color(220, 223, 228);

    private static final Color PRIMARY =
            new Color(25, 25, 28);


    private final CustomerDAO customerDAO;

    private PlaceholderTextField nameField;
    private PlaceholderTextField phoneField;
    private PlaceholderTextField emailField;

    private JTextArea addressArea;

    private boolean customerSaved;
    private Customer customerToEdit;

    public AddCustomerDialog(
            Window owner
    ) {

        this(owner, null);
    }


    public AddCustomerDialog(
            Window owner,
            Customer customerToEdit
    ) {

        super(
                owner,
                customerToEdit == null
                        ? "Add Customer"
                        : "Edit Customer",
                ModalityType.APPLICATION_MODAL
        );

        this.customerToEdit =
                customerToEdit;

        customerDAO =
                new CustomerDAO();

        customerSaved = false;


        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setSize(
                520,
                560
        );

        setMinimumSize(
                new Dimension(
                        480,
                        520
                )
        );


        createInterface();


        if (customerToEdit != null) {
            populateFields();
        }


        setLocationRelativeTo(
                owner
        );
    }


    // ==========================================
    // INTERFACE
    // ==========================================

    private void createInterface() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(
                BACKGROUND
        );

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        28, 30, 24, 30
                )
        );


        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        root.add(
                createForm(),
                BorderLayout.CENTER
        );

        root.add(
                createButtons(),
                BorderLayout.SOUTH
        );


        setContentPane(root);
    }


    // ==========================================
    // HEADER
    // ==========================================

    private JPanel createHeader() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        
        JLabel title =
            new JLabel(
                    customerToEdit == null
                            ? "Add Customer"
                            : "Edit Customer"
            );
        

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(
                TEXT_PRIMARY
        );

        JLabel subtitle =
            new JLabel(
                    customerToEdit == null
                            ? "Enter the customer's basic information."
                            : "Update the customer's information."
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


        panel.add(title);

        panel.add(
                Box.createVerticalStrut(5)
        );

        panel.add(subtitle);

        panel.add(
                Box.createVerticalStrut(24)
        );


        return panel;
    }


    // ==========================================
    // FORM
    // ==========================================

    private JPanel createForm() {

        JPanel form =
                new JPanel();

        form.setOpaque(false);

        form.setLayout(
                new BoxLayout(
                        form,
                        BoxLayout.Y_AXIS
                )
        );


        // ---------- Name ----------

        nameField =
                new PlaceholderTextField(
                        "e.g. Nimal Perera",
                        25
                );

        nameField.setToolTipText(
                "Enter the customer's full name"
        );

        styleTextField(nameField);


        form.add(
                createField(
                        "Customer Name *",
                        nameField
                )
        );

        form.add(
                Box.createVerticalStrut(14)
        );


        // ---------- Phone ----------

        phoneField =
                new PlaceholderTextField(
                        "e.g. 0771234567",
                        25
                );

        phoneField.setToolTipText(
                "Enter the customer's phone number"
        );

        styleTextField(phoneField);


        form.add(
                createField(
                        "Phone",
                        phoneField
                )
        );

        form.add(
                Box.createVerticalStrut(14)
        );


        // ---------- Email ----------

        emailField =
                new PlaceholderTextField(
                        "e.g. customer@example.com",
                        25
                );

        emailField.setToolTipText(
                "Enter the customer's email address"
        );

        styleTextField(emailField);


        form.add(
                createField(
                        "Email",
                        emailField
                )
        );

        form.add(
                Box.createVerticalStrut(14)
        );


        // ---------- Address ----------

        addressArea =
                new JTextArea(
                        4,
                        25
                );

        addressArea.setLineWrap(
                true
        );

        addressArea.setWrapStyleWord(
                true
        );

        addressArea.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        addressArea.setToolTipText(
                "Enter the customer's address"
        );


        JScrollPane addressScrollPane =
                new JScrollPane(
                        addressArea
                );

        addressScrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );


        form.add(
                createField(
                        "Address",
                        addressScrollPane
                )
        );


        return form;
    }


    // ==========================================
    // REUSABLE FIELD ROW
    // ==========================================

    private JPanel createField(
            String labelText,
            java.awt.Component component
    ) {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel label =
                new JLabel(
                        labelText
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        label.setForeground(
                TEXT_PRIMARY
        );

        label.setAlignmentX(
                LEFT_ALIGNMENT
        );


        component.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        component
                                .getPreferredSize()
                                .height
                )
        );


        panel.add(label);

        panel.add(
                Box.createVerticalStrut(6)
        );

        panel.add(component);


        return panel;
    }


    // ==========================================
    // BUTTONS
    // ==========================================

    private JPanel createButtons() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 0, 0, 0
                )
        );


        JButton cancelButton =
                new JButton(
                        "Cancel"
                );

        cancelButton.setFocusPainted(
                false
        );

        cancelButton.addActionListener(
                e -> dispose()
        );

        JButton saveButton =
            new JButton(
                    customerToEdit == null
                            ? "Save Customer"
                            : "Save Changes"
            );


        saveButton.setForeground(
                Color.WHITE
        );

        saveButton.setBackground(
                PRIMARY
        );

        saveButton.setFocusPainted(
                false
        );

        saveButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        saveButton.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16
                )
        );

        saveButton.addActionListener(
                e -> saveCustomer()
        );


        panel.add(cancelButton);
        panel.add(saveButton);


        return panel;
    }

    // ==========================================
    // POPULATE EDIT FORM
    // ==========================================

    private void populateFields() {

        nameField.setText(
                customerToEdit.getName()
        );

        phoneField.setText(
                customerToEdit.getPhone() == null
                        ? ""
                        : customerToEdit.getPhone()
        );

        emailField.setText(
                customerToEdit.getEmail() == null
                        ? ""
                        : customerToEdit.getEmail()
        );

        addressArea.setText(
                customerToEdit.getAddress() == null
                        ? ""
                        : customerToEdit.getAddress()
        );
    }


    // ==========================================
    // SAVE CUSTOMER
    // ==========================================

    private void saveCustomer() {

        String name =
                nameField
                        .getText()
                        .trim();

        String phone =
                phoneField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String address =
                addressArea
                        .getText()
                        .trim();


        // ---------- Required field ----------

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Customer name is required.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocusInWindow();

            return;
        }


        // ---------- Basic email validation ----------

        if (!email.isEmpty()
                && (!email.contains("@")
                || !email.contains("."))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            emailField.requestFocusInWindow();

            return;
        }  
        // ==========================================
        // POSSIBLE DUPLICATE NAME WARNING
        // ==========================================

        if (customerToEdit == null) {

            try {

                boolean nameExists =
                        customerDAO
                                .customerNameExists(
                                        name
                                );


                if (nameExists) {

                    Object[] options = {
                        "Add Anyway",
                        "Cancel"
                    };


                    int choice =
                            JOptionPane.showOptionDialog(
                                    this,
                                    "A customer named \""
                                            + name
                                            + "\" already exists.\n\n"
                                            + "This may be a different person. "
                                            + "Do you still want to add this customer?",
                                    "Possible Duplicate Customer",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    options,
                                    options[1]
                            );


                    if (choice != 0) {
                        return;
                    }
                }


            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to check for duplicate customers.\n"
                                + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }
        }
        
        // ---------- Create model ----------
        Customer customer;

        if (customerToEdit == null) {

            customer =
                    new Customer();

            customer.setStatus(
                    "ACTIVE"
            );

        } else {

            customer =
                    customerToEdit;
        }

        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customer.setAddress(address);

        // ---------- Save to database ----------

        try {

            boolean saved;

            if (customerToEdit == null) {

                saved =
                        customerDAO
                                .addCustomer(
                                        customer
                                );

            } else {

                saved =
                        customerDAO
                                .updateCustomer(
                                        customer
                                );
            }

            if (saved) {

                customerSaved = true;

                JOptionPane.showMessageDialog(
                        this,
                        //"Customer added successfully.",
                        customerToEdit == null
                                ? "Customer added successfully."
                                : "Customer updated successfully.",                       
                        "Customer Saved",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Customer could not be saved.",
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            
        } catch (SQLException e) {

            String errorMessage =
                    getDatabaseErrorMessage(e);

            Object[] options = {
                "Retry"
            };

            JOptionPane.showOptionDialog(
                    this,
                    errorMessage,
                    "Customer Already Registered",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[0]
            );


            String databaseMessage =
                    e.getMessage() == null
                            ? ""
                            : e.getMessage().toLowerCase();


            if (databaseMessage.contains(
                    "uq_customer_phone"
            )) {

                phoneField.requestFocusInWindow();

                phoneField.selectAll();

            } else if (databaseMessage.contains(
                    "uq_customer_email"
            )) {

                emailField.requestFocusInWindow();

                emailField.selectAll();
            }
        }    
        
    }


    // ==========================================
    // DATABASE ERROR MESSAGE
    // ==========================================

    private String getDatabaseErrorMessage(
            SQLException e
    ) {

        String message =
                e.getMessage() == null
                        ? ""
                        : e.getMessage()
                                .toLowerCase();


        if (message.contains(
                "uq_customer_phone"
        )) {

            return "That phone number is already registered.";
        }


        if (message.contains(
                "uq_customer_email"
        )) {

            return "That email address is already registered.";
        }


        if (message.contains(
                "duplicate"
        )) {

            return "A customer with the same phone number or email already exists.";
        }


        return "Unable to save customer.\n"
                + e.getMessage();
    }


    // ==========================================
    // TEXT FIELD STYLE
    // ==========================================

    private void styleTextField(
            PlaceholderTextField field
    ) {

        field.setPreferredSize(
                new Dimension(
                        400,
                        34
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        34
                )
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 8, 5, 8
                        )
                )
        );
    }


    // ==========================================
    // RESULT
    // ==========================================

    public boolean isCustomerSaved() {

        return customerSaved;
    }
}