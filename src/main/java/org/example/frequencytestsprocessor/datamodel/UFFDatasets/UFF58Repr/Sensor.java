package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;
import lombok.*;
import org.example.frequencytestsprocessor.datamodel.controlTheory.FRF;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"parentType", "data"})
public class Sensor {
    @Getter
    private String sensorName;
    @Getter
    @Setter
    SensorDataType parentType;
    @Getter // run id -> data
    private Map<Long, SensorData> data = new HashMap<>();
    public Sensor(String sensorName, Long currentRun, List<Double> frequencies, List<Complex> complexValues) {
        this.sensorName = sensorName;
        this.data.put(currentRun, new SensorData(frequencies, complexValues));
    }

    public Sensor addRun(Long currentRun, List<Double> frequencies, List<Complex> complexValues) {
        if (currentRun == null) {
            throw new RuntimeException("currentRun can't be null");
        } if (frequencies == null || frequencies.isEmpty()) {
            throw new RuntimeException("frequencies can't be null or empty");
        } if (complexValues == null || complexValues.isEmpty()) {
            throw new RuntimeException("complexValues can't be null or empty");
        } if (data.containsKey(currentRun)) {
            throw new RuntimeException("currentRun already exists");
        }
        this.data.put(currentRun, new SensorData(frequencies, complexValues));
        return this;
    }
    public void mergeSensorData(Sensor secondSensorWithData) {
        Map<Long, SensorData> secondData = secondSensorWithData.data;
        if (this.equals(secondSensorWithData)) {
            secondData.keySet().forEach(curRunId -> this.addRun(curRunId, secondData.get(curRunId).frequencies, secondData.get(curRunId).complexValues));
        } else throw new RuntimeException("Impossible to merge sensors with different names");
    }
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class SensorData implements FRF{
        @Getter
        @Setter
        private List<Double> frequencies;
        @Getter
        @Setter
        private List<Complex> complexValues;
        @Override
        public List<Double> getXData(){
            return complexValues.stream().map(Complex::getReal).toList();
        }
        @Override
        public List<Double> getYData() {
            return complexValues.stream().map(Complex::getImag).toList();
        }
    }
}