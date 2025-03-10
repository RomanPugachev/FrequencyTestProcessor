package org.example.frequencytestsprocessor.datamodel.UFF58Repr;

import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFF58;

import static org.example.frequencytestsprocessor.commons.CommonMethods.print;


public class UFF58Representation {
    public Section section;
    public SensorDataType sensorDataType;
    public Sensor sensorWithData;
    public Long runId;
    public UFF58Representation(UFF58 uff58) {
        String[] typeAndSensorStr = uff58.id1.split(" ");
        String sectionString = extractSectionName(uff58.id4), typeString = typeAndSensorStr[0], sensorWithDataString = typeAndSensorStr[typeAndSensorStr.length - 1];
        runId = extractRunId(uff58.id4);
        if (runId == null) {
            print("Warning: can't extract run id from this id4 dataset value --->>> " + uff58.id4);
        }
        if (typeString.equals("Harmonic")) typeString += "Spectrum";
//        sensorWithData = new Sensor(sensorWithDataString, runId,
//                List.copyOf(uff58.x),
//                uff58.data.stream().map(Complex::clone).toList());
        sensorDataType = new SensorDataType(typeString);
        section = new Section(sectionString);
    }
    private String extractSectionName(String input) {
        String regex = "Record\\s+\\d+\\s+of section \"([^\"]+)\".*";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return input; // Return original string if no match found
    }
    private Long extractRunId(String id4) {
        String runPattern = ".*,\\s*run\\s*\"(\\w+)\".*";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(runPattern);
        java.util.regex.Matcher matcher = pattern.matcher(id4);
        if (matcher.matches()) {
            String runIdStr = matcher.group(1);
            if (runIdStr.matches("\\d+")) {
                return Long.parseLong(runIdStr);
            } else if (runIdStr.matches("K_\\d+")) {
                return Long.parseLong(runIdStr.substring(2));
            }
        }
        return null;
    }
}
// UPDATE
/*
package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;

import lombok.*;
import org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;
import java.util.Set;

import static org.example.frequencytestsprocessor.commons.CommonMethods.print;


public class UFF58Representation {
    public Section section;
    public SensorDataType sensorDataType;
    public Sensor sensorWithData;
    public Long runId;
    public UFF58Representation(UFF58 uff58) {
        String[] typeAndSensorStr = uff58.id1.split(" ");
        String sectionString = extractSectionName(uff58.id4), typeString = typeAndSensorStr[0], sensorWithDataString = typeAndSensorStr[typeAndSensorStr.length - 1];
        runId = extractRunId(uff58.id4);
        print("Run id is " + runId);
        if (typeString.equals("Harmonic")) typeString += "Spectrum";
        sensorWithData = new Sensor(sensorWithDataString,
                List.copyOf(uff58.x),
                uff58.data.stream().map(Complex::clone).toList());
        sensorDataType = new SensorDataType(typeString);
        section = new Section(sectionString);
    }
    private String extractSectionName(String input) {
        String regex = "Record\\s+\\d+\\s+of section \"([^\"]+)\".*";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return input; // Return original string if no match found
    }
    private Long extractRunId(String id4) {
        String runPattern = ".*,\\s*run\\s*\"(\\w+)\".*";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(runPattern);
        java.util.regex.Matcher matcher = pattern.matcher(id4);
        if (matcher.matches()) {
            String runIdStr = matcher.group(1);
            if (runIdStr.matches("\\d+")) {
                return Long.parseLong(runIdStr);
            } else if (runIdStr.matches("K_\\d+")) {
                return Long.parseLong(runIdStr.substring(2));
            }
        }
        return null;
    }
}


 */