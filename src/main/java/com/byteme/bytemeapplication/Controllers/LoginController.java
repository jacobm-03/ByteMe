package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class LoginController {

    @FXML private HBox container;
    @FXML private HBox loginPane, signupPane;

    @FXML private VBox signupForm; // ← ✅ Add this!
    @FXML private TextField firstNameField, lastNameField, signupEmailField;
    @FXML private PasswordField signupPasswordField, confirmPasswordField;


    @FXML
    private void handleLogin() {
        System.out.println("Login with credentials.");
    }



    @FXML
    private void showSignUp() {
        animatePanes(0.3125, 0.6875);
        fadeSignupForm(true);
    }

    @FXML
    private void showLogin() {
        animatePanes(0.6875, 0.3125);
        fadeSignupForm(false);
    }

    private void animatePanes(double loginPercent, double signupPercent) {
        double totalWidth = container.getPrefWidth();

        KeyValue loginWidth = new KeyValue(loginPane.prefWidthProperty(), totalWidth * loginPercent);
        KeyValue signupWidth = new KeyValue(signupPane.prefWidthProperty(), totalWidth * signupPercent);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400), loginWidth, signupWidth)
        );
        timeline.play();
    }

    private void fadeSignupForm(boolean show) {
        FadeTransition fade = new FadeTransition(Duration.millis(400), signupForm);
        fade.setFromValue(show ? 0 : 1);
        fade.setToValue(show ? 1 : 0);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        fade.setOnFinished(e -> signupForm.setVisible(show));
        if (show) signupForm.setVisible(true);
        fade.play();
    }

    @FXML
    private boolean handleSignUp() {
        String id = generateSixDigitId();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = signupEmailField.getText().trim();
        String password = signupPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("All fields must be filled.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("❌ Passwords do not match.");
            return false;
        }

        try (Connection conn = DatabaseConnection.getInstance()) {
            String sql = "INSERT INTO users (id, firstname, lastname, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);

            stmt.executeUpdate();
            showAlert("✅ Account created successfully!");
            clearFields();
            return true;

        } catch (SQLException e) {
            showAlert("❌ Database error: " + e.getMessage());
            return false;
        }
    }


    @FXML
    private void handleSignUpAndSwitch() {
        // Check if form is currently visible
        if (!signupForm.isVisible()) {
            showSignUp();  // Animate & reveal form
        } else {
            if (handleSignUp()) {
                showLogin(); // Only transition if sign-up successful
            }
        }
    }

    private String generateSixDigitId() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        signupEmailField.clear();
        signupPasswordField.clear();
        confirmPasswordField.clear();
    }


}
