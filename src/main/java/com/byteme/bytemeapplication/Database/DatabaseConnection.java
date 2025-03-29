package com.byteme.bytemeapplication.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static Connection instance = null;

    private DatabaseConnection() {
        String url = "jdbc:sqlite:bytme.db"; // this will create the DB in your project root
        try {
            instance = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite database.");
        } catch (SQLException sqlEx) {
            System.err.println("Connection failed: " + sqlEx.getMessage());
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }
}