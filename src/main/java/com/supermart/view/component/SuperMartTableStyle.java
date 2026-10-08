/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view.component;

/**
 *
 * @author pabodini
 */

import com.supermart.util.AppearanceManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;


public final class SuperMartTableStyle {

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color GRID_COLOR =
            new Color(240, 241, 243);

    private static final Color HEADER_BACKGROUND =
            new Color(250, 250, 251);

    private static final Color SELECTION_BACKGROUND =
            new Color(239, 240, 243);


    private SuperMartTableStyle() {
        // Utility class
    }


    // =========================================================
    // APPLY STANDARD SUPERMART TABLE STYLE
    // =========================================================

    public static void apply(JTable table) {
        
        // ---------- General table ----------
        table.setRowHeight(42);

        table.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        boolean dark =
                AppearanceManager.isDarkMode();

        Color textPrimary =
                dark
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_PRIMARY;

        Color gridColor =
                dark
                        ? AppearanceManager.DARK_DIVIDER
                        : GRID_COLOR;

        Color tableBackground =
                dark
                        ? AppearanceManager.DARK_SURFACE
                        : Color.WHITE;

        Color selectionBackground =
                dark
                        ? AppearanceManager.DARK_SELECTED
                        : SELECTION_BACKGROUND;

        table.setForeground(textPrimary);
        table.setBackground(tableBackground);

        table.setGridColor(gridColor);

        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setSelectionBackground(selectionBackground);
        table.setSelectionForeground(textPrimary);

        table.setFillsViewportHeight(true);

        // ---------- Header ----------
        JTableHeader tableHeader =
                table.getTableHeader();

        tableHeader.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );
        
        tableHeader.setForeground(
                dark
                        ? AppearanceManager.DARK_TEXT
                        : TEXT_SECONDARY
        );

        tableHeader.setBackground(
                dark
                        ? AppearanceManager.DARK_RAISED
                        : HEADER_BACKGROUND
        );
        
        tableHeader.setPreferredSize(
                new Dimension(
                        tableHeader
                                .getPreferredSize()
                                .width,
                        42
                )
        );


        // Header uses the same
        // centered-block + left-text idea
        tableHeader.setDefaultRenderer(
                createHeaderRenderer(
                        tableHeader
                )
        );


        // ---------- Row cells ----------
        DefaultTableCellRenderer
                centeredLeftRenderer =
                createCenteredLeftRenderer();


        // Apply it to every column
        for (int column = 0;
                column < table.getColumnCount();
                column++) {

            table.getColumnModel()
                    .getColumn(column)
                    .setCellRenderer(
                            centeredLeftRenderer
                    );
        }
    }


    // =========================================================
    // HEADER RENDERER
    // =========================================================

    private static DefaultTableCellRenderer
            createHeaderRenderer(
                    JTableHeader tableHeader
            ) {

        return new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {

                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );


                setHorizontalAlignment(
                        SwingConstants.LEFT
                );

                setVerticalAlignment(
                        SwingConstants.CENTER
                );

                setFont(
                        tableHeader.getFont()
                );

                if (AppearanceManager.isDarkMode()) {

                    setForeground(
                            AppearanceManager.DARK_TEXT
                    );

                    setBackground(
                            AppearanceManager.DARK_RAISED
                    );

                } else {

                    setForeground(
                            TEXT_SECONDARY
                    );

                    setBackground(
                            HEADER_BACKGROUND
                    );
                }
                
                setOpaque(true);


                int columnWidth =
                        table.getColumnModel()
                                .getColumn(column)
                                .getWidth();

                int padding =
                        Math.max(
                                8,
                                columnWidth / 4
                        );


                setBorder(
                        BorderFactory.createEmptyBorder(
                                0,
                                padding,
                                0,
                                4
                        )
                );


                return this;
            }
        };
    }


    // =========================================================
    // ROW CELL RENDERER
    // =========================================================

    private static DefaultTableCellRenderer
            createCenteredLeftRenderer() {

        return new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {

                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );


                // Text itself remains left aligned
                setHorizontalAlignment(
                        SwingConstants.LEFT
                );


                // Move the text block toward
                // the center of its column
                int columnWidth =
                        table.getColumnModel()
                                .getColumn(column)
                                .getWidth();

                int padding =
                        Math.max(
                                8,
                                columnWidth / 4
                        );


                setBorder(
                        BorderFactory.createEmptyBorder(
                                0,
                                padding,
                                0,
                                4
                        )
                );


                return this;
            }
        };
    }
}
