module org.example.laborator6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.laborator6 to javafx.fxml;
    exports org.example.laborator6;
}