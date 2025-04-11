package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label yearLabel;

    @FXML private Button editProfileBtn;
    @FXML private Button progressBtn;
    @FXML private Button goToCoursesBtn;

    @FXML private StackPane modalOverlay;
    @FXML private TextField titleField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private FlowPane yearSelector;

    private ToggleGroup yearToggleGroup = new ToggleGroup();

    @FXML
    private void initialize() {
        // Simulated user data — replace with actual logic later
        String fullName = "Student Name";
        String email = "StudentName@email.com";
        String year = "Year 12";

        nameLabel.setText(fullName);
        emailLabel.setText(email);
        yearLabel.setText(year);

        editProfileBtn.setOnAction(e -> handleEditProfileClick());
        progressBtn.setOnAction(e -> System.out.println("Progress clicked!"));

        // Group year toggle buttons
        for (Node node : yearSelector.getChildren()) {
            if (node instanceof ToggleButton button) {
                button.setToggleGroup(yearToggleGroup);
            }
        }
    }

    @FXML
    private void handleEditProfileClick() {
        System.out.println("Edit Profile clicked!");
        modalOverlay.setVisible(true);
        modalOverlay.setManaged(true);
    }

    @FXML
    private void handleSaveChanges() {
        modalOverlay.setVisible(false);
        modalOverlay.setManaged(false);

        String title = titleField.getText();
        String first = firstNameField.getText();
        String last = lastNameField.getText();

        nameLabel.setText((title + " " + first + " " + last).trim());

        ToggleButton selected = (ToggleButton) yearToggleGroup.getSelectedToggle();
        if (selected != null) {
            yearLabel.setText(selected.getText());
        }
    }

    @FXML
    private void handleGoToCourses(ActionEvent event) {
        try {
            Node courseView = FXMLLoader.load(getClass().getResource("/com/byteme/bytemeapplication/fxml/CourseView.fxml"));
            Node contentArea = goToCoursesBtn.getScene().lookup("#contentArea");
            if (contentArea instanceof Pane pane) {
                pane.getChildren().clear();
                pane.getChildren().add(courseView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
