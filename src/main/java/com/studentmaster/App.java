
package com.studentmaster;

import javax.swing.SwingUtilities;

import com.studentmaster.ui.StudentForm;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentForm form = new StudentForm();
            form.setVisible(true);
        });
    }
}
