module org.example.frequencytestsprocessor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires jep;
    requires org.apache.commons.lang3;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires jakarta.transaction;
    requires org.reflections;
    requires commons.math3;
    requires JTransforms;

    opens org.example.frequencytestsprocessor to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs to org.hibernate.orm.core, com.fasterxml.jackson.databind; // javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets to javafx.fxml, com.fasterxml.jackson.databind, org.hibernate.orm.core;
    opens org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents to org.hibernate.orm.core;
    opens org.example.frequencytestsprocessor.datamodel.myMath to com.fasterxml.jackson.databind;
    opens org.example.frequencytestsprocessor.commons to javafx.fxml;
    opens org.example.frequencytestsprocessor.datamodel.UFF58Repr to com.fasterxml.jackson.databind, javafx.fxml, javafx.base;
    opens org.example.frequencytestsprocessor.datamodel.formula to javafx.base, javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.frequencytestsprocessor.controllers to javafx.fxml;
    opens org.example.frequencytestsprocessor.datamodel.databaseModel.datasources to com.fasterxml.jackson.databind, javafx.fxml, org.hibernate.orm.core;
    opens org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets to com.fasterxml.jackson.databind, javafx.fxml, org.hibernate.orm.core;
    exports org.example.frequencytestsprocessor.controllers;
    exports org.example.frequencytestsprocessor;
    exports org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;
    exports org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents to com.fasterxml.jackson.databind;
    exports org.example.frequencytestsprocessor.commons;
    exports org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets;
    exports org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;
    exports org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities;
    exports org.example.frequencytestsprocessor.converters;
    exports org.example.frequencytestsprocessor.helpers;

}