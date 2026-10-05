package com.supermart.view;

import com.supermart.dao.CategoryDAO;
import com.supermart.dao.ProductDAO;
import com.supermart.dao.SupplierDAO;
import com.supermart.model.Category;
import com.supermart.model.Product;
import com.supermart.model.Supplier;
import com.supermart.view.component.PlaceholderTextField;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

public class AddProductDialog extends JDialog {
    
    private final PlaceholderTextField barcodeField =
            new PlaceholderTextField(
                    "e.g. 100004",
                    20
            );

    private final PlaceholderTextField nameField =
            new PlaceholderTextField(
                    "e.g. Orange Juice 1L",
                    20
            );

    private final JComboBox<Category> categoryComboBox =
            new JComboBox<>();

    private final JComboBox<Supplier> supplierComboBox =
            new JComboBox<>();

    private final PlaceholderTextField costPriceField =
            new PlaceholderTextField(
                    "e.g. 350.00",
                    20
            );

    private final PlaceholderTextField sellingPriceField =
            new PlaceholderTextField(
                    "e.g. 450.00",
                    20
            );

    private final PlaceholderTextField quantityField =
            new PlaceholderTextField(
                    "e.g. 20",
                    20
            );

    private final PlaceholderTextField reorderLevelField =
            new PlaceholderTextField(
                    "e.g. 10",
                    20
            );

    private final ProductDAO productDAO;

    private boolean productAdded = false;
    
    private boolean productUpdated = false;
    private Product productToEdit;

    public AddProductDialog(JFrame parent) {

        super(parent, "Add Product", true);

        productDAO = new ProductDAO();
        
        
        barcodeField.setToolTipText(
                "Enter the unique barcode used to identify the product"
        );

        nameField.setToolTipText(
                "Enter the product name"
        );

        categoryComboBox.setToolTipText(
                "Select the category this product belongs to"
        );

        supplierComboBox.setToolTipText(
                "Select the supplier that provides this product"
        );

        costPriceField.setToolTipText(
                "Enter the purchase cost of one unit"
        );

        sellingPriceField.setToolTipText(
                "Enter the selling price of one unit"
        );

        quantityField.setToolTipText(
                "Enter the opening stock quantity"
        );

        reorderLevelField.setToolTipText(
                "Enter the stock level at which this product should be considered low stock"
        );

        
        
        

        setSize(520, 650);
        setLocationRelativeTo(parent);
        setResizable(false);

        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        loadCategoriesAndSuppliers();
    }
    
    private void loadProductData() {

        if (productToEdit == null) {
            return;
        }

        barcodeField.setText(
                productToEdit.getBarcode()
        );

        nameField.setText(
                productToEdit.getName()
        );

        costPriceField.setText(
                productToEdit.getCostPrice().toString()
        );

        sellingPriceField.setText(
                productToEdit.getSellingPrice().toString()
        );

        quantityField.setText(
                String.valueOf(
                        productToEdit.getQuantity()
                )
        );

        reorderLevelField.setText(
                String.valueOf(
                        productToEdit.getReorderLevel()
                )
        );


        // Select correct category
        for (int i = 0;
             i < categoryComboBox.getItemCount();
             i++) {

            Category category =
                    categoryComboBox.getItemAt(i);

            if (category.getCategoryId()
                    == productToEdit.getCategoryId()) {

                categoryComboBox.setSelectedIndex(i);
                break;
            }
        }


        // Select correct supplier
        if (productToEdit.getSupplierId() != null) {

            for (int i = 0;
                 i < supplierComboBox.getItemCount();
                 i++) {

                Supplier supplier =
                        supplierComboBox.getItemAt(i);

                if (supplier.getSupplierId()
                        == productToEdit.getSupplierId()) {

                    supplierComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    public AddProductDialog(
        JFrame parent,
        Product product
    ) {

        this(parent);

        this.productToEdit = product;

        setTitle("Edit Product");

        loadProductData();
    }
    
    

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 30, 15, 30
                )
        );

        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Add Product");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Enter the product information below."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                new Color(110, 113, 120)
        );

        title.setAlignmentX(LEFT_ALIGNMENT);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);

