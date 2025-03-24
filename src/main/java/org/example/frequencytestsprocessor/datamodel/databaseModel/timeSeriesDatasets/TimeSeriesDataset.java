package org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets;

import jakarta.persistence.*;
import org.example.frequencytestsprocessor.converters.DoubleListConverter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;

import java.util.List;

@Entity
public class TimeSeriesDataset {
    @Id
    @GeneratedValue
    @Column(name = "datasetId")
    private Long datasetId;

    @ManyToOne
    @JoinColumn(name = "sourceId", insertable = false, updatable = false)
    private TimeSeriesDataSource parentTimeSeries;

    @Convert(converter = DoubleListConverter.class)
    private List<Double> timeData;
}
