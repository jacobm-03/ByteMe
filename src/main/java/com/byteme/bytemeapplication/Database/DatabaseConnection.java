package com.byteme.bytemeapplication.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection instance;
    private static final String DB_PATH = "data/UserData.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    private DatabaseConnection() {
        try {
            instance = DriverManager.getConnection(URL);
            System.out.println("✅ Connected to SQLite at: " + new java.io.File(DB_PATH).getAbsolutePath());

            createUsersTable();
            createSubjectsTable();
            createUserScoresTable(); // Track quiz scores over time

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    public static Connection getInstance() {
        try {
            if (instance == null || instance.isClosed()) {
                new DatabaseConnection();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking DB connection: " + e.getMessage());
        }
        return instance;
    }

    private void createUsersTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                firstname TEXT NOT NULL,
                lastname TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            );
        """;

        executeTableCreation(sql, "users");
    }

    private void createSubjectsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS subjects (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                color TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(id)
            );
        """;

        executeTableCreation(sql, "subjects");
    }

    private void createUserScoresTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS user_scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                subject_id INTEGER NOT NULL,
                score INTEGER NOT NULL,
                total_questions INTEGER NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES users(id),
                FOREIGN KEY(subject_id) REFERENCES subjects(id)
            );
        """;

        executeTableCreation(sql, "user_scores");
    }

    private void executeTableCreation(String sql, String tableName) {
        try (Statement stmt = instance.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ " + tableName + " table created or already exists.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to create " + tableName + " table: " + e.getMessage());
        }
    }
}
