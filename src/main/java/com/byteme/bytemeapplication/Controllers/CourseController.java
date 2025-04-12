package com.byteme.bytemeapplication.Controllers;

import com.byteme.bytemeapplication.Database.DatabaseConnection;
import com.byteme.bytemeapplication.Helpers.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    @FXML
    private void initialize() {
        loadUserSubjects();
    }

    private void loadUserSubjects() {
        subjectFlow.getChildren().clear();

        // Handle case if user is not logged in
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
                String name = rs.getString("name");
                String color = rs.getString("color");

                StackPane tile = createSubjectTile(name, color);
                subjectFlow.getChildren().add(tile);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Toggle empty state visibility
        emptyStateBox.setVisible(!hasSubjects);
        emptyStateBox.setManaged(!hasSubjects);

        subjectFlow.getChildren().add(createAddTile());
    }

    private StackPane createSubjectTile(String name, String colorHex) {
        StackPane tile = new StackPane();
        tile.setPrefSize(180, 100);
        tile.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 20px;");

        Label label = new Label(name);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        tile.getChildren().add(label);

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

    private void showEmptyState() {
        emptyStateBox.setVisible(true);
        emptyStateBox.setManaged(true);
    }
}
