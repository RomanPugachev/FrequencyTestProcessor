package org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.converters.DoubleListConverter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.TimeSeriesBasedFRF;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;

import java.util.LinkedList;
import java.util.List;

@Entity
public class TimeSeriesDataset {
    @Id
    @GeneratedValue
    @Column(name = "datasetId")
    private Long datasetId;

    @Getter
    @Setter
    private String datasetName;

    @Setter
    @ManyToOne
    @JoinColumn(name = "sourceId", insertable = false, updatable = false)
    private TimeSeriesDataSource parentTimeSeries;

    @Getter
    @Convert(converter = DoubleListConverter.class)
    private List<Double> timeData;

    @OneToMany(mappedBy = "parentTimeSeriesDataset")
    private List<TimeSeriesBasedFRF> timeSeriesBasedFRFS;

    public TimeSeriesDataset() {
    }

    public TimeSeriesDataset(String datasetName) {
        this.datasetName = datasetName;
    }

    public TimeSeriesDataset addTimeData(Double timeData) {
        if (this.timeData == null) {
            this.timeData = new LinkedList<>();
            this.timeData.add(timeData);
        } else {
            this.timeData.add(timeData);
        }
        if (timeData == null) {
            System.out.println("null timeData");
        }
        return this;
    }

    public TimeSeriesDataset addTimeData(List<Double> timeData) {
        if (this.timeData == null) {
            this.timeData = timeData;
        } else {
            this.timeData.addAll(timeData);
        }
        return this;
    }
}
