/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.util;

/**
 *
 * @author pabodini
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public final class AppearanceManager {
    
    private static boolean windowListenerInstalled = false;

    public static final Color LIGHT_BACKGROUND =
            new Color(247, 248, 250);

    public static final Color LIGHT_SURFACE =
            Color.WHITE;

    public static final Color LIGHT_TEXT =
            new Color(25, 25, 28);

    public static final Color LIGHT_SECONDARY =
            new Color(110, 113, 120);

    public static final Color LIGHT_BORDER =
            new Color(225, 228, 232);

    
    // =========================================================
    // DARK SUPERMART PALETTE
    // =========================================================

    public static final Color DARK_BACKGROUND =
            new Color(30, 32, 36);       // #1E2024

    public static final Color DARK_SURFACE =
            new Color(40, 43, 48);       // #282B30

    public static final Color DARK_INPUT =
            new Color(36, 39, 44);       // #24272C

    public static final Color DARK_RAISED =
            new Color(52, 56, 63);       // #34383F

    public static final Color DARK_SELECTED =
            new Color(61, 66, 74);       // #3D424A

    public static final Color DARK_TEXT =
            new Color(238, 239, 241);    // #EEEFF1

    public static final Color DARK_SECONDARY =
            new Color(190, 194, 201);    // #BEC2C9
    
    public static final Color DARK_BORDER =
            new Color(66, 70, 77);       // #42464D
    
    public static final Color DARK_DIVIDER =
            new Color(59, 63, 69);       // #3B3F45
    
    
    private AppearanceManager() {
    }


    public static boolean isDarkMode() {

        return AppPreferences.THEME_DARK.equals(
                AppPreferences.getTheme()
        );
    }

    public static void applySavedAppearance() {
        
        
        applySwingDefaults();
        installDarkUIDefaults();

        for (Window window : Window.getWindows()) {

            /*
             * Reinstall Swing's UI delegates first.
             * Our custom appearance must be applied AFTER this.
             */
            SwingUtilities.updateComponentTreeUI(window);

            /*
             * Font scaling applies to both themes.
             */
            applyFontTree(window);

            /*
             * IMPORTANT:
             * Light mode is the ORIGINAL SuperMart design.
             *
             * We do not generically recolor the application
             * when Light is selected.
             */
            if (isDarkMode()) {
                applyDarkTheme(window);
            }

            window.revalidate();
            window.repaint();
        }
    }
    
    public static void applyToComponentTree(
            Component component
    ) {

        if (component == null) {
            return;
        }

        applyFont(component);

        if (isDarkMode()) {
            applyDarkThemeComponent(component);
        }

        if (component instanceof Container container) {

            for (Component child : container.getComponents()) {
                applyToComponentTree(child);
            }
        }
    }

    private static void applyFontTree(
            Component component
    ) {

        if (component == null) {
            return;
        }

        applyFont(component);

        if (component instanceof Container container) {

            for (Component child : container.getComponents()) {
                applyFontTree(child);
            }
        }
    }


    private static void applyDarkTheme(
            Component component
    ) {

        if (component == null) {
            return;
        }

        applyDarkThemeComponent(component);

        if (component instanceof Container container) {

            for (Component child : container.getComponents()) {
                applyDarkTheme(child);
            }
        }
    }

    private static void applyFont(
            Component component
    ) {

        Font current =
                component.getFont();

        if (current == null) {
            return;
        }

        int baseSize =
                AppPreferences.getFontSize();

        float targetSize =
                calculateTargetFontSize(
                        component,
                        current,
                        baseSize
                );

        component.setFont(
                current.deriveFont(
                        targetSize
                )
        );
    }


    private static float calculateTargetFontSize(
            Component component,
            Font current,
            int baseSize
    ) {

        if (component instanceof JTableHeader) {
            return baseSize - 2f;
        }

        if (component instanceof JTable) {
            return baseSize - 2f;
        }

        if (component instanceof JButton) {
            return baseSize - 2f;
        }

        if (component instanceof JTextField
                || component instanceof JTextArea
                || component instanceof JComboBox) {

            return baseSize - 2f;
        }

        if (component instanceof JLabel) {

            if (current.isBold()
                    && current.getSize() >= 20) {

                return baseSize + 8f;
            }

            if (current.isBold()
                    && current.getSize() >= 16) {

                return baseSize + 2f;
            }

            return baseSize - 2f;
        }

        return baseSize - 2f;
    }


    private static void applyDarkThemeComponent(
            Component component
    ) {

      
        Color background =
                DARK_BACKGROUND;

        Color surface =
                DARK_SURFACE;

        Color text =
                DARK_TEXT;

        Color secondary =
                DARK_SECONDARY;

        Color border =
                DARK_BORDER;
        
        
        if (component instanceof JScrollBar scrollBar) {

            scrollBar.setBackground(DARK_SURFACE);
            scrollBar.setForeground(DARK_RAISED);

            scrollBar.setOpaque(true);

            return;
        }
        
        if (component instanceof JTable table) {

            table.setBackground(DARK_SURFACE);
            table.setForeground(DARK_TEXT);

            table.setGridColor(DARK_DIVIDER);

            table.setSelectionBackground(DARK_SELECTED);
            table.setSelectionForeground(DARK_TEXT);

            table.setShowVerticalLines(false);
            table.setShowHorizontalLines(true);

            if (table.getTableHeader() != null) {

                table.getTableHeader().setOpaque(true);
                table.getTableHeader().setBackground(DARK_RAISED);
                table.getTableHeader().setForeground(DARK_TEXT);
            }

            return;
        }

        
        if (component instanceof JComboBox<?> comboBox) {

            comboBox.setBackground(DARK_INPUT);

            comboBox.setForeground(
                    DARK_TEXT
            );

            comboBox.setOpaque(true);
            
            comboBox.setBorder(
                    BorderFactory.createLineBorder(
                            DARK_BORDER
                    )
            );

            comboBox.setUI(
                    new BasicComboBoxUI() {

                        @Override
                        protected JButton createArrowButton() {

                            JButton arrowButton =
                                    new JButton() {

                                        @Override
                                        protected void paintComponent(
                                                Graphics g
                                        ) {

                                            Graphics2D g2 =
                                                    (Graphics2D) g.create();

                                            try {

                                                g2.setRenderingHint(
                                                        RenderingHints.KEY_ANTIALIASING,
                                                        RenderingHints.VALUE_ANTIALIAS_ON
                                                );

                                                g2.setColor(
                                                        AppearanceManager.DARK_SECONDARY
                                                );

                                                int centerX =
                                                        getWidth() / 2;

                                                int centerY =
                                                        getHeight() / 2;

                                                Polygon arrow =
                                                        new Polygon();

                                                arrow.addPoint(
                                                        centerX - 4,
                                                        centerY - 2
                                                );

                                                arrow.addPoint(
                                                        centerX + 4,
                                                        centerY - 2
                                                );

                                                arrow.addPoint(
                                                        centerX,
                                                        centerY + 3
                                                );

                                                g2.fillPolygon(
                                                        arrow
                                                );

                                            } finally {

                                                g2.dispose();
                                            }
                                        }
                                    };

                            arrowButton.setPreferredSize(
                                    new Dimension(
                                            24,
                                            24
                                    )
                            );

                            arrowButton.setBackground(
                                    DARK_INPUT
                            );

                            arrowButton.setBorder(
                                    BorderFactory.createMatteBorder(
                                            0,
                                            1,
                                            0,
                                            0,
                                            DARK_BORDER
                                    )
                            );

                            arrowButton.setFocusPainted(
                                    false
                            );

                            arrowButton.setContentAreaFilled(
                                    false
                            );

                            arrowButton.setOpaque(
                                    true
                            );

                            return arrowButton;
                        }
                    }
            );

            ListCellRenderer<Object> renderer =
                    new DefaultListCellRenderer() {

                        @Override
                        public Component getListCellRendererComponent(
                                JList<?> list,
                                Object value,
                                int index,
                                boolean isSelected,
                                boolean cellHasFocus
                        ) {

                            JLabel label =
                                    (JLabel) super
                                            .getListCellRendererComponent(
                                                    list,
                                                    value,
                                                    index,
                                                    isSelected,
                                                    cellHasFocus
                                            );

                            label.setOpaque(true);

                            label.setForeground(
                                    DARK_TEXT
                            );

                            
                            label.setBackground(
                                    isSelected
                                            ? DARK_SELECTED
                                            : DARK_INPUT
                            );

                            label.setBorder(
                                    BorderFactory.createEmptyBorder(
                                            5,
                                            8,
                                            5,
                                            8
                                    )
                            );

                            return label;
                        }
                    };

            @SuppressWarnings("unchecked")
            JComboBox<Object> objectCombo =
                    (JComboBox<Object>) comboBox;

            objectCombo.setRenderer(renderer);

            return;
        }

        if (component instanceof JTextField
                || component instanceof JTextArea) {

            component.setBackground(DARK_INPUT);
            component.setForeground(DARK_TEXT);

            if (component instanceof JTextField textField) {

                // Makes the typing cursor visible in Dark mode.
                textField.setCaretColor(DARK_TEXT);

                // Visible selection without becoming bright blue/white.
                textField.setSelectionColor(DARK_SELECTED);
                textField.setSelectedTextColor(DARK_TEXT);
            }

            if (component instanceof JTextArea textArea) {

                textArea.setCaretColor(DARK_TEXT);
                textArea.setSelectionColor(DARK_SELECTED);
                textArea.setSelectedTextColor(DARK_TEXT);
            }

            if (component instanceof JComponent jComponent) {

                jComponent.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        DARK_BORDER
                                ),
                                BorderFactory.createEmptyBorder(
                                        6,
                                        8,
                                        6,
                                        8
                                )
                        )
                );
            }

            return;
        }

        if (component instanceof JScrollPane scrollPane) {

            scrollPane.setBackground(surface);

            if (scrollPane.getViewport()
                    != null) {

                scrollPane.getViewport()
                        .setBackground(surface);
            }

            return;
        }


        if (component instanceof JViewport viewport) {

            viewport.setBackground(surface);

            return;
        }


        if (component instanceof JLabel label) {

            label.setForeground(
                    text
            );

            return;
        }

        
        
        if (component instanceof JButton button) {

            button.setBackground(DARK_RAISED);
            button.setForeground(DARK_TEXT);

            /*
             * Important:
             * Prevent the installed Swing Look & Feel from repainting
             * clicked/selected buttons with its light background.
             */
            button.setOpaque(true);
            button.setContentAreaFilled(true);

            button.setFocusPainted(false);

            button.setRolloverEnabled(false);

            button.getModel().addChangeListener(e -> {

                if (!isDarkMode()) {
                    return;
                }

                if (button.getModel().isPressed()
                        || button.getModel().isArmed()
                        || button.getModel().isSelected()) {

                    button.setBackground(
                            DARK_SELECTED
                    );

                } else {

                    button.setBackground(
                            DARK_RAISED
                    );
                }

                button.setForeground(
                        DARK_TEXT
                );
            });
            
            
            
            button.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    DARK_BORDER
                            ),
                            BorderFactory.createEmptyBorder(
                                    6,
                                    12,
                                    6,
                                    12
                            )
                    )
            );

            return;
        }

        if (component instanceof JPanel panel) {

            Color original =
                    panel.getBackground();

            if (isVeryLight(original)) {

                panel.setBackground(
                        DARK_SURFACE
                );

            } else {

                panel.setBackground(
                        DARK_BACKGROUND
                );
            }

            panel.setForeground(
                    DARK_TEXT
            );

            return;
        }
    }


    private static void applySwingDefaults() {

        int fontSize =
                AppPreferences.getFontSize();

        Font defaultFont =
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        Math.max(
                                11,
                                fontSize - 2
                        )
                );

        UIManager.put(
                "Label.font",
                defaultFont
        );

        UIManager.put(
                "Button.font",
                defaultFont
        );

        UIManager.put(
                "TextField.font",
                defaultFont
        );

        UIManager.put(
                "TextArea.font",
                defaultFont
        );

        UIManager.put(
                "ComboBox.font",
                defaultFont
        );

        UIManager.put(
                "Table.font",
                defaultFont
        );

        UIManager.put(
                "TableHeader.font",
                defaultFont.deriveFont(
                        Font.BOLD
                )
        );
    }
    
    
    public static void installAutomaticWindowTheming() {

        if (windowListenerInstalled) {
            return;
        }

        windowListenerInstalled = true;

        Toolkit.getDefaultToolkit().addAWTEventListener(
                event -> {

                    if (!(event instanceof WindowEvent windowEvent)) {
                        return;
                    }

                    if (windowEvent.getID()
                            != WindowEvent.WINDOW_OPENED) {
                        return;
                    }

                    Window window =
                            windowEvent.getWindow();

                    SwingUtilities.invokeLater(() -> {

                        SwingUtilities.updateComponentTreeUI(
                                window
                        );

                        applyFontTree(window);

                        if (isDarkMode()) {
                            applyDarkTheme(window);
                        }

                        window.revalidate();
                        window.repaint();
                    });
                },
                AWTEvent.WINDOW_EVENT_MASK
        );
    }

    private static boolean isVeryLight(
            Color color
    ) {

        if (color == null) {
            return false;
        }

        return color.getRed() >= 225
                && color.getGreen() >= 225
                && color.getBlue() >= 225;
    }


    private static boolean isVeryDark(
            Color color
    ) {

        if (color == null) {
            return false;
        }

        return color.getRed() <= 60
                && color.getGreen() <= 60
                && color.getBlue() <= 60;
    }
    
    
    private static void installDarkUIDefaults() {
        
        if (!isDarkMode()) {
            return;
        }

        // =====================================================
        // SCROLLBARS
        // =====================================================

        UIManager.put(
                "ScrollBar.background",
                DARK_SURFACE
        );

        UIManager.put(
                "ScrollBar.track",
                DARK_SURFACE
        );

        UIManager.put(
                "ScrollBar.thumb",
                DARK_RAISED
        );

        UIManager.put(
                "ScrollBar.thumbDarkShadow",
                DARK_RAISED
        );

        UIManager.put(
                "ScrollBar.thumbHighlight",
                DARK_SELECTED
        );

        UIManager.put(
                "ScrollBar.thumbShadow",
                DARK_RAISED
        );

        UIManager.put(
                "ScrollBar.foreground",
                DARK_RAISED
        );

        UIManager.put(
                "ScrollPane.background",
                DARK_SURFACE
        );

        UIManager.put(
                "ScrollPane.foreground",
                DARK_TEXT
        );

        // Buttons
        UIManager.put(
                "Button.background",
                DARK_RAISED
        );

        UIManager.put(
                "Button.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "Button.select",
                DARK_SELECTED
        );

        UIManager.put(
                "Button.focus",
                DARK_BORDER
        );

        // Combo boxes
        UIManager.put(
                "ComboBox.background",
                DARK_INPUT
        );

        UIManager.put(
                "ComboBox.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "ComboBox.selectionBackground",
                DARK_SELECTED
        );

        UIManager.put(
                "ComboBox.selectionForeground",
                DARK_TEXT
        );

        // Popup lists
        UIManager.put(
                "List.background",
                DARK_SURFACE
        );

        UIManager.put(
                "List.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "List.selectionBackground",
                DARK_SELECTED
        );

        UIManager.put(
                "List.selectionForeground",
                DARK_TEXT
        );

        // Inputs
        UIManager.put(
                "TextField.background",
                DARK_INPUT
        );

        UIManager.put(
                "TextField.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "TextField.caretForeground",
                DARK_TEXT
        );

        // Tables
        UIManager.put(
                "Table.background",
                DARK_SURFACE
        );

        UIManager.put(
                "Table.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "Table.selectionBackground",
                DARK_SELECTED
        );

        UIManager.put(
                "Table.selectionForeground",
                DARK_TEXT
        );

        UIManager.put(
                "Table.gridColor",
                DARK_DIVIDER
        );

        UIManager.put(
                "TableHeader.background",
                DARK_RAISED
        );

        UIManager.put(
                "TableHeader.foreground",
                DARK_TEXT
        );

        // Scrollbars
        UIManager.put(
                "ScrollBar.background",
                DARK_BACKGROUND
        );

        UIManager.put(
                "ScrollBar.thumb",
                DARK_RAISED
        );

        UIManager.put(
                "ScrollBar.track",
                DARK_BACKGROUND
        );
        
        // =====================================================
        // JOptionPane / dialogs
        // =====================================================

        UIManager.put(
                "OptionPane.background",
                DARK_SURFACE
        );

        UIManager.put(
                "OptionPane.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "OptionPane.messageForeground",
                DARK_TEXT
        );

        UIManager.put(
                "Panel.background",
                DARK_SURFACE
        );

        UIManager.put(
                "Label.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "Button.background",
                DARK_RAISED
        );

        UIManager.put(
                "Button.foreground",
                DARK_TEXT
        );

        UIManager.put(
                "Button.select",
                DARK_SELECTED
        );

        UIManager.put(
                "Button.focus",
                DARK_BORDER
        );
        
    }
 
}