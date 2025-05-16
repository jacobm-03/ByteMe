package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import com.byteme.bytemeapplication.Helpers.Session;
import com.byteme.bytemeapplication.Models.User;

public class HomeController {

    private static HomeController instance;

    public HomeController() {
        instance = this;
    }

    public static HomeController getInstance() {
        return instance;
    }

    @FXML private Label navHome, navCourses, navProgress, navProfile;
    @FXML private StackPane contentArea;
    @FXML private Label userNameLabel;

    @FXML
    private void initialize() {
        setUserNameLabel();
        setupUserDropdown();

        try {
            loadDefaultHomeContent();
        } catch (IOException e) {
            System.out.println("❌ Failed to load default Home content");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNavClick(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        String destination = getFXMLPath(clickedLabel.getText());

        if (destination != null) {
            try {
                loadContent(destination);
            } catch (IOException e) {
                System.out.println("❌ Error loading content for " + clickedLabel.getText());
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Unknown nav item: " + clickedLabel.getText());
        }
    }



    private void setUserNameLabel() {
        User user = Session.getCurrentUser();
        userNameLabel.setText(user != null ? user.getFirstName().toLowerCase() : "Guest");
    }

    private void setupUserDropdown() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem logoutItem = new MenuItem(" Log out");
        logoutItem.setOnAction(e -> handleLogout());

        contextMenu.getItems().add(logoutItem);

        userNameLabel.setOnMouseClicked(event -> {
            if (event.isPrimaryButtonDown()) {
                contextMenu.show(userNameLabel, Side.BOTTOM, 0, 0);
            }
        });
    }

    private void loadDefaultHomeContent() throws IOException {
        loadContent("/com/byteme/bytemeapplication/fxml/HomeContent.fxml");
    }

    private String getFXMLPath(String labelText) {
        return switch (labelText) {
            case "Home" -> "/com/byteme/bytemeapplication/fxml/HomeContent.fxml";
            case "Courses" -> "/com/byteme/bytemeapplication/fxml/CourseView.fxml";
            case "Progress" -> "/com/byteme/bytemeapplication/fxml/ProgressView.fxml";
            case "Profile" -> "/com/byteme/bytemeapplication/fxml/ProfileView.fxml";
            default -> null;
        };
    }

    private void handleLogout() {
        System.out.println("Logging out");
        Session.setCurrentUser(null);

        try {
            redirectToLoginView();
        } catch (IOException e) {
            System.out.println("❌ Failed to log out");
            e.printStackTrace();
        }
    }

    private void redirectToLoginView() throws IOException {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/byteme/bytemeapplication/fxml/LoginView.fxml"));
        Stage stage = (Stage) userNameLabel.getScene().getWindow();
        Scene loginScene = new Scene(loginRoot);
        stage.setScene(loginScene);
        stage.sizeToScene();
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
