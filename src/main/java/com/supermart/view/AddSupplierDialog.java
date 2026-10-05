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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddSupplierDialog extends JDialog {

    private static final Color BACKGROUND =
            Color.WHITE;

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color BORDER =
            new Color(220, 223, 228);

    private static final Color PRIMARY_BUTTON =
            new Color(25, 25, 28);


    private final SupplierDAO supplierDAO;
    
    private final PlaceholderTextField nameField;
    private final PlaceholderTextField phoneField;
    private final PlaceholderTextField emailField;
    private final PlaceholderTextField addressField;

    private boolean supplierSaved = false;
    private Supplier supplierToEdit;


    // ==========================================
    // constructor
    // ==========================================
    
    public AddSupplierDialog(
            Window owner
    ) {

        this(
                owner,
                null
        );
    }


    public AddSupplierDialog(
            Window owner,
            Supplier supplierToEdit
    ) {

        super(
                owner,
                supplierToEdit == null
                        ? "Add Supplier"
                        : "Edit Supplier",
                ModalityType.APPLICATION_MODAL
        );


        this.supplierToEdit =
                supplierToEdit;

        supplierDAO =
                new SupplierDAO();


        nameField =
                createTextField(
                        "e.g. Fresh Foods Ltd",
                        "Enter the supplier or company name"
                );

        phoneField =
                createTextField(
                        "e.g. 0771234567",
                        "Enter the supplier's contact number"
                );

        emailField =
                createTextField(
                        "e.g. sales@freshfoods.lk",
                        "Enter the supplier's email address"
                );

        addressField =
                createTextField(
                        "e.g. Colombo",
                        "Enter the supplier's business or contact address"
                );


        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setResizable(false);

        setLayout(
                new BorderLayout()
        );

        getContentPane().setBackground(
                BACKGROUND
        );


        add(
                createHeader(),
                BorderLayout.NORTH
        );

        add(
                createForm(),
                BorderLayout.CENTER
        );

        add(
                createActions(),
                BorderLayout.SOUTH
        );


        pack();

        setMinimumSize(
                new Dimension(
                        500,
                        430
                )
        );


        // Fill the fields only when editing.
        if (supplierToEdit != null) {

            populateFields();
        }


        setLocationRelativeTo(
                owner
        );
    }
    
    // ==========================================
    // HEADER
    // ==========================================

    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        18,
                        30
                )
        );


        JPanel textPanel =
                new JPanel(
                        new GridBagLayout()
                );

        textPanel.setBackground(
                BACKGROUND
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor =
                GridBagConstraints.WEST;

        JLabel title =
                new JLabel(
                        supplierToEdit == null
                                ? "Add Supplier"
                                : "Edit Supplier"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(
                TEXT_PRIMARY
        );


        textPanel.add(
                title,
                gbc
        );


        gbc.gridy++;
        gbc.insets =
                new Insets(
                        5,
                        0,
                        0,
                        0
                );
        
        JLabel subtitle =
                new JLabel(
                        supplierToEdit == null
                                ? "Enter the supplier's contact and business information."
                                : "Update the supplier's contact and business information."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                TEXT_SECONDARY
        );


        textPanel.add(
                subtitle,
                gbc
        );


        panel.add(
                textPanel,
                BorderLayout.WEST
        );


        return panel;
    }


    // ==========================================
    // FORM
    // ==========================================

    private JPanel createForm() {

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(
                BACKGROUND
        );
        
        
        form.setBorder(
                BorderFactory.createEmptyBorder(
                        22,
                        30,
                        22,
                        30
                )
        );



        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill =
                GridBagConstraints.HORIZONTAL;
        gbc.anchor =
                GridBagConstraints.WEST;


        addField(
                form,
                gbc,
                "Supplier Name *",
                nameField
        );


        addField(
                form,
                gbc,
                "Phone",
                phoneField
        );


        addField(
                form,
                gbc,
                "Email",
                emailField
        );


        addField(
                form,
                gbc,
                "Address",
                addressField
        );


        return form;
    }


    private void addField(
            JPanel form,
            GridBagConstraints gbc,
            String labelText,
            JTextField field
    ) {

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


        gbc.insets =
                new Insets(
                        0,
                        0,
                        6,
                        0
                );

        form.add(
                label,
                gbc
        );


        gbc.gridy++;

        gbc.insets =
                new Insets(
                        0,
                        0,
                        16,
                        0
                );

        form.add(
                field,
                gbc
        );


        gbc.gridy++;
    }


    // ==========================================
    // ACTION BUTTONS
    // ==========================================

    private JPanel createActions() {

        JPanel panel =
                new JPanel(
                        new java.awt.FlowLayout(
                                java.awt.FlowLayout.RIGHT,
                                10,
                                16
                        )
                );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        20,
                        4,
                        20
                )
        );


        JButton cancelButton =
                new JButton(
                        "Cancel"
                );
       
        JButton saveButton =
                new JButton(
                        supplierToEdit == null
                                ? "Add Supplier"
                                : "Save Changes"
                );
        
        styleSecondaryButton(
                cancelButton
        );

        stylePrimaryButton(
                saveButton
        );


        cancelButton.addActionListener(
                e -> dispose()
        );


        saveButton.addActionListener(
                e -> saveSupplier()
        );


        panel.add(
                cancelButton
        );

        panel.add(
                saveButton
        );


        return panel;
    }
    

    // ==========================================
    // POPULATE EDIT FORM
    // ==========================================

    private void populateFields() {

        nameField.setText(
                supplierToEdit.getName()
        );

        phoneField.setText(
                supplierToEdit.getPhone() == null
                        ? ""
                        : supplierToEdit.getPhone()
        );

        emailField.setText(
                supplierToEdit.getEmail() == null
                        ? ""
                        : supplierToEdit.getEmail()
        );

        addressField.setText(
                supplierToEdit.getAddress() == null
                        ? ""
                        : supplierToEdit.getAddress()
        );
    }

    // ==========================================
    // SAVE
    // ==========================================

    private void saveSupplier() {

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
                addressField
                        .getText()
                        .trim();


        // ------------------------------------------
        // REQUIRED NAME
        // ------------------------------------------

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier name is required.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocusInWindow();

            return;
        }


        // ------------------------------------------
        // NAME LENGTH
        // ------------------------------------------

        if (name.length() > 150) {

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier name cannot exceed 150 characters.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocusInWindow();

            return;
        }


        // ------------------------------------------
        // PHONE VALIDATION
        // Optional, but if entered it must contain
        // only sensible phone characters.
        // ------------------------------------------

        if (!phone.isEmpty()
                && !phone.matches(
                        "[0-9+()\\-\\s]{7,20}"
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid phone number.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            phoneField.requestFocusInWindow();

            return;
        }


        // ------------------------------------------
        // EMAIL VALIDATION
        // ------------------------------------------

        if (!email.isEmpty()
                && !email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid email address.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            emailField.requestFocusInWindow();

            return;
        }


        if (email.length() > 150) {

            JOptionPane.showMessageDialog(
                    this,
                    "Email cannot exceed 150 characters.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            emailField.requestFocusInWindow();

            return;
        }


        if (address.length() > 255) {

            JOptionPane.showMessageDialog(
                    this,
                    "Address cannot exceed 255 characters.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            addressField.requestFocusInWindow();

            return;
        }


        try {

            // ======================================
            // DUPLICATE NAME CHECK
            // ======================================
            
            boolean duplicateName;


            if (supplierToEdit == null) {

                duplicateName =
                        supplierDAO
                                .supplierNameExists(
                                        name
                                );

            } else {

                duplicateName =
                        supplierDAO
                                .supplierNameExistsExcept(
                                        name,
                                        supplierToEdit
                                                .getSupplierId()
                                );
            }


            if (duplicateName) {

                Object[] options = {
                    supplierToEdit == null
                            ? "Add Anyway"
                            : "Save Anyway",

                    "Cancel"
                };


                int choice =
                        JOptionPane.showOptionDialog(
                                this,
                                "A supplier named \""
                                        + name
                                        + "\" already exists.\n\n"
                                        + (supplierToEdit == null
                                                ? "Do you still want to add this supplier?"
                                                : "Do you still want to save these changes?"),
                                "Possible Duplicate Supplier",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                options,
                                options[1]
                        );


                if (choice != 0) {
                    return;
                }
            }

            // ======================================
            // CREATE / UPDATE SUPPLIER OBJECT
            // ======================================

            Supplier supplier;


            if (supplierToEdit == null) {

                // ADD MODE
                supplier =
                        new Supplier();

                supplier.setStatus(
                        "ACTIVE"
                );

            } else {

                // EDIT MODE
                supplier =
                        supplierToEdit;
            }


            supplier.setName(
                    name
            );

            supplier.setPhone(
                    phone
            );

            supplier.setEmail(
                    email
            );

            supplier.setAddress(
                    address
            );


            // ======================================
            // SAVE TO DATABASE
            // ======================================

            boolean saved;


            if (supplierToEdit == null) {

                saved =
                        supplierDAO
                                .addSupplier(
                                        supplier
                                );

            } else {

                saved =
                        supplierDAO
                                .updateSupplier(
                                        supplier
                                );
            }

            if (saved) {

                supplierSaved = true;

                JOptionPane.showMessageDialog(
                        this,
                        supplierToEdit == null
                                ? "Supplier added successfully."
                                : "Supplier updated successfully.",
                        "Supplier Saved",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        supplierToEdit == null
                                ? "Supplier could not be added."
                                : "Supplier could not be updated.",
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    (supplierToEdit == null
                            ? "Unable to add supplier.\n"
                            : "Unable to update supplier.\n")
                            + e.getMessage(),
                   
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // ==========================================
    // RESULT
    // ==========================================

    public boolean isSupplierSaved() {

        return supplierSaved;
    }


    // ==========================================
    // FIELD STYLE
    // ==========================================

 
    
    private PlaceholderTextField createTextField(
            String placeholder,
            String tooltip
    ) {

        PlaceholderTextField field =
                new PlaceholderTextField(
                        placeholder,
                        25
                );

        field.setToolTipText(
                tooltip
        );

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        field.setPreferredSize(
                new Dimension(
                        400,
                        36
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                10,
                                7,
                                10
                        )
                )
        );

        return field;
    } 

    // ==========================================
    // BUTTON STYLE
    // ==========================================

    private void stylePrimaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                PRIMARY_BUTTON
        );

        button.setFocusPainted(
                false
        );

        button.setPreferredSize(
                new Dimension(
                        120,
                        36
                )
        );
    }


    private void styleSecondaryButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setPreferredSize(
                new Dimension(
                        90,
                        36
                )
        );
    }
}