        return panel;
    }

    // =========================================================
    // FORM
    // =========================================================

    private JPanel createForm() {

        JPanel form =
                new JPanel(
                        new GridLayout(
                                8,
                                2,
                                12,
                                12
                        )
                );

        form.setBackground(Color.WHITE);

        form.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 30, 20, 30
                )
        );

        form.add(new JLabel("Barcode"));
        form.add(barcodeField);

        form.add(new JLabel("Product Name"));
        form.add(nameField);

        form.add(new JLabel("Category"));
        form.add(categoryComboBox);

        form.add(new JLabel("Supplier"));
        form.add(supplierComboBox);

        form.add(new JLabel("Cost Price"));
        form.add(costPriceField);

        form.add(new JLabel("Selling Price"));
        form.add(sellingPriceField);

        form.add(new JLabel("Quantity"));
        form.add(quantityField);

        form.add(new JLabel("Reorder Level"));
        form.add(reorderLevelField);

        return form;
    }

    // =========================================================
    // BUTTONS
    // =========================================================

    private JPanel createButtons() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 30, 20, 30
                )
        );

        JButton cancelButton =
                new JButton("Cancel");

        JButton saveButton =
                new JButton("Save Product");

        saveButton.setBackground(
                new Color(25, 25, 28)
        );

        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        cancelButton.addActionListener(
                e -> dispose()
        );

        saveButton.addActionListener(
                e -> saveProduct()
        );

        panel.add(cancelButton);
        panel.add(saveButton);

        return panel;
    }

    // =========================================================
    // LOAD CATEGORY + SUPPLIER DATA
    // =========================================================

    private void loadCategoriesAndSuppliers() {

        CategoryDAO categoryDAO =
                new CategoryDAO();

        SupplierDAO supplierDAO =
                new SupplierDAO();

        try {

            for (Category category
                    : categoryDAO.getAllCategories()) {

                categoryComboBox.addItem(category);
            }

            for (Supplier supplier
                    : supplierDAO.getAllSuppliers()) {

                supplierComboBox.addItem(supplier);
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load categories or suppliers.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // SAVE PRODUCT
    // =========================================================

    private void saveProduct() {

        try {

            // -------------------------
            // Required text fields
            // -------------------------

            if (barcodeField
                    .getText()
                    .trim()
                    .isEmpty()) {

                showValidationError(
                        "Barcode is required."
                );

                return;
            }

            if (nameField
                    .getText()
                    .trim()
                    .isEmpty()) {

                showValidationError(
                        "Product name is required."
                );

                return;
            }

            if (costPriceField
                    .getText()
                    .trim()
                    .isEmpty()) {

                showValidationError(
                        "Cost price is required."
                );

                return;
            }

            if (sellingPriceField
                    .getText()
                    .trim()
                    .isEmpty()) {

                showValidationError(
                        "Selling price is required."
                );

                return;
            }

            if (quantityField
                    .getText()
                    .trim()
                    .isEmpty()) {

                showValidationError(
                        "Quantity is required."
                );

                return;
            }

            if (reorderLevelField
                    .getText()
                    .trim()
                    .isEmpty()) {

                showValidationError(
                        "Reorder level is required."
                );

                return;
            }

            // -------------------------
            // Selected objects
            // -------------------------

            Category selectedCategory =
                    (Category)
                            categoryComboBox
                                    .getSelectedItem();

            Supplier selectedSupplier =
                    (Supplier)
                            supplierComboBox
                                    .getSelectedItem();

            if (selectedCategory == null) {

                showValidationError(
                        "Please select a category."
                );

                return;
            }

            if (selectedSupplier == null) {

                showValidationError(
                        "Please select a supplier."
                );

                return;
            }

            // -------------------------
            // Convert numeric values
            // -------------------------

            BigDecimal costPrice =
                    new BigDecimal(
                            costPriceField
                                    .getText()
                                    .trim()
                    );

            BigDecimal sellingPrice =
                    new BigDecimal(
                            sellingPriceField
                                    .getText()
                                    .trim()
                    );

            int quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            int reorderLevel =
                    Integer.parseInt(
                            reorderLevelField
                                    .getText()
                                    .trim()
                    );

            // -------------------------
            // Business validation
            // -------------------------

            if (costPrice.compareTo(
                    BigDecimal.ZERO) < 0) {

                showValidationError(
                        "Cost price cannot be negative."
                );

                return;
            }

            if (sellingPrice.compareTo(
                    BigDecimal.ZERO) <= 0) {

                showValidationError(
                        "Selling price must be greater than zero."
                );

                return;
            }

            if (quantity < 0) {

                showValidationError(
                        "Quantity cannot be negative."
                );

                return;
            }

            if (reorderLevel < 0) {

                showValidationError(
                        "Reorder level cannot be negative."
                );

                return;
            }

            // -------------------------
            // Build Product object
            // -------------------------

            Product product = new Product();

            product.setBarcode(
                    barcodeField
                            .getText()
                            .trim()
            );

            product.setName(
                    nameField
                            .getText()
                            .trim()
            );

            product.setCategoryId(
                    selectedCategory
                            .getCategoryId()
            );

            product.setSupplierId(
                    selectedSupplier
                            .getSupplierId()
            );

            product.setCostPrice(costPrice);
            product.setSellingPrice(sellingPrice);
            product.setQuantity(quantity);
            product.setReorderLevel(reorderLevel);
            
            if (productToEdit == null) {

                product.setStatus(
                        "ACTIVE"
                );

            } else {

                product.setStatus(
                        productToEdit.getStatus()
                );
            }
            
            if (productToEdit == null) {

                // =========================
                // ADD MODE
                // =========================

                boolean saved =
                        productDAO.addProduct(product);

                if (saved) {

                    productAdded = true;

                    JOptionPane.showMessageDialog(
                            this,
                            "Product added successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }

            } else {

                // =========================
                // EDIT MODE
                // =========================

                product.setProductId(
                        productToEdit.getProductId()
                );

                boolean updated =
                        productDAO.updateProduct(product);

                if (updated) {

                    productUpdated = true;

                    JOptionPane.showMessageDialog(
                            this,
                            "Product updated successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose();
                }
            }
            
        } catch (NumberFormatException e) {

            showValidationError(
                    "Prices, quantity and reorder level "
                            + "must contain valid numbers."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save product.\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // VALIDATION MESSAGE
    // =========================================================

    private void showValidationError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid Input",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =========================================================
    // RESULT
    // =========================================================

    public boolean isProductAdded() {
        return productAdded;
    }
    
    public boolean isProductUpdated() {
        return productUpdated;
    }
}