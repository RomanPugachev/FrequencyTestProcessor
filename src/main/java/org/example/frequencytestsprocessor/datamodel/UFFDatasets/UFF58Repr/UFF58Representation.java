package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;

import lombok.*;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;
import java.util.Set;


public class UFF58Representation {
    public Section section;
    public SensorDataType sensorDataType;
    public Sensor sensorWithData;
    public UFF58Representation(UFF58 uff58) {
        String[] typeAndSensorStr = uff58.id1.split(" ");
        String sectionString = uff58.id4, typeString = typeAndSensorStr[0], sensorWithDataString = typeAndSensorStr[2];
        sensorWithData = new Sensor(sensorWithDataString,
                List.copyOf(uff58.x),
                uff58.data.stream().map(Complex::clone).toList());
        sensorDataType = new SensorDataType(typeString);
        section = new Section(sectionString);
    }
}
