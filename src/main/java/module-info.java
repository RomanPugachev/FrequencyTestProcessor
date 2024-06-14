module org.example.frequencytestsprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.frequencytestsprocessor to javafx.fxml;
    exports org.example.frequencytestsprocessor;
}