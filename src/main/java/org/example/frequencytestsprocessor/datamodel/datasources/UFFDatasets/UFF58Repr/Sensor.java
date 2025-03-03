package org.example.frequencytestsprocessor.datamodel.datasources.UFFDatasets.UFF58Repr;
import lombok.*;
import org.example.frequencytestsprocessor.datamodel.controlTheory.DiscreteFRF;
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
    private Map<Long, DiscreteFRF> data = new HashMap<>();
    public Sensor(String sensorName, Long currentRun, List<Double> frequencies, List<Complex> complexValues) {
        this.sensorName = sensorName;
        this.data.put(currentRun, new DiscreteFRF(frequencies, complexValues));
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
        this.data.put(currentRun, new DiscreteFRF(frequencies, complexValues));
        return this;
    }
    public void mergeSensorData(Sensor secondSensorWithData) {
        Map<Long, DiscreteFRF> secondData = secondSensorWithData.data;
        if (this.equals(secondSensorWithData)) {
            secondData.keySet().forEach(curRunId -> this.addRun(curRunId, secondData.get(curRunId).getFrequencies(), secondData.get(curRunId).getComplexValues()));
        } else throw new RuntimeException("Impossible to merge sensors with different names");
    }
}