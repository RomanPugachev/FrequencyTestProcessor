package org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import static org.example.frequencytestsprocessor.controllers.MainController.objectMapper;

@Entity
@Table(name = "uff151")
@DiscriminatorValue(value = "151")
@Getter
@Setter
public class UFF151 extends UFFDataset {
    @JsonProperty("model_name")
    private String modelName;
    private String description;
    @JsonProperty("db_app")
    private String dbApp;
    @JsonProperty("date_db_created")
    private String dateDbCreated;
    @JsonProperty("time_db_created")
    private String timeDbCreated;
    @JsonProperty("version_db1")
    private int versionDb1;
    @JsonProperty("version_db2")
    private int versionDb2;
    @JsonProperty("file_type")
    private int fileType;
    @JsonProperty("date_db_saved")
    private String dateDbSaved;
    @JsonProperty("time_db_saved")
    private String timeDbSaved;
    private String program;
    @JsonProperty("date_file_written")
    private String dateFileWritten;
    @JsonProperty("time_file_written")
    private String timeFileWritten;
    @SneakyThrows
    @Override
    public String toString(){
            return objectMapper.writeValueAsString(this);
    }
}