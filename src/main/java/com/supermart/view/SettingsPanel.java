/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */
import com.supermart.util.AppPreferences;
import com.supermart.util.AppearanceManager;
import com.supermart.view.component.WidthTrackingPanel;

import com.supermart.util.DBConnection;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;


public class SettingsPanel extends JPanel {

    private static final Color BACKGROUND =
            new Color(247, 248, 250);

    private static final Color SURFACE =
            Color.WHITE;

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color BORDER =
            new Color(225, 228, 232);

    private JRadioButton lightThemeButton;
    private JRadioButton darkThemeButton;

    private JLabel fontSizeValue;

    private int selectedFontSize;
    private JTextField storeNameField;


    public SettingsPanel() {

        selectedFontSize =
                AppPreferences.getFontSize();

        setLayout(new BorderLayout());

        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        16, //32
                        18, //36
                        16,
                        18
                )
        );
        /*
        add(
                createContent(),
                BorderLayout.NORTH
        ); */
        
        
        JScrollPane scrollPane =
                new JScrollPane(createContent());

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        
        
    }


    private JPanel createContent() {
        /*
        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        ); */
        
        /*
        
        JPanel content = new JPanel(); */

        JPanel content = new WidthTrackingPanel();
        
        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        content.setPreferredSize(null);
        
        
        


        JLabel title =
                new JLabel("Settings");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );

        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);


        JLabel subtitle =
                new JLabel(
                        "Customize your SuperMart experience"
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);


        content.add(title);
        content.add(Box.createVerticalStrut(5));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(28));


        JPanel appearanceCard =
                createAppearanceCard();

        appearanceCard.setAlignmentX(
                LEFT_ALIGNMENT
        );
        /*
        content.add(appearanceCard);

        return content; */
        
        content.add(appearanceCard);

        content.add(Box.createVerticalStrut(16));
        /*
        JPanel storeCard = createStoreInformationCard();
        storeCard.setAlignmentX(LEFT_ALIGNMENT);
        content.add(storeCard);

        content.add(Box.createVerticalStrut(16));

        JPanel systemCard = createSystemInformationCard(); */
        
        JPanel storeCard = createStoreInformationCard();
        storeCard.setAlignmentX(LEFT_ALIGNMENT);
        content.add(storeCard);

        content.add(Box.createVerticalStrut(16));

        // Database Connection
        JPanel databaseCard = createDatabaseConnectionCard();
        databaseCard.setAlignmentX(LEFT_ALIGNMENT);
        content.add(databaseCard);

        content.add(Box.createVerticalStrut(16));

        JPanel systemCard = createSystemInformationCard();
         
        
        systemCard.setAlignmentX(LEFT_ALIGNMENT);
        content.add(systemCard);

        content.add(Box.createVerticalStrut(24));

        return content;
        
        
    }

    private JPanel createAppearanceCard() {

        JPanel card =
                new JPanel();

        card.setBackground(SURFACE);

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                24,
                                26,
                                24,
                                26
                        )
                )
        );

        /*
        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        330
                )
        );

        card.setPreferredSize(
                new Dimension(
                        850,
                        330
                )
        ); */
        /*
        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        400
                )
        );

        card.setPreferredSize(
                new Dimension(
                        850,
                        400
                )
        ); */


        JLabel heading =
                new JLabel("Appearance");

        heading.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        heading.setForeground(TEXT_PRIMARY);
        heading.setAlignmentX(LEFT_ALIGNMENT);


        JLabel description =
                new JLabel(
                        "Choose how SuperMart looks on this computer."
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        description.setForeground(TEXT_SECONDARY);
        description.setAlignmentX(LEFT_ALIGNMENT);


        card.add(heading);
        card.add(Box.createVerticalStrut(5));
        card.add(description);
        card.add(Box.createVerticalStrut(26));


        // ==========================================
        // THEME
        // ==========================================

        JLabel themeLabel =
                new JLabel("Theme");

        themeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        themeLabel.setForeground(TEXT_PRIMARY);
        themeLabel.setAlignmentX(LEFT_ALIGNMENT);


        JLabel themeDescription =
                new JLabel(
                        "Select the interface appearance."
                );

        themeDescription.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        themeDescription.setForeground(TEXT_SECONDARY);
        themeDescription.setAlignmentX(LEFT_ALIGNMENT);


        JPanel themePanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                18,
                                0
                        )
                );

        themePanel.setOpaque(false);
        themePanel.setAlignmentX(LEFT_ALIGNMENT);


        lightThemeButton =
                new JRadioButton("Light");

        darkThemeButton =
                new JRadioButton("Dark");
        


        lightThemeButton.setOpaque(false);
        darkThemeButton.setOpaque(false);

        lightThemeButton.setFocusPainted(false);
        darkThemeButton.setFocusPainted(false);
        
        Color themeOptionText =
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY;

        lightThemeButton.setForeground(themeOptionText);
        darkThemeButton.setForeground(themeOptionText);
        
        styleThemeOption(lightThemeButton);
        styleThemeOption(darkThemeButton);


        ButtonGroup themeGroup =
                new ButtonGroup();

        themeGroup.add(lightThemeButton);
        themeGroup.add(darkThemeButton);

        lightThemeButton.addActionListener(
                e -> refreshThemeOptionStyles()
        );

        darkThemeButton.addActionListener(
                e -> refreshThemeOptionStyles()
        );


        if (AppPreferences.THEME_DARK.equals(
                AppPreferences.getTheme()
        )) {

            darkThemeButton.setSelected(true);

        } else {

            lightThemeButton.setSelected(true);
        }


        themePanel.add(lightThemeButton);
        themePanel.add(darkThemeButton);
        refreshThemeOptionStyles();


        card.add(themeLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(themeDescription);
        card.add(Box.createVerticalStrut(9));
        card.add(themePanel);
        card.add(Box.createVerticalStrut(24));


        // ==========================================
        // FONT SIZE
        // ==========================================

        JLabel fontLabel =
                new JLabel(
                        "Interface Font Size"
                );

        fontLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        fontLabel.setForeground(TEXT_PRIMARY);
        fontLabel.setAlignmentX(LEFT_ALIGNMENT);


        JLabel fontDescription =
                new JLabel(
                        "Adjust text size throughout the application."
                );

        fontDescription.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        fontDescription.setForeground(TEXT_SECONDARY);
        fontDescription.setAlignmentX(LEFT_ALIGNMENT);


        JPanel fontPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        fontPanel.setOpaque(false);
        fontPanel.setAlignmentX(LEFT_ALIGNMENT);


        JButton decreaseButton =
                createSmallButton("−");

        JButton increaseButton =
                createSmallButton("+");


        fontSizeValue =
                new JLabel();

        fontSizeValue.setHorizontalAlignment(
                JLabel.CENTER
        );

        fontSizeValue.setPreferredSize(
                new Dimension(
                        75,
                        34
                )
        );

        fontSizeValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        updateFontSizeLabel();


        decreaseButton.addActionListener(e -> {

            if (selectedFontSize
                    > AppPreferences.MIN_FONT_SIZE) {

                selectedFontSize--;
                updateFontSizeLabel();
            }
        });


        increaseButton.addActionListener(e -> {

            if (selectedFontSize
                    < AppPreferences.MAX_FONT_SIZE) {

                selectedFontSize++;
                updateFontSizeLabel();
            }
        });


        fontPanel.add(decreaseButton);
        fontPanel.add(fontSizeValue);
        fontPanel.add(increaseButton);


        card.add(fontLabel);
        card.add(Box.createVerticalStrut(3));
        card.add(fontDescription);
        card.add(Box.createVerticalStrut(9));
        card.add(fontPanel);

        //card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(24));

        // ==========================================
        // ACTIONS
        // ==========================================

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        actions.setOpaque(false);
        actions.setAlignmentX(LEFT_ALIGNMENT);


        JButton resetButton =
                createSecondaryButton(
                        "Reset"
                );

        JButton applyButton =
                createPrimaryButton(
                        "Apply Changes"
                );


        resetButton.addActionListener(
                e -> resetAppearance()
        );

        applyButton.addActionListener(
                e -> applyAppearance()
        );


        actions.add(resetButton);
        actions.add(applyButton);

        card.add(actions);

        return card;
    }

    private JPanel createSettingsCard(
            String title,
            String description
    ) {
        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(SURFACE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(
                                22, 26, 22, 26
                        )
                )
        );

        card.setAlignmentX(LEFT_ALIGNMENT);

        card.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 300)
        );

        JLabel heading = new JLabel(title);
        heading.setFont(
                new Font("SansSerif", Font.BOLD, 17)
        );
        heading.setForeground(TEXT_PRIMARY);
        heading.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(description);
        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));

        return card;
    }


    private JPanel createStoreInformationCard() {

        JPanel card = createSettingsCard(
                "Store Information",
                "Details displayed throughout your retail system."
        );

        JLabel storeLabel =
                new JLabel("Store Name");

        storeLabel.setAlignmentX(LEFT_ALIGNMENT);

        storeNameField = new JTextField(
                AppPreferences.getStoreName()
        );

        storeNameField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );

        storeNameField.setAlignmentX(LEFT_ALIGNMENT);

        JLabel currencyLabel =
                new JLabel("Currency");

        currencyLabel.setAlignmentX(LEFT_ALIGNMENT);

        JTextField currencyField =
                new JTextField("LKR (Rs.)");

        currencyField.setEditable(false);

        currencyField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 38)
        );

        currencyField.setAlignmentX(LEFT_ALIGNMENT);

        JButton saveButton =
                createPrimaryButton("Save Store Information");

        saveButton.addActionListener(e -> {

            String storeName =
                    storeNameField.getText().trim();

            if (storeName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a store name.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            AppPreferences.setStoreName(storeName);

            JOptionPane.showMessageDialog(
                    this,
                    "Store information saved successfully.",
                    "Settings Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        JPanel actions =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT, 0, 0
                ));

        actions.setOpaque(false);
        actions.setAlignmentX(LEFT_ALIGNMENT);
        actions.add(saveButton);

        card.add(storeLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(storeNameField);
        card.add(Box.createVerticalStrut(16));

        card.add(currencyLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(currencyField);
        card.add(Box.createVerticalStrut(18));
        card.add(actions);

        return card;
    }
    
    
    
    
    
    
    
    
    
    private JPanel createDatabaseConnectionCard() {

        JPanel card = createSettingsCard(
                "Database Connection",
                "Configure the MySQL database used by SuperMart."
        );

        JTextField hostField = new JTextField("localhost");
        JTextField portField = new JTextField("3306");
        JTextField databaseField = new JTextField("supermart_db");
        JTextField usernameField = new JTextField("root");
        JPasswordField passwordField = new JPasswordField();

        // Load previously saved configuration.
        try {
            Properties config = DBConnection.getConfiguration();

            String url = config.getProperty("db.url", "");
            String prefix = "jdbc:mysql://";

            if (url.startsWith(prefix)) {
                String address = url.substring(prefix.length());
                int slash = address.indexOf('/');

                if (slash >= 0) {
                    String server = address.substring(0, slash);
                    String database = address.substring(slash + 1);
                    int query = database.indexOf('?');

                    if (query >= 0) {
                        database = database.substring(0, query);
                    }

                    int colon = server.lastIndexOf(':');

                    if (colon >= 0) {
                        hostField.setText(server.substring(0, colon));
                        portField.setText(server.substring(colon + 1));
                    } else {
                        hostField.setText(server);
                    }

                    databaseField.setText(database);
                }
            }

            usernameField.setText(
                    config.getProperty("db.user", "root")
            );

            passwordField.setText(
                    config.getProperty("db.password", "")
            );

        } catch (SQLException e) {
            // Default values remain visible.
        }

        JPanel fields = new JPanel(new GridLayout(0, 1, 0, 6));
        fields.setOpaque(false);
        fields.setAlignmentX(LEFT_ALIGNMENT);

        fields.add(new JLabel("Host"));
        fields.add(hostField);

        fields.add(new JLabel("Port"));
        fields.add(portField);

        fields.add(new JLabel("Database"));
        fields.add(databaseField);

        fields.add(new JLabel("Username"));
        fields.add(usernameField);

        fields.add(new JLabel("Password"));
        fields.add(passwordField);

        // Allow the form to follow the card width.
        fields.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 310)
        );

        card.add(fields);
        card.add(Box.createVerticalStrut(18));

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        actions.setOpaque(false);
        actions.setAlignmentX(LEFT_ALIGNMENT);

        JButton testButton =
                createSecondaryButton("Test Connection");

        JButton saveButton =
                createPrimaryButton("Save Connection");

        actions.add(testButton);
        actions.add(saveButton);

        card.add(actions);

        // Reuse one validation/test flow for both buttons.
        java.awt.event.ActionListener runConnectionAction = event -> {

            String host = hostField.getText().trim();
            String port = portField.getText().trim();
            String database = databaseField.getText().trim();
            String username = usernameField.getText().trim();
            char[] passwordChars = passwordField.getPassword();

            if (host.isEmpty() || database.isEmpty()
                    || username.isEmpty()) {

                java.util.Arrays.fill(passwordChars, '\0');

                JOptionPane.showMessageDialog(
                        this,
                        "Host, database, and username are required.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int portNumber;

            try {
                portNumber = Integer.parseInt(port);

                if (portNumber < 1 || portNumber > 65535) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException ex) {
                java.util.Arrays.fill(passwordChars, '\0');

                JOptionPane.showMessageDialog(
                        this,
                        "Port must be a number between 1 and 65535.",
                        "Invalid Port",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String url = "jdbc:mysql://" + host
                    + ":" + portNumber + "/" + database;

            boolean saveRequested =
                    event.getSource() == saveButton;

            testButton.setEnabled(false);
            saveButton.setEnabled(false);

            new SwingWorker<Void, Void>() {

                private Exception failure;

                @Override
                protected Void doInBackground() {

                    String password = new String(passwordChars);

                    try (Connection connection =
                            DBConnection.testConnection(
                                    url, username, password
                            )) {

                        if (saveRequested) {
                            DBConnection.saveConfiguration(
                                    url, username, password
                            );
                        }

                    } catch (Exception ex) {
                        failure = ex;
                    } finally {
                        java.util.Arrays.fill(passwordChars, '\0');
                    }

                    return null;
                }

                @Override
                protected void done() {

                    testButton.setEnabled(true);
                    saveButton.setEnabled(true);

                    if (failure != null) {
                        JOptionPane.showMessageDialog(
                                SettingsPanel.this,
                                "Connection failed:\n"
                                        + failure.getMessage(),
                                "Database Connection",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    JOptionPane.showMessageDialog(
                            SettingsPanel.this,
                            saveRequested
                                    ? "Database connection tested and saved successfully."
                                    : "Database connection successful!",
                            "Database Connection",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            }.execute();
        };

        testButton.addActionListener(runConnectionAction);
        saveButton.addActionListener(runConnectionAction);

        return card;
    }


    private JPanel createSystemInformationCard() {

        JPanel card = createSettingsCard(
                "System Information",
                "Application and technology details."
        );

        card.add(createInformationRow(
                "Application", "SuperMart"
        ));

        card.add(Box.createVerticalStrut(12));

        card.add(createInformationRow(
                "Version", "1.0"
        ));

        card.add(Box.createVerticalStrut(12));

        card.add(createInformationRow(
                "Database", "MySQL"
        ));

        return card;
    }
    
    /*

    private JPanel createInformationRow(
            String label,
            String value
    ) {

        JPanel row = new JPanel(
                new BorderLayout()
        );

        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setForeground(TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT_PRIMARY);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);

        row.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 28)
        );

        return row;
    } */
    
    
    private JPanel createInformationRow(
            String label,
            String value
    ) {

        JPanel row = new JPanel(
                new java.awt.GridLayout(1, 2, 12, 0)
        );

        row.setOpaque(false);
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel nameLabel = new JLabel(label);
        nameLabel.setForeground(TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(JLabel.RIGHT);

        row.add(nameLabel);
        row.add(valueLabel);

        row.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 28)
        );

        return row;
    }
    

    private void styleThemeOption(JRadioButton button) {

        boolean dark =
                AppearanceManager.isDarkMode();

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setForeground(
                dark
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY
        );

        button.setBackground(
                dark
                        ? AppearanceManager.DARK_INPUT
                        : Color.WHITE
        );

        button.setOpaque(true);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                dark
                                        ? AppearanceManager.DARK_BORDER
                                        : BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                7,
                                11,
                                7,
                                11
                        )
                )
        );

        button.setFocusPainted(false);
    }
    
    
    private void refreshThemeOptionStyles() {

        styleThemeOption(lightThemeButton);
        styleThemeOption(darkThemeButton);

        Color selectedBackground =
                AppearanceManager.isDarkMode()
                        ? AppearanceManager.DARK_RAISED
                        : new Color(239, 240, 243);

        if (lightThemeButton.isSelected()) {
            lightThemeButton.setBackground(
                    selectedBackground
            );
        }

        if (darkThemeButton.isSelected()) {
            darkThemeButton.setBackground(
                    selectedBackground
            );
        }

        lightThemeButton.repaint();
        darkThemeButton.repaint();
    }

    private JButton createSmallButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setPreferredSize(
                new Dimension(
                        42,
                        34
                )
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        button.setFocusPainted(false);

        return button;
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
                        12
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(
                new Color(
                        25,
                        25,
                        28
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
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
                        12
                )
        );

        button.setForeground(TEXT_PRIMARY);
        button.setBackground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                9,
                                17,
                                9,
                                17
                        )
                )
        );

        return button;
    }


    private void updateFontSizeLabel() {

        fontSizeValue.setText(
                selectedFontSize + " px"
        );
    }
    
    private void applyAppearance() {

        String theme =
                darkThemeButton.isSelected()
                        ? AppPreferences.THEME_DARK
                        : AppPreferences.THEME_LIGHT;
        
        refreshThemeOptionStyles();
        AppPreferences.setTheme(theme);

        AppPreferences.setFontSize(
                selectedFontSize
        );

        JOptionPane.showMessageDialog(
                this,
                "Appearance settings saved.\n\n"
                        + "Restart SuperMart to apply the selected theme.",
                "Settings Saved",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void resetAppearance() {

        AppPreferences.resetAppearance();

        selectedFontSize =
                AppPreferences.DEFAULT_FONT_SIZE;

        lightThemeButton.setSelected(true);
        refreshThemeOptionStyles();

        updateFontSizeLabel();

        JOptionPane.showMessageDialog(
                this,
                "Appearance settings reset to the SuperMart defaults.\n\n"
                        + "Restart SuperMart to apply the changes.",
                "Settings Reset",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}