package org.example.frequencytestsprocessor.datamodel.datasetRepresentation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class RepresentableDataset {
    @Getter
    @Setter
    private String datasetId;
    @Getter
    @Setter
    private Long dataSetRunNumber;
    @Getter
    @Setter
    private String datasetDescription;

    @Getter
    protected List<Double> xData;
    @Getter
    protected List<Double> yData;
}
