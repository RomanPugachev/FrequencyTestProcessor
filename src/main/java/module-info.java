module org.example.frequencytestsprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;


    opens org.example.frequencytestsprocessor to javafx.fxml;
    exports org.example.frequencytestsprocessor;
    exports org.example.frequencytestsprocessor.commons;
    opens org.example.frequencytestsprocessor.commons to javafx.fxml;
}