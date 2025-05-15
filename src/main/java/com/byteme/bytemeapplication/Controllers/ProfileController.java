package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Models.User;
import com.byteme.bytemeapplication.Helpers.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProfileController {

    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label yearLabel;

    @FXML private Button editProfileButton;
    @FXML private Button progressButton;
    @FXML private Button navigateToCoursesBtn;

    @FXML private StackPane editProfileModalOverlay;
    @FXML private TextField userTitleField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private FlowPane yearSelector;

    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;

    private ToggleGroup yearToggleGroup = new ToggleGroup();

    @FXML
    private void initialize() {
        User user = Session.getCurrentUser();

        if (user != null) {
            fullNameLabel.setText((Session.getTitle() != null ? Session.getTitle() + " " : "") + user.getFullName());
            emailLabel.setText(user.getEmail());
            yearLabel.setText(Session.getYear() != null ? Session.getYear() : "");
        } else {
            fullNameLabel.setText("Student Name");
            emailLabel.setText("StudentName@email.com");
            yearLabel.setText("Year 12");
        }

        editProfileButton.setOnAction(e -> handleEditProfileClick());
        progressButton.setOnAction(e -> handleProgressClick());

        for (Node node : yearSelector.getChildren()) {
            if (node instanceof ToggleButton button) {
                button.setToggleGroup(yearToggleGroup);
            }
        }
    }

    @FXML
    private void handleEditProfileClick() {
        System.out.println("Edit Profile clicked!");

        // Show the modal
        editProfileModalOverlay.setVisible(true);
        editProfileModalOverlay.setManaged(true);

        // Auto-fill fields with session user data
        User user = Session.getCurrentUser();
        if (user != null) {
            firstNameField.setText(user.getFirstName());
            lastNameField.setText(user.getLastName());
        }
    }

    @FXML
    private void handleSaveChanges() {
        editProfileModalOverlay.setVisible(false);
        editProfileModalOverlay.setManaged(false);

        String userTitle = userTitleField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String fullName = (userTitle + " " + firstName + " " + lastName).trim();

        fullNameLabel.setText(fullName);

        ToggleButton selected = (ToggleButton) yearToggleGroup.getSelectedToggle();
        if (selected != null) {
            yearLabel.setText(selected.getText());
            Session.setYear(selected.getText());
        }

        try (Connection conn = DatabaseConnection.getInstance()) {
            int userId = Session.getCurrentUser().getId();

            String sql = "UPDATE users SET firstname = ?, lastname = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, userId);
            stmt.executeUpdate();

            // Update session info as well
            User updatedUser = new User(userId, firstName, lastName, Session.getCurrentUser().getEmail());
            Session.setCurrentUser(updatedUser);

            showAlert("✅ Profile updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("❌ Error updating profile: " + e.getMessage());
        }
    }


    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            try (Connection conn = DatabaseConnection.getInstance()) {
                int userId = Session.getCurrentUser().getId();
                String sql = "DELETE FROM users WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.executeUpdate();

                Session.clear();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/byteme/bytemeapplication/fxml/LoginView.fxml"));
                Scene loginScene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(loginScene);
                stage.setTitle("Login - ByteMe");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Failed to delete account: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGoToCourses(ActionEvent event) {
        try {
            Node courseView = FXMLLoader.load(getClass().getResource("/com/byteme/bytemeapplication/fxml/CourseView.fxml"));
            Node contentArea = navigateToCoursesBtn.getScene().lookup("#contentArea");
            if (contentArea instanceof Pane pane) {
                pane.getChildren().clear();
                pane.getChildren().add(courseView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

       //method to handle progress button click
    @FXML
    private void handleProgressClick() {
        try {
            // Load the ProgressController view
            Node progressView = FXMLLoader.load(getClass().getResource("/com/byteme/bytemeapplication/fxml/ProgressView.fxml"));
            Node contentArea = progressButton.getScene().lookup("#contentArea");  // Assuming there is a contentArea in your layout

            if (contentArea instanceof Pane pane) {
                pane.getChildren().clear();  // Clear current content
                pane.getChildren().add(progressView);  // Add the new view
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert("Please fill in all password fields.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("New passwords do not match.");
            return;
        }

        try (Connection conn = DatabaseConnection.getInstance()) {
            int userId = Session.getCurrentUser().getId();

            // Check if old password is correct
            String checkSql = "SELECT password FROM users WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            var rs = checkStmt.executeQuery();

            if (rs.next()) {
                String actualOldPassword = rs.getString("password");
                if (!actualOldPassword.equals(currentPassword)) {
                    showAlert("Old password is incorrect.");
                    return;
                }
            }

            // Update password
            String updateSql = "UPDATE users SET password = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newPassword);
            updateStmt.setInt(2, userId);
            updateStmt.executeUpdate();

            showAlert("✅ Password changed successfully!");

            // Clear fields after success
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmNewPasswordField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("❌ Error: " + e.getMessage());
        }
    }

}
