package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;
import lombok.*;
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
    @Getter
    private Map<Long, SensorData> data = new HashMap<>();
    public Sensor(String sensorName, Long currentRun, List<Double> freqs, List<Complex> complexes) {
        this.sensorName = sensorName;
        this.data.put(currentRun, new SensorData(freqs, complexes));
    }

    public Sensor addRun(Long currentRun, List<Double> freqs, List<Complex> complexes) {
        if (currentRun == null) {
            throw new RuntimeException("currentRun can't be null");
        } if (freqs == null || freqs.isEmpty()) {
            throw new RuntimeException("freqs can't be null or empty");
        } if (complexes == null || complexes.isEmpty()) {
            throw new RuntimeException("complexes can't be null or empty");
        } if (data.containsKey(currentRun)) {
            throw new RuntimeException("currentRun already exists");
        }
        this.data.put(currentRun, new SensorData(freqs, complexes));
        return this;
    }
    public void mergeSensorData(Sensor secondSensorWithData) {
        Map<Long, SensorData> secondData = secondSensorWithData.data;
        if (this.equals(secondSensorWithData)) {
            secondData.keySet().forEach(curRunId -> this.addRun(curRunId, secondData.get(curRunId).freqs, secondData.get(curRunId).complexes));
        } else throw new RuntimeException("Impossible to merge sensors with different names");
    }
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class SensorData {
        @Getter
        private List<Double> freqs;
        @Getter
        private List<Complex> complexes;
    }
}