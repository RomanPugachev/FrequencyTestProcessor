package org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents.AircraftModel;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.DataSource;

@AllArgsConstructor
public class  DataSourceProxy implements DataSourceTableProxy{
    @Getter
    @Setter
    private DataSource dataSource;

    @Override
    public String getTableColumnValue() {
        return dataSource.getSourceAddress();
    }
}

