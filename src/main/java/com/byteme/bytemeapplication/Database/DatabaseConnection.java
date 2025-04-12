package com.byteme.bytemeapplication.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {
    private static Connection instance;

    private static final String URL = "jdbc:sqlite:data/UserData.db";


    private DatabaseConnection() {
        try {
            instance = DriverManager.getConnection(URL);
            System.out.println("✅ Connected to SQLite at: " + new java.io.File(URL.replace("jdbc:sqlite:", "")).getAbsolutePath());

            // ✅ Create table when app connects to DB
            createUsersTable();

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
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            firstname TEXT NOT NULL,
            lastname TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL
        );
    """;

        try (Statement stmt = instance.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("✅ users table created or already exists.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to create users table: " + e.getMessage());
        }
    }


}