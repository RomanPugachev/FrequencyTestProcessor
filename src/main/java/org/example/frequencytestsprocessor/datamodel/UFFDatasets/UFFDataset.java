package org.example.frequencytestsprocessor.datamodel.UFFDatasets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class UFFDataset {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int type;
}
