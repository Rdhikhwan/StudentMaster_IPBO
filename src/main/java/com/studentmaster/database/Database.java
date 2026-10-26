
package com.studentmaster.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:studentmaster.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                nim TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                major TEXT NOT NULL,
                email TEXT,
                photo TEXT
            )
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database berhasil disiapkan!");
        } catch (SQLException e) {
            System.err.println("Gagal menyiapkan database: " + e.getMessage());
        }
    }
}
