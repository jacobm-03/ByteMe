package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class HomeController {

    @FXML private Label navHome, navCourses, navProgress, navProfile;

    @FXML private StackPane contentArea; // 🔄 The central content container in HomeView.fxml

    @FXML
    private void initialize() {
        // Load default content (e.g., Home)
        try {
            loadContent("/com/byteme/bytemeapplication/fxml/HomeContent.fxml"); // You can extract the quote/button section into this optional sub-FXML
        } catch (IOException e) {
            System.out.println("❌ Failed to load default Home content");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNavClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        String labelText = clickedLabel.getText();
        System.out.println("Nav clicked: " + labelText);

        String fxmlFile = null;

        switch (labelText) {
            case "Home":
                fxmlFile = "/com/byteme/bytemeapplication/fxml/HomeContent.fxml";
                break;
            case "Courses":
                fxmlFile = "/com/byteme/bytemeapplication/fxml/CourseView.fxml";
                break;
            case "Progress":
                fxmlFile = "/com/byteme/bytemeapplication/fxml/ProgressView.fxml";
                break;
            case "Profile":
                fxmlFile = "/com/byteme/bytemeapplication/fxml/ProfileView.fxml";
                break;
            default:
                System.out.println("❌ Unknown nav item: " + labelText);
                return;
        }

        try {
            loadContent(fxmlFile);
        } catch (IOException e) {
            System.out.println("❌ Error loading content for " + labelText);
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        System.out.println("Resolved URL: " + url);

        if (url == null) {
            throw new IOException("FXML file not found at path: " + fxmlPath);
        }

        Parent content = FXMLLoader.load(url);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(content);
    }
}
