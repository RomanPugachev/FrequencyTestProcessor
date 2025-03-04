package org.example.frequencytestsprocessor.datamodel.datasources.UFFDatasets.UFF58Repr;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.frequencytestsprocessor.datamodel.controlTheory.DiscreteFRF;
import org.example.frequencytestsprocessor.services.idManagement.IdManager;

import java.util.Map;

@ToString
public class SensorProxyForTable extends Sensor implements IdManager.HasId {
    @Getter
    @Setter
    private String id;
    @Getter
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
    public Map<Long, DiscreteFRF> getData() {
        return originalSensor.getData();
    }
}
