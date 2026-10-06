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
import com.supermart.model.Employee;
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
import javax.swing.JComboBox;

public class AddEmployeeDialog extends JDialog {

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


    private final EmployeeDAO employeeDAO;

    private PlaceholderTextField nameField;
    private PlaceholderTextField phoneField;
    private PlaceholderTextField emailField;

    
    private JComboBox<String> roleCombo;

    private boolean employeeSaved;
    private Employee employeeToEdit;

    public AddEmployeeDialog(
            Window owner
    ) {

        this(owner, null);
    }


    public AddEmployeeDialog(
            Window owner,
            Employee employeeToEdit
    ) {

        super(
                owner,
                employeeToEdit == null
                        ? "Add Employee"
                        : "Edit Employee",
                ModalityType.APPLICATION_MODAL
        );

        this.employeeToEdit =
                employeeToEdit;

        employeeDAO =
                new EmployeeDAO();

        employeeSaved = false;


        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setSize(
                520,
                500 //560
        );

        setMinimumSize(
                new Dimension(
                        480,
                        460 //520
                )
        );


        createInterface();


        if (employeeToEdit != null) {
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
                    employeeToEdit == null
                            ? "Add Employee"
                            : "Edit Employee"
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
                    employeeToEdit == null
                            ? "Enter the employee's basic information."
                            : "Update the employee's information."
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
                "Enter the employee's full name"
        );

        styleTextField(nameField);

        form.add(
                createField(
                        "Employee Name *",
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
                "Enter the employee's phone number"
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
                        "e.g. employee@example.com",
                        25
                );

        emailField.setToolTipText(
                "Enter the employee's email address"
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


        // ---------- Role ----------

        roleCombo =
                new JComboBox<>(
                        new String[]{
                            "MANAGER",
                            "CASHIER",
                            "STOCK_CLERK"
                        }
                );

        roleCombo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        roleCombo.setPreferredSize(
                new Dimension(
                        400,
                        34
                )
        );

        roleCombo.setToolTipText(
                "Select the employee's role"
        );

        form.add(
                createField(
                        "Role *",
                        roleCombo
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
                    employeeToEdit == null
                            ? "Save Employee"
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
                e -> saveEmployee()
        );


        panel.add(cancelButton);
        panel.add(saveButton);


        return panel;
    }

    private void populateFields() {

        nameField.setText(
                employeeToEdit.getName()
        );

        phoneField.setText(
                employeeToEdit.getPhone() == null
                        ? ""
                        : employeeToEdit.getPhone()
        );

        emailField.setText(
                employeeToEdit.getEmail() == null
                        ? ""
                        : employeeToEdit.getEmail()
        );

        roleCombo.setSelectedItem(
                employeeToEdit.getRole()
        );
    }
    

    // ==========================================
    // SAVE EMPLOYEE
    // ==========================================
    
    private void saveEmployee() {

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

        String role =
                (String) roleCombo
                        .getSelectedItem();


        // ==========================================
        // VALIDATION
        // ==========================================

        // ---------- Name required ----------

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee name is required.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocusInWindow();

            return;
        }


        // ---------- Name length ----------

        if (name.length() > 150) {

            JOptionPane.showMessageDialog(
                    this,
                    "Employee name cannot exceed 150 characters.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            nameField.requestFocusInWindow();

            return;
        }


        // ---------- Phone validation ----------

        if (!phone.isEmpty()
                && !phone.matches("[0-9+()\\-\\s]{7,20}")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid phone number.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            phoneField.requestFocusInWindow();

            return;
        }


        // ---------- Email length ----------

        if (email.length() > 150) {

            JOptionPane.showMessageDialog(
                    this,
                    "Email address cannot exceed 150 characters.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            emailField.requestFocusInWindow();

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


        // ---------- Role required ----------

        if (role == null
                || role.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an employee role.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            roleCombo.requestFocusInWindow();

            return;
        }


        // ==========================================
        // DUPLICATE EMAIL CHECK
        // ==========================================

        if (!email.isEmpty()) {

            try {

                boolean emailExists;

                if (employeeToEdit == null) {

                    emailExists =
                            employeeDAO
                                    .employeeEmailExists(
                                            email
                                    );

                } else {

                    emailExists =
                            employeeDAO
                                    .employeeEmailExistsExcept(
                                            email,
                                            employeeToEdit
                                                    .getEmployeeId()
                                    );
                }


                if (emailExists) {

                    JOptionPane.showMessageDialog(
                            this,
                            "That email address is already registered "
                                    + "to another employee.",
                            "Duplicate Employee",
                            JOptionPane.WARNING_MESSAGE
                    );

                    emailField.requestFocusInWindow();
                    emailField.selectAll();

                    return;
                }


            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to check employee email.\n"
                                + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }
        }


        // ==========================================
        // CREATE / UPDATE MODEL
        // ==========================================

        Employee employee;

        if (employeeToEdit == null) {

            employee =
                    new Employee();

            employee.setStatus(
                    "ACTIVE"
            );

        } else {

            employee =
                    employeeToEdit;
        }


        employee.setName(name);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setRole(role);


        // ==========================================
        // SAVE TO DATABASE
        // ==========================================

        try {

            boolean saved;

            if (employeeToEdit == null) {

                saved =
                        employeeDAO
                                .addEmployee(
                                        employee
                                );

            } else {

                saved =
                        employeeDAO
                                .updateEmployee(
                                        employee
                                );
            }


            if (saved) {

                employeeSaved = true;

                JOptionPane.showMessageDialog(
                        this,
                        employeeToEdit == null
                                ? "Employee added successfully."
                                : "Employee updated successfully.",
                        "Employee Saved",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Employee could not be saved.",
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }


        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    getDatabaseErrorMessage(e),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String getDatabaseErrorMessage(
            SQLException e
    ) {

        String message =
                e.getMessage() == null
                        ? ""
                        : e.getMessage()
                                .toLowerCase();


        if (message.contains(
                "uq_employee_email"
        )) {

            return "That email address is already registered "
                    + "to another employee.";
        }


        if (message.contains(
                "duplicate"
        )) {

            return "An employee with that email address already exists.";
        }


        return "Unable to save employee.\n"
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

    public boolean isEmployeeSaved() {

        return employeeSaved;
    }
}