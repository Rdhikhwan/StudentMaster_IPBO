
package com.studentmaster.report;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

public class ReportDialog {

    public static void show(JFrame parent) {

        JTextField nimAwalField = new JTextField();
        JTextField nimAkhirField = new JTextField();

        JPanel fields = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        fields.setBorder(new EmptyBorder(15, 15, 15, 15));
        fields.add(new JLabel("NIM Awal:"));
        fields.add(nimAwalField);
        fields.add(new JLabel("NIM Akhir:"));
        fields.add(nimAkhirField);

        int result = JOptionPane.showConfirmDialog(
                parent,
                fields,
                "Cetak Laporan Mahasiswa",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String nimAwal = nimAwalField.getText().trim();
        String nimAkhir = nimAkhirField.getText().trim();

        if (nimAwal.isBlank() || nimAkhir.isBlank()) {
            JOptionPane.showMessageDialog(
                    parent,
                    "NIM awal dan NIM akhir wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (nimAwal.compareTo(nimAkhir) > 0) {
            JOptionPane.showMessageDialog(
                    parent,
                    "NIM awal tidak boleh lebih besar dari NIM akhir.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan PDF");
        chooser.setSelectedFile(
                new File("Laporan_Mahasiswa.pdf")
        );
        chooser.setFileFilter(
                new FileNameExtensionFilter("File PDF", "pdf")
        );

        if (chooser.showSaveDialog(parent)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File output = chooser.getSelectedFile();
        String outputPath = output.getAbsolutePath();

        if (!outputPath.toLowerCase().endsWith(".pdf")) {
            outputPath += ".pdf";
        }

        if (new File(outputPath).exists()) {
            int overwrite = JOptionPane.showConfirmDialog(
                    parent,
                    "File sudah ada. Ganti file tersebut?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (overwrite != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            ReportService.exportStudentReport(
                    nimAwal,
                    nimAkhir,
                    outputPath
            );

            JOptionPane.showMessageDialog(
                    parent,
                    "Laporan PDF berhasil dibuat:\n" + outputPath,
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    parent,
                    "Gagal membuat laporan:\n" + ex.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
