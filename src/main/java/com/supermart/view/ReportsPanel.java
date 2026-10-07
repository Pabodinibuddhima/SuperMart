/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.view;

/**
 *
 * @author pabodini
 */
import com.supermart.report.SalesReportService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class ReportsPanel extends JPanel {

    private static final Color BACKGROUND =
            new Color(247, 248, 250);

    private static final Color CARD_BACKGROUND =
            Color.WHITE;

    private static final Color TEXT_PRIMARY =
            new Color(25, 25, 28);

    private static final Color TEXT_SECONDARY =
            new Color(110, 113, 120);

    private static final Color BORDER =
            new Color(230, 232, 235);

    private static final Color PRIMARY =
            new Color(25, 25, 28);

    private final SalesReportService reportService;

    private JButton viewReportButton;
    private JButton exportPdfButton;

    public ReportsPanel() {

        reportService =
                new SalesReportService();

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


        // ==========================================
        // PAGE HEADER
        // ==========================================

        JLabel title =
                new JLabel("Reports");

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
                        "Business reports and performance insights"
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

        content.add(
                Box.createVerticalStrut(5)
        );

        content.add(subtitle);

        content.add(
                Box.createVerticalStrut(28)
        );


        // ==========================================
        // REPORT CARD
        // ==========================================

        JPanel reportCard =
                createSalesPerformanceCard();

        reportCard.setAlignmentX(
                LEFT_ALIGNMENT
        );

        content.add(reportCard);


        return content;
    }


    private JPanel createSalesPerformanceCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                24,
                                0
                        )
                );

        card.setBackground(CARD_BACKGROUND);

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
                        150
                )
        );

        card.setPreferredSize(
                new Dimension(
                        850,
                        150
                )
        );


        // ==========================================
        // LEFT SIDE
        // ==========================================

        JPanel information =
                new JPanel();

        information.setOpaque(false);

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel reportTitle =
                new JLabel(
                        "Sales Performance Report"
                );

        reportTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        reportTitle.setForeground(
                TEXT_PRIMARY
        );


        JLabel description =
                new JLabel(
                        "Sales, refunds, costs and profitability overview"
                );

        description.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        description.setForeground(
                TEXT_SECONDARY
        );


        JLabel details =
                new JLabel(
                        "Includes multi-table sales data, "
                        + "historical cost, net sales and gross profit."
                );

        details.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        details.setForeground(
                TEXT_SECONDARY
        );


        information.add(reportTitle);

        information.add(
                Box.createVerticalStrut(8)
        );

        information.add(description);

        information.add(
                Box.createVerticalStrut(5)
        );

        information.add(details);


        // ==========================================
        // RIGHT SIDE BUTTONS
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


        exportPdfButton =
                createSecondaryButton(
                        "Export PDF"
                );

        viewReportButton =
                createPrimaryButton(
                        "View Report"
                );


        exportPdfButton.addActionListener(
                e -> exportPdf()
        );

        viewReportButton.addActionListener(
                e -> viewReport()
        );


        actions.add(exportPdfButton);
        actions.add(viewReportButton);


        card.add(
                information,
                BorderLayout.CENTER
        );

        card.add(
                actions,
                BorderLayout.EAST
        );


        return card;
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
        button.setBackground(PRIMARY);

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

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

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

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


    // ==========================================
    // VIEW REPORT
    // ==========================================

    private void viewReport() {

        setButtonsEnabled(false);

        SwingWorker<JasperPrint, Void> worker =
                new SwingWorker<>() {

            @Override
            protected JasperPrint doInBackground()
                    throws Exception {

                return reportService
                        .generateSalesPerformanceReport();
            }


            @Override
            protected void done() {

                try {

                    JasperPrint print =
                            get();

                    JasperViewer viewer =
                            new JasperViewer(
                                    print,
                                    false
                            );

                    viewer.setTitle(
                            "SuperMart — "
                            + "Sales Performance Report"
                    );

                    viewer.setVisible(true);

                } catch (Exception ex) {

                    showReportError(ex);

                } finally {

                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }


    // ==========================================
    // EXPORT PDF
    // ==========================================

    private void exportPdf() {

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Export Sales Performance Report"
        );

        chooser.setFileFilter(
                new FileNameExtensionFilter(
                        "PDF Documents (*.pdf)",
                        "pdf"
                )
        );

        chooser.setSelectedFile(
                new File(
                        "SuperMart-Sales-Performance.pdf"
                )
        );


        int result =
                chooser.showSaveDialog(
                        SwingUtilities.getWindowAncestor(
                                this
                        )
                );


        if (result
                != JFileChooser.APPROVE_OPTION) {

            return;
        }


        File selectedFile =
                chooser.getSelectedFile();


        if (!selectedFile
                .getName()
                .toLowerCase()
                .endsWith(".pdf")) {

            selectedFile =
                    new File(
                            selectedFile
                                    .getAbsolutePath()
                                    + ".pdf"
                    );
        }


        if (selectedFile.exists()) {

            int overwrite =
                    JOptionPane.showConfirmDialog(
                            this,
                            "The selected file already exists.\n"
                            + "Do you want to replace it?",
                            "Replace File",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (overwrite
                    != JOptionPane.YES_OPTION) {

                return;
            }
        }


        final File exportFile =
                selectedFile;


        setButtonsEnabled(false);


        SwingWorker<Void, Void> worker =
                new SwingWorker<>() {

            @Override
            protected Void doInBackground()
                    throws Exception {

                JasperPrint print =
                        reportService
                                .generateSalesPerformanceReport();

                JasperExportManager
                        .exportReportToPdfFile(
                                print,
                                exportFile
                                        .getAbsolutePath()
                        );

                return null;
            }


            @Override
            protected void done() {

                try {

                    get();

                    JOptionPane.showMessageDialog(
                            ReportsPanel.this,
                            "Report exported successfully.\n\n"
                            + exportFile
                                    .getAbsolutePath(),
                            "Export Complete",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (Exception ex) {

                    showReportError(ex);

                } finally {

                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }


    private void setButtonsEnabled(
            boolean enabled
    ) {

        viewReportButton.setEnabled(enabled);
        exportPdfButton.setEnabled(enabled);
    }


    private void showReportError(
            Exception exception
    ) {

        Throwable cause =
                exception.getCause() != null
                        ? exception.getCause()
                        : exception;

        JOptionPane.showMessageDialog(
                this,
                "Unable to generate the report.\n\n"
                + cause.getMessage(),
                "Report Error",
                JOptionPane.ERROR_MESSAGE
        );

        cause.printStackTrace();
    }
}
