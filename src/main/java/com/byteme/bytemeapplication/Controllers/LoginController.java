package com.byteme.bytemeapplication.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class LoginController {

    @FXML private HBox container;
    @FXML private HBox loginPane, signupPane;

    @FXML
    private void handleLogin() {
        System.out.println("Login with credentials.");
    }

    @FXML
    private void handleSignup() {
        System.out.println("Signup with credentials.");
    }

    @FXML
    private void showSignUp() {
        // Animate from login to signup
        animatePanes(0.25, 0.75);
    }

    @FXML
    private void showLogin() {
        // Animate from signup to login
        animatePanes(0.75, 0.25);
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
}
