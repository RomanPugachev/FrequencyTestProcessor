package org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
        public String modelName;
        public String description;
        @JsonProperty("db_app")
        public String dbApp;
        @JsonProperty("date_db_created")
        public String dateDbCreated;
        @JsonProperty("time_db_created")
        public String timeDbCreated;
        @JsonProperty("version_db1")
        public int versionDb1;
        @JsonProperty("version_db2")
        public int versionDb2;
        @JsonProperty("file_type")
        public int fileType;
        @JsonProperty("date_db_saved")
        public String dateDbSaved;
        @JsonProperty("time_db_saved")
        public String timeDbSaved;
        public String program;
        @JsonProperty("date_file_written")
        public String dateFileWritten;
        @JsonProperty("time_file_written")
        public String timeFileWritten;

        @SneakyThrows
        @Override
        public String toString(){
                return objectMapper.writeValueAsString(this);
        }
}