package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;

import lombok.Getter;

import java.util.Map;

public class SensorProxyForTable extends Sensor {
    @Getter
    private String stringId;
    private Sensor originalSensor;

    public SensorProxyForTable(Sensor originalSensor, String stringId) {
        this.originalSensor = originalSensor;
        this.stringId = stringId;
    }

    @Override
    public String getSensorName() {
        return originalSensor.getSensorName();
    }

    @Override
    public SensorDataType getParentType() {
        return originalSensor.getParentType();
    }

    @Override
    public void setParentType(SensorDataType parentType) {
        originalSensor.setParentType(parentType);
    }

    @Override
    public Map<Long, SensorData> getData() {
        return originalSensor.getData();
    }
}
