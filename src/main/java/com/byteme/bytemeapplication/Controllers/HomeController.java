package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import com.byteme.bytemeapplication.Helpers.Session;
import com.byteme.bytemeapplication.Models.User;

import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;

public class HomeController {

    private static HomeController instance; // 🔥 Allows global access

    public HomeController() {
        instance = this;
    }

    public static HomeController getInstance() {
        return instance;
    }

    @FXML private Label navHome, navCourses, navProgress, navProfile;
    @FXML private StackPane contentArea; // The central container for dynamic swapping

    @FXML private Label userNameLabel;

    @FXML
    private void initialize() {
        try {
            User user = Session.getCurrentUser();
            if (user != null) {
                userNameLabel.setText(user.getFirstName().toLowerCase()); // or use full name
            } else {
                userNameLabel.setText("Guest");
            }

            userDrop(); // sets up dropdown when clicking on user

            loadContent("/com/byteme/bytemeapplication/fxml/HomeContent.fxml");
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

    public void userDrop(){ //creates the user dropdown
        ContextMenu contextMenu = new ContextMenu();
        MenuItem logoutItem = new MenuItem(" Log out");
        logoutItem.setOnAction(e -> hLogout());

        contextMenu.getItems().add(logoutItem);

        userNameLabel.setOnMouseClicked(event -> {
            if (event.getButton().name().equals("PRIMARY")) {
                contextMenu.show(userNameLabel, Side.BOTTOM, 0, 0); //Sets dropdown below username

            }
        });
    }

    private void hLogout() {
        System.out.println("Logging out");
        Session.setCurrentUser(null); // Logs out user

        try {
            // Redirects user to login page
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/byteme/bytemeapplication/fxml/LoginView.fxml"));
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            Scene loginScene = new Scene(loginRoot);

            stage.setScene(loginScene);
            stage.sizeToScene(); //resets the page size

        } catch (IOException e) {
            System.out.println("❌ Failed to log out");
            e.printStackTrace();
        }
    }


    public void loadContent(String fxmlPath) throws IOException {
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
