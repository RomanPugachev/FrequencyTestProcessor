package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;
import org.example.frequencytestsprocessor.datamodel.myMath.Complex;
import org.example.frequencytestsprocessor.datamodel.myMath.FourierTransforms;

import java.util.Arrays;
import java.util.List;

import static org.example.frequencytestsprocessor.commons.CommonMethods.getDataForFourierTransforms;
import static org.example.frequencytestsprocessor.commons.CommonMethods.getFrequenciesOfBorderedSeries;


@Getter
@Setter
@Entity
@Table(name="timeSeriesBasedCalculatedFrequencyDataRecords")
@DiscriminatorValue(value = "timeSeriesBased")
public class TimeSeriesBasedCalculatedFrequencyDataRecord extends CalculatedFrequencyDataRecord {
    @ManyToOne
    @JoinColumn(name = "parentDatasetId")
    private TimeSeriesDataset parentTimeSeriesDataset;

    private Double leftLimit;

    private Double rightLimit;

    public TimeSeriesBasedCalculatedFrequencyDataRecord() {}

    public TimeSeriesBasedCalculatedFrequencyDataRecord(TimeSeriesDataset parentTimeSeriesDataset, Double leftLimit, Double rightLimit, String name) {
        super(name);
        this.parentTimeSeriesDataset = parentTimeSeriesDataset;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
        refreshRawFrequencyData();
    }

    @Override
    public void refreshRawFrequencyData() {
        List<Double> dataToTransform = getDataForFourierTransforms(parentTimeSeriesDataset.getTimeData(), parentTimeSeriesDataset.getParentTimeStamps1(), leftLimit, rightLimit);
        Complex[] complexes = FourierTransforms.fft(dataToTransform);
        List<Double> frequencies = getFrequenciesOfBorderedSeries(parentTimeSeriesDataset.getParentTimeStamps1(), leftLimit, rightLimit);
        setRawFrequencyData(new RawFrequencyData(frequencies, Arrays.stream(complexes).toList()));
    }
}
