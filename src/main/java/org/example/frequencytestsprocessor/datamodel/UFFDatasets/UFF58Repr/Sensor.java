package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;
import lombok.*;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;

import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    @Getter
    private String sensorName;
    @Getter
    @Setter
    SensorDataType parentType;
    @Getter
    private List<Double> freqs;
    @Getter
    private List<Complex> data;
    public Sensor(String sensorName, List<Double> freqs, List<Complex> data) {
        this.sensorName = sensorName;
        this.freqs = freqs;
        this.data = data;
    }
}