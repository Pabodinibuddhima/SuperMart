/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view.component;

/**
 *
 * @author pabodini
 */
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class ResponsiveFlowPanel extends JPanel {

    private final int horizontalGap;
    private final int verticalGap;
    private final int rowHeight;

    public ResponsiveFlowPanel() {

        this(10, 5, 32);
    }

    public ResponsiveFlowPanel(
            int horizontalGap,
            int verticalGap,
            int rowHeight
    ) {

        super(
                new FlowLayout(
                        FlowLayout.LEFT,
                        horizontalGap,
                        verticalGap
                )
        );

        this.horizontalGap = horizontalGap;
        this.verticalGap = verticalGap;
        this.rowHeight = rowHeight;

        setOpaque(false);

        addComponentListener(
                new ComponentAdapter() {

            @Override
            public void componentResized(
                    ComponentEvent e
            ) {

                updateResponsiveHeight();
            }
        });
        
        
    }
    private void updateResponsiveHeight() {

        Insets insets = getInsets();

        int availableWidth =
                getWidth()
                - insets.left
                - insets.right;

        if (availableWidth <= 0) {
            return;
        }


        int rows = 1;

        /*
         * FlowLayout also keeps its own horizontal
         * left/right gaps around the row.
         */
        int usableWidth =
                availableWidth
                - (horizontalGap * 2);

        int currentRowWidth = 0;


        for (Component component : getComponents()) {

            if (!component.isVisible()) {
                continue;
            }

            int componentWidth =
                    component
                            .getPreferredSize()
                            .width;


            if (currentRowWidth == 0) {

                currentRowWidth =
                        componentWidth;

            } else if (
                    currentRowWidth
                    + horizontalGap
                    + componentWidth
                    <= usableWidth
            ) {

                currentRowWidth +=
                        horizontalGap
                        + componentWidth;

            } else {

                rows++;

                currentRowWidth =
                        componentWidth;
            }
        }


        /*
         * FlowLayout has a vertical gap above
         * and below its components as well.
         */
        int requiredHeight =
                insets.top
                + insets.bottom
                + (verticalGap * 2)
                + (rows * rowHeight)
                + ((rows - 1) * verticalGap);


        Dimension preferredSize =
                getPreferredSize();

        if (preferredSize.height
                != requiredHeight) {

            setPreferredSize(
                    new Dimension(
                            preferredSize.width,
                            requiredHeight
                    )
            );

            revalidate();
            repaint();
        }
    }
    
    @Override
    public void doLayout() {

        super.doLayout();

        updateResponsiveHeight();
    }
}