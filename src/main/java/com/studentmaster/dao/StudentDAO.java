
package com.studentmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.studentmaster.database.Database;
import com.studentmaster.model.Student;

public class StudentDAO {

    
public void addStudent(Student student) throws SQLException {
    String sql = """
        INSERT INTO students (nim, name, major, email, photo)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (Connection conn = Database.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, student.getNim());
        stmt.setString(2, student.getName());
        stmt.setString(3, student.getMajor());
        stmt.setString(4, student.getEmail());
        stmt.setString(5, student.getPhoto());

        int rows = stmt.executeUpdate();

        if (rows == 0) {
            throw new SQLException("Data mahasiswa gagal disimpan.");
        }

        System.out.println("Data berhasil disimpan: " + student.getNim());
    }
}


    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY nim";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getString("nim"),
                    rs.getString("name"),
                    rs.getString("major"),
                    rs.getString("email"),
                    rs.getString("photo")
                ));
            }
        }

        return students;
    }

    public void updateStudent(Student student) throws SQLException {
        String sql = """
            UPDATE students
            SET name = ?, major = ?, email = ?, photo = ?
            WHERE nim = ?
            """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getMajor());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhoto());
            stmt.setString(5, student.getNim());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Mahasiswa tidak ditemukan.");
            }
        }
    }

    public void deleteStudent(String nim) throws SQLException {
        String sql = "DELETE FROM students WHERE nim = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Mahasiswa tidak ditemukan.");
            }
        }
    }
}