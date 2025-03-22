module com.byteme {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.byteme.App to javafx.fxml;
    exports com.byteme.App;

    opens com.byteme.controllers to javafx.fxml;
    exports com.byteme.controllers;
}
