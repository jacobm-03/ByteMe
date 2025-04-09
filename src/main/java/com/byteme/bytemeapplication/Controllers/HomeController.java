package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.net.URL;
import java.io.IOException;

public class HomeController {

    @FXML
    private void handleNavClick(MouseEvent event) throws IOException {
        Label clickedLabel = (Label) event.getSource();
        String labelText = clickedLabel.getText();
        System.out.println("Nav clicked: " + labelText);

        String fxmlFile = null;

        switch (labelText) {
            case "Home":
                fxmlFile = "/com/byteme/bytemeapplication/fxml/HomeView.fxml";
                break;
            case "Courses":
                fxmlFile = "/com/byteme/bytemeapplication/fxml/CourseView.fxml"; // ✅ Fix this
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

        URL url = getClass().getClassLoader().getResource(fxmlFile.substring(1));
        System.out.println("Resolved URL: " + url); // ✅ Check this

        if (url == null) {
            System.out.println("❌ Could not load FXML file: " + fxmlFile);
            return;
        }

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
