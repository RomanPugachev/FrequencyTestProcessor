module org.example.frequencytestsprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires static lombok;


    opens org.example.frequencytestsprocessor to javafx.fxml;
    exports org.example.frequencytestsprocessor;
    exports org.example.frequencytestsprocessor.commons;
    opens org.example.frequencytestsprocessor.commons to javafx.fxml;
}