package com.byteme.bytemeapplication.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CourseController {

    @FXML
    private Button addSubjectBtn;

    @FXML
    private void handleAddSubject() {
        System.out.println("Add Subject clicked!");
        // TODO: Open subject creation popup or navigate to another view
    }
}
