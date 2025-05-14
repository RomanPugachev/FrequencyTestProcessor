package org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents.AircraftModel;

@AllArgsConstructor
public class AircraftModelProxy implements DataSourceTableProxy{
    @Getter
    @Setter
    private AircraftModel aircraftModel;

    @Override
    public String getTableColumnValue() {
        return aircraftModel.getAircraftModelName();
    }
}
