package com.studentmaster.report;

import java.awt.Image;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ReportService {

    private static final String DATABASE_URL =
            "jdbc:sqlite:studentmaster.db";

    private static final String REPORT_TEMPLATE =
            "/StudentReport.jrxml";

    public static void exportStudentReport(
            String nimAwal,
            String nimAkhir,
            String outputPath
    ) throws Exception {

        // Membaca template laporan
        InputStream template =
                ReportService.class.getResourceAsStream(
                        REPORT_TEMPLATE
                );

        if (template == null) {
            throw new IllegalStateException(
                    "File StudentReport.jrxml tidak ditemukan."
            );
        }

        try (InputStream reportTemplate = template) {

            // Mengompilasi file JRXML
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(
                            reportTemplate
                    );

            // Menyiapkan parameter laporan
            Map<String, Object> parameters =
                    new HashMap<>();

            parameters.put("NIM_AWAL", nimAwal);
            parameters.put("NIM_AKHIR", nimAkhir);

            // Logo untuk header laporan (kosong jika file tidak ada)
            Image logo = null;
            try (InputStream logoStream =
                    ReportService.class.getResourceAsStream(
                            "/images/logo.png"
                    )) {
                if (logoStream != null) {
                    logo = ImageIO.read(logoStream);
                }
            }
            parameters.put("LOGO", logo);

            // Membuka koneksi database SQLite
            try (Connection connection =
                    DriverManager.getConnection(
                            DATABASE_URL
                    )) {

                // Mengambil data dan mengisi laporan
                JasperPrint jasperPrint =
                        JasperFillManager.fillReport(
                                jasperReport,
                                parameters,
                                connection
                        );

                // Mengekspor laporan menjadi PDF
                JasperExportManager.exportReportToPdfFile(
                        jasperPrint,
                        outputPath
                );
            }
        }
    }
}