package org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.converters.DoubleListConverter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs.TimeSeriesBasedCalculatedFrequencyDataRecord;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.TimeSeriesDataSource;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "timeSeriesDatasets")
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
    @JoinColumn(name = "sourceId", insertable = false)
    private TimeSeriesDataSource parentTimeSeries;

    @Getter
    @Setter
    @Transient
    private List<Double> parentTimeStamps1;
    @Getter
    @Setter
    @Transient
    private List<Double> parentTimeStamps2;



    @Getter
    @Setter
    @Convert(converter = DoubleListConverter.class)
    private List<Double> timeData;

    @OneToMany(mappedBy = "parentTimeSeriesDataset", cascade = CascadeType.ALL)
    private List<TimeSeriesBasedCalculatedFrequencyDataRecord> timeSeriesBasedFRFS;

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

    public List<Double> getParentTimeStamps1(){
        return parentTimeSeries.getTimeStamps1();
    }

    public List<Double> getParentTimeStamps2(){
        return parentTimeSeries.getTimeStamps2();
    }

    public TimeSeriesBasedCalculatedFrequencyDataRecord addChildFrequencyRecord(TimeSeriesBasedCalculatedFrequencyDataRecord incomingRecord) {
        timeSeriesBasedFRFS.add(incomingRecord);
        incomingRecord.setParentTimeSeriesDataset(this);
        return incomingRecord;
    }
}
