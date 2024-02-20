module com.example.testinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens prj to com.google.gson;
    opens com.example.testinterface to javafx.fxml;
    exports com.example.testinterface;
}