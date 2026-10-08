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


    public SettingsPanel() {

        selectedFontSize =
                AppPreferences.getFontSize();

        setLayout(new BorderLayout());

        setBackground(BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        32,
                        36,
                        32,
                        36
                )
        );

        add(
                createContent(),
                BorderLayout.NORTH
        );
    }


    private JPanel createContent() {

        JPanel content = new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );


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

        content.add(appearanceCard);

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
        );


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

        card.add(Box.createVerticalGlue());


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