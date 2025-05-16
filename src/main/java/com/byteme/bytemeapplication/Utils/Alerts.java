package com.byteme.bytemeapplication.Utils;

import javafx.scene.control.Alert;

public class Alerts {
    public static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
