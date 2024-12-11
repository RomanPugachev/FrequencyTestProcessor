package org.example.frequencytestsprocessor.datamodel.UFFDatasets.UFF58Repr;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class SensorProxyForTable extends Sensor implements IdManager.HasId {
    @Getter
    @Setter
    private String id;
    private Sensor originalSensor;

    public SensorProxyForTable(Sensor originalSensor) {
        this.originalSensor = originalSensor;
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
