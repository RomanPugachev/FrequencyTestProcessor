package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.frequencytestsprocessor.converters.LongListConverter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "timeSeriesSources")
@DiscriminatorValue("TimeSeriesSource")
public class TimeSeriesDataSource extends DataSource {

    @Convert(converter = LongListConverter.class)
    private List<Long> timeStamps;
    @Getter
    @OneToMany(mappedBy = "parentTimeSeries")
    private List<TimeSeriesDataset> timeSeriesDatasets;

    public TimeSeriesDataSource() {
    }

    public TimeSeriesDataSource(String csvPath) {
        super(csvPath);
    }

    public TimeSeriesDataSource addTimeSeriesDataset(TimeSeriesDataset timeSeriesDataset) {
        if (timeSeriesDatasets == null) {
            timeSeriesDatasets = new ArrayList<>();
        }
        timeSeriesDatasets.add(timeSeriesDataset);
        timeSeriesDataset.setParentTimeSeries(this);
        return this;
    }

    public TimeSeriesDataSource addTimeSeriesDataset(List<TimeSeriesDataset> timeSeriesDataset) {
        if (timeSeriesDatasets == null) {
            timeSeriesDatasets = new ArrayList<>();
        }
        timeSeriesDatasets.addAll(timeSeriesDataset);
        timeSeriesDataset.forEach(timeSeriesDataset1 -> timeSeriesDataset1.setParentTimeSeries(this));
        return this;
    }


    public TimeSeriesDataSource addTimeStamps(Long timeStamps) {
        if (this.timeStamps == null) {
            this.timeStamps = new LinkedList<>();
        }
        this.timeStamps.add(timeStamps);
        return this;
    }

    public TimeSeriesDataSource addTimeStamps(List<Long> timeStamps) {
        if (this.timeStamps == null) {
            this.timeStamps = new LinkedList<>();
        }
        this.timeStamps.addAll(timeStamps);
        return this;
    }

    public static TimeSeriesDataSource ofCSV(String csvPath) {
        TimeSeriesDataSource timeSeriesDataSource = new TimeSeriesDataSource(csvPath);
        timeSeriesDataSource.timeSeriesDatasets = new ArrayList<>();
        return timeSeriesDataSource;
    }
}
