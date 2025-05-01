package org.example.frequencytestsprocessor.datamodel.proxy.dataSourceTableProxy;

import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.CalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.DataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;

import java.util.ArrayList;
import java.util.List;

import static org.example.frequencytestsprocessor.commons.StaticStrings.DEFAULT_CALCULATED_DATA_SOURCE_VALUE;

public class CalculatedSourceProxy extends DataSource implements DataSourceTableProxy {
    private static CalculatedSourceProxy instance;
    private final List<CalculatedFrequencyDataRecord> frequencyDataRecords = new ArrayList<>();

    public List<CalculatedFrequencyDataRecord> getFrequencyDataRecords() {
        return frequencyDataRecords;
    }

    @Override
    public List<CalculatedFrequencyDataRecord> getDatasets(){
        return frequencyDataRecords;
    }

    @Override
    public String getTableColumnValue() {
        return getSourceAddress();
    }
    private CalculatedSourceProxy(String sourceAddress) {
        setSourceAddress(sourceAddress);
    }

    public static CalculatedSourceProxy getInstance() {
        if (instance == null) {
            instance = new CalculatedSourceProxy(DEFAULT_CALCULATED_DATA_SOURCE_VALUE);
        }
        return instance;
    }
}
