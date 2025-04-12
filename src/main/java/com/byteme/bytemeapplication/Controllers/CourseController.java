package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

import java.io.IOException;

public class CourseController {

    @FXML
    private Button addSubjectBtn; // ✅ Make sure this ID matches the fx:id in CourseView.fxml

    @FXML
    private void handleAddSubject() {
        try {
            // Load the AddSubjectView.fxml layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/byteme/bytemeapplication/fxml/AddSubjectView.fxml"));
            Parent addSubjectContent = loader.load();

            // Get the contentArea from the scene to swap the content dynamically
            Scene currentScene = addSubjectBtn.getScene();
            StackPane contentArea = (StackPane) currentScene.lookup("#contentArea");

            if (contentArea != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(addSubjectContent);
                System.out.println("✅ Subject creation interface loaded into contentArea.");
            } else {
                System.err.println("❌ contentArea not found in current scene.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
