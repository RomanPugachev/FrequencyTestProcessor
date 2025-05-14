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
@Table(name = "uff164")
@DiscriminatorValue(value = "164")
@Getter
@Setter
public class UFF164 extends UFFDataset{
        @JsonProperty("units_code")
        public int unitsCode;
        @JsonProperty("units_description")
        public String unitsDescription;
        @JsonProperty("temp_mode")
        public int tempMode;
        public double length;
        public double force;
        public double temp;
        @JsonProperty("temp_offset")
        public double tempOffset;

        @SneakyThrows
        @Override
        public String toString(){
                return objectMapper.writeValueAsString(this);
        }
    }