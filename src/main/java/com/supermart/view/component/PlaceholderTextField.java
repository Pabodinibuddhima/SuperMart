/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view.component;

/**
 *
 * @author pabodini
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PlaceholderTextField extends JTextField {

    private String placeholder;

    public PlaceholderTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        // First let the normal JTextField paint itself.
        super.paintComponent(g);

        // Only draw the placeholder when the field is empty
        // and does not currently have focus.
        if (placeholder == null
                || placeholder.isEmpty()
                || !getText().isEmpty()) {
            return;
        }
        
        Graphics2D copy =
                (Graphics2D) g.create();

        try {

            copy.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            copy.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            copy.setFont(getFont());
        
            copy.setColor(new Color(150, 153, 160));

            Insets insets = getInsets();

            int x = insets.left + 2;

            int y = (getHeight()
                    - copy.getFontMetrics().getHeight()) / 2
                    + copy.getFontMetrics().getAscent();

            copy.drawString(placeholder, x, y);

        } finally {
            copy.dispose();
        }
    }
}
