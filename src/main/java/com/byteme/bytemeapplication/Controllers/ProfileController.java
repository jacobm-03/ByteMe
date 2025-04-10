package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import java.io.IOException;


public class ProfileController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label yearLabel;

    @FXML
    private Button editProfileBtn;
    @FXML
    private Button progressBtn;


    @FXML
    private void initialize() {
        // Simulated user data — replace with actual logic later
        String fullName = "Student Name";
        String email = "StudentName@email.com";
        String year = "Year 12";

        nameLabel.setText(fullName);
        emailLabel.setText(email);
        yearLabel.setText(year);

        editProfileBtn.setOnAction(e -> System.out.println("Edit Profile clicked!"));
        progressBtn.setOnAction(e -> System.out.println("Progress clicked!"));
    }
}
