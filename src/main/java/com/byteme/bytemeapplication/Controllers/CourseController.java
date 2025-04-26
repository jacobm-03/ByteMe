package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import com.byteme.bytemeapplication.Utils.QuizDataHolder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseController {

    @FXML private FlowPane subjectFlow;
    @FXML private VBox emptyStateBox;
    @FXML private javafx.scene.control.Button delStateBtn;
    private boolean delState = false;

    @FXML
    private void initialize() {
        loadUserSubjects();
    }

    private void loadUserSubjects() {
        subjectFlow.getChildren().clear();

        if (Session.getCurrentUser() == null) {
            showEmptyState();
            subjectFlow.getChildren().add(createAddTile());
            return;
        }

        int userId = Session.getCurrentUser().getId();
        String sql = "SELECT * FROM subjects WHERE user_id = ?";
        boolean hasSubjects = false;

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hasSubjects = true;
                int subjectId = rs.getInt("id");
                String name = rs.getString("name");
                String color = rs.getString("color");

                StackPane tile = createSubjectTile(subjectId, name, color);
                subjectFlow.getChildren().add(tile);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        emptyStateBox.setVisible(!hasSubjects);
        emptyStateBox.setManaged(!hasSubjects);
        subjectFlow.getChildren().add(createAddTile());
    }

    @FXML
    private void toggleDelState() {
        delState = !delState;
        if (delState) { // Pressing the button makes it darker to show the user it's in the delete state
            delStateBtn.setStyle("-fx-background-color: #a799ff; -fx-text-fill: white;" +
                    "-fx-font-weight: bold; -fx-padding: 6 16; -fx-background-radius: 10;");
        } else {
            delStateBtn.setStyle("-fx-background-color: #bca8ff; -fx-text-fill: white;" +
                    "-fx-font-weight: bold; -fx-padding: 6 16; -fx-background-radius: 10;");
        }
        loadUserSubjects();
    }

    private StackPane createSubjectTile(int subjectId, String name, String colorHex) {
        StackPane tile = new StackPane();
        tile.setPrefSize(180, 100);
        if (delState) { // Makes subject box grey to show user it can be deleted
            tile.setStyle("-fx-background-color: #BEBEBE; -fx-background-radius: 20px;");
        } else {
            tile.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 20px;");
        }
        Label label = new Label(name);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        tile.getChildren().add(label);

        tile.setOnMouseClicked(e -> {
            if (delState) {
                // In delete state
                delSub(subjectId);
                loadUserSubjects();
            } else {
                // In normal state
                try {
                    QuizDataHolder.setSubjectId(subjectId);

                    Scene scene = tile.getScene();
                    StackPane contentArea = (StackPane) scene.lookup("#contentArea");

                    if (contentArea != null) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/byteme/bytemeapplication/fxml/SubjectDetailView.fxml"));
                        Parent detailView = loader.load();
                        contentArea.getChildren().setAll(detailView);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return tile;
    }

    private StackPane createAddTile() {
        StackPane tile = new StackPane();
        tile.setPrefSize(180, 100);
        tile.setStyle("-fx-background-color: #bca8ff; -fx-background-radius: 20px;");
        Label plus = new Label("+");
        plus.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        tile.getChildren().add(plus);

        tile.setOnMouseClicked(e -> {
            try {
                Scene scene = tile.getScene();
                StackPane contentArea = (StackPane) scene.lookup("#contentArea");

                if (contentArea != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/byteme/bytemeapplication/fxml/AddSubjectView.fxml"));
                    Parent addSubjectContent = loader.load();
                    contentArea.getChildren().setAll(addSubjectContent);
                } else {
                    System.err.println("❌ contentArea not found.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return tile;
    }

    private void delSub(int subjectId) {
        String sql = "DELETE FROM subjects WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectId);
            stmt.executeUpdate(); // Deletes subject in database
            showAlert("✅ Subject deleted successfully!");


        } catch (SQLException e) {
            showAlert("❌ Failed to delete subject: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showEmptyState() {
        emptyStateBox.setVisible(true);
        emptyStateBox.setManaged(true);
    }
}
