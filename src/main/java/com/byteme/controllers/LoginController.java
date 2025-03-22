package com.byteme.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label loginStatus;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("1234")) {
            loginStatus.setText("✅ Login successful!");
        } else {
            loginStatus.setText("❌ Invalid username or password.");
        }
    }

    @FXML
    private void handleSignUp() {
        loginStatus.setText("Sign up feature coming soon...");
    }
}
