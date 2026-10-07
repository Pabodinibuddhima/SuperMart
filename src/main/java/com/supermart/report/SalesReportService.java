/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.report;

/**
 *
 * @author pabodini
 */

import com.supermart.util.DBConnection;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class SalesReportService {

    public JasperPrint generateSalesPerformanceReport()
            throws Exception {

        InputStream reportStream =
                getClass().getResourceAsStream(
                        "/reports/sales_performance.jrxml"
                );

        if (reportStream == null) {
            throw new IllegalStateException(
                    "Sales performance report template "
                    + "could not be found."
            );
        }

        try (reportStream;
             Connection connection =
                     DBConnection.getConnection()) {

            Map<String, Object> parameters =
                    loadFinancialSummary(connection);

            JasperReport report =
                    JasperCompileManager.compileReport(
                            reportStream
                    );

            return JasperFillManager.fillReport(
                    report,
                    parameters,
                    connection
            );
        }
    }


    private Map<String, Object> loadFinancialSummary(
            Connection connection
    ) throws Exception {

        Map<String, Object> parameters =
                new HashMap<>();

        BigDecimal grossSales = BigDecimal.ZERO;
        BigDecimal discounts = BigDecimal.ZERO;
        BigDecimal refunds = BigDecimal.ZERO;
        BigDecimal grossCogs = BigDecimal.ZERO;
        BigDecimal returnedCogs = BigDecimal.ZERO;


        String salesSql = """
                SELECT
                    COALESCE(SUM(subtotal), 0)
                        AS gross_sales,

                    COALESCE(SUM(discount), 0)
                        AS discounts

                FROM sales
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(salesSql);

             ResultSet result =
                     statement.executeQuery()) {

            if (result.next()) {
                grossSales =
                        result.getBigDecimal(
                                "gross_sales"
                        );

                discounts =
                        result.getBigDecimal(
                                "discounts"
                        );
            }
        }


        String refundSql = """
                SELECT
                    COALESCE(
                        SUM(refund_amount),
                        0
                    ) AS refunds
                FROM returns
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(refundSql);

             ResultSet result =
                     statement.executeQuery()) {

            if (result.next()) {
                refunds =
                        result.getBigDecimal(
                                "refunds"
                        );
            }
        }


        String cogsSql = """
                SELECT
                    COALESCE(
                        SUM(
                            unit_cost
                            * quantity
                        ),
                        0
                    ) AS gross_cogs
                FROM sale_items
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(cogsSql);

             ResultSet result =
                     statement.executeQuery()) {

            if (result.next()) {
                grossCogs =
                        result.getBigDecimal(
                                "gross_cogs"
                        );
            }
        }


        String returnedCogsSql = """
                SELECT
                    COALESCE(
                        SUM(
                            si.unit_cost
                            * ri.quantity
                        ),
                        0
                    ) AS returned_cogs

                FROM return_items ri

                JOIN sale_items si
                    ON ri.sale_item_id =
                       si.sale_item_id
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             returnedCogsSql
                     );

             ResultSet result =
                     statement.executeQuery()) {

            if (result.next()) {
                returnedCogs =
                        result.getBigDecimal(
                                "returned_cogs"
                        );
            }
        }


        BigDecimal netSales =
                grossSales
                        .subtract(discounts)
                        .subtract(refunds);

        BigDecimal netCogs =
                grossCogs.subtract(returnedCogs);

        BigDecimal grossProfit =
                netSales.subtract(netCogs);


        parameters.put(
                "GROSS_SALES",
                grossSales
        );

        parameters.put(
                "DISCOUNTS",
                discounts
        );

        parameters.put(
                "REFUNDS",
                refunds
        );

        parameters.put(
                "NET_SALES",
                netSales
        );

        parameters.put(
                "GROSS_COGS",
                grossCogs
        );

        parameters.put(
                "RETURNED_COGS",
                returnedCogs
        );

        parameters.put(
                "NET_COGS",
                netCogs
        );

        parameters.put(
                "GROSS_PROFIT",
                grossProfit
        );

        return parameters;
    }
}
