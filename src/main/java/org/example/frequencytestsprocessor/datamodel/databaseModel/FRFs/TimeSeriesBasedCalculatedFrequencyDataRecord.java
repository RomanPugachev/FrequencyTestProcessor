package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;

@Table(name="timeSeriesBasedCalculatedFrequencyDataRecords")
@DiscriminatorValue(value = "timeSeriesBased")
public class TimeSeriesBasedCalculatedFrequencyDataRecord extends CalculatedFrequencyDataRecords{
    @ManyToOne
    @JoinColumn(name = "parentDatasetId")
    private TimeSeriesDataset parentTimeSeriesDataset;

    private Long leftLimitId;

    private Long rightLimitId;

    public TimeSeriesBasedCalculatedFrequencyDataRecord() {}

    public TimeSeriesBasedCalculatedFrequencyDataRecord(TimeSeriesDataset parentTimeSeriesDataset, Long leftLimitId, Long rightLimitId) {
        this.parentTimeSeriesDataset = parentTimeSeriesDataset;
        this.leftLimitId = leftLimitId;
        this.rightLimitId = rightLimitId;
        refreshRawFrequencyData();
    }

    @Override
    public void refreshRawFrequencyData() {
        // TODO: implement Fourier transform
    }
}
