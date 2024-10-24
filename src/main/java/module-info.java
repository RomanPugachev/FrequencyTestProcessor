module org.example.frequencytestsprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires jep;
    requires org.apache.commons.lang3;

    opens org.example.frequencytestsprocessor to javafx.fxml;
    exports org.example.frequencytestsprocessor;
    exports org.example.frequencytestsprocessor.commons;
    exports org.example.frequencytestsprocessor.datamodel.UFFDatasets;
    opens org.example.frequencytestsprocessor.datamodel.UFFDatasets to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.frequencytestsprocessor.datamodel.myMath to com.fasterxml.jackson.databind;
    opens org.example.frequencytestsprocessor.commons to javafx.fxml;
    opens org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr to com.fasterxml.jackson.databind, javafx.fxml;
}