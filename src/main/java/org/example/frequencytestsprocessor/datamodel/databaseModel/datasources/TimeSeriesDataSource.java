package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.converters.DoubleListConverter;
import org.example.frequencytestsprocessor.converters.LongListConverter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "timeSeriesSources")
@DiscriminatorValue("TimeSeriesSource")
public class TimeSeriesDataSource extends DataSource {

    @Convert(converter = DoubleListConverter.class)
    private List<Double> timeStamps1 = new LinkedList<>();
    @Convert(converter = DoubleListConverter.class)
    private List<Double> timeStamps2 = new LinkedList<>();

    @Setter
    @Getter
    @OneToMany(mappedBy = "parentTimeSeries", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TimeSeriesDataset> timeSeriesDatasets = new ArrayList<>();

    public TimeSeriesDataSource() {
    }

    public TimeSeriesDataSource(String csvPath) {
        super(csvPath);
    }

    public TimeSeriesDataSource addTimeSeriesDataset(TimeSeriesDataset timeSeriesDataset) {
        timeSeriesDatasets.add(timeSeriesDataset);
        timeSeriesDataset.setParentTimeSeries(this);
        return this;
    }

    public TimeSeriesDataSource addTimeSeriesDataset(List<TimeSeriesDataset> timeSeriesDataset) {
        timeSeriesDatasets.addAll(timeSeriesDataset);
        timeSeriesDataset.forEach(timeSeriesDataset1 -> timeSeriesDataset1.setParentTimeSeries(this));
        return this;
    }


    public TimeSeriesDataSource addTimeStamps1(Double timeStamps) {
        this.timeStamps1.add(timeStamps);
        return this;
    }

    public TimeSeriesDataSource addTimeStamps1(List<Double> timeStamps) {
        this.timeStamps1.addAll(timeStamps);
        return this;
    }

    public TimeSeriesDataSource addTimeStamps2(Double timeStamps) {
        this.timeStamps2.add(timeStamps);
        return this;
    }

    public TimeSeriesDataSource addTimeStamps2(List<Double> timeStamps) {
        this.timeStamps2.addAll(timeStamps);
        return this;
    }

    public static TimeSeriesDataSource ofCSV(String csvPath) {
        TimeSeriesDataSource timeSeriesDataSource = new TimeSeriesDataSource(csvPath);
        timeSeriesDataSource.timeSeriesDatasets = new ArrayList<>();
        return timeSeriesDataSource;
    }
}
