package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSubjectController {

    @FXML private TextField subjectField;
    @FXML private StackPane subjectTile;
    @FXML private Label tileLabel;
    @FXML private Circle colorRed, colorOrange, colorBlue, colorTeal, colorGreen, colorPurple;

    private String selectedColor = "#1976D2"; // Default color

    @FXML
    private void updateTilePreview() {
        tileLabel.setText(subjectField.getText());
    }

    @FXML
    private void handleColorSelect(MouseEvent event) {
        Circle clicked = (Circle) event.getSource();
        String colorHex = toHex(clicked.getFill().toString()); // Convert fill to hex
        selectedColor = colorHex;

        subjectTile.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 25px;");
    }

    @FXML
    private void handleCreateSubject() {
        String subjectName = subjectField.getText().trim();
        String colorHex = selectedColor;
        int userId = Session.getCurrentUser().getId();

        if (subjectName.isEmpty() || colorHex == null) {
            showAlert("Please enter a subject and pick a color.");
            return;
        }

        String sql = "INSERT INTO subjects (user_id, name, color) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, subjectName);
            stmt.setString(3, colorHex);

            stmt.executeUpdate();
            showAlert("✅ Subject saved successfully!");

            // Optional: navigate back to CourseView after saving
            handleBack();

        } catch (SQLException e) {
            showAlert("❌ Failed to save subject: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        try {
            HomeController.getInstance().loadContent("/com/byteme/bytemeapplication/fxml/CourseView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Convert JavaFX color (0xff123456) to CSS-compatible #123456
    private String toHex(String fxColor) {
        if (fxColor.startsWith("0x")) {
            return "#" + fxColor.substring(2, 8); // extract RRGGBB only
        }
        try {
            return Paint.valueOf(fxColor).toString(); // fallback
        } catch (Exception e) {
            return "#1976D2"; // default color
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
