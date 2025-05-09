module com.bytemeapplication {
    requires javafx.controls;
    requires javafx.fxml;

    // Only open/exports actual non-empty packages
    opens com.byteme.bytemeapplication.Application to javafx.fxml;
    opens com.byteme.bytemeapplication.Controllers to javafx.fxml;
    requires java.sql; //
    requires org.apache.pdfbox;
    requires org.json;
    requires java.net.http;

    exports com.byteme.bytemeapplication.Utils;



    exports com.byteme.bytemeapplication.Application;
    exports com.byteme.bytemeapplication.Controllers;
    exports com.byteme.bytemeapplication.Models;

    // Allow reflection (used by JUnit) for testing
    opens com.byteme.bytemeapplication.Models to org.junit.jupiter;
}