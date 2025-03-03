package org.example.frequencytestsprocessor.datamodel.datasources.UFFDatasets;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;

import static org.example.frequencytestsprocessor.controllers.MainController.objectMapper;

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