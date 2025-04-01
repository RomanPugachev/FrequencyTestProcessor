package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = "timeSeriesBasedFRFs")
public class TimeSeriesBasedFRF extends FrequencyDataRecord{
    @ManyToOne
    @JoinColumn(name = "parentDatasetId")
    private TimeSeriesDataset parentTimeSeriesDataset;

    private Long leftLimitId;

    private Long rightLimitId;

    public TimeSeriesBasedFRF() {}

    public TimeSeriesBasedFRF(TimeSeriesDataset parentTimeSeriesDataset, Long leftLimitId, Long rightLimitId) {
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
