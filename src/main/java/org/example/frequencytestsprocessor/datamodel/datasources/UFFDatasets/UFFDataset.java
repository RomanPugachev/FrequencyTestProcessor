package org.example.frequencytestsprocessor.datamodel.datasources.UFFDatasets;

import lombok.Getter;
import lombok.Setter;

import lombok.AccessLevel;

public abstract class UFFDataset {
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int type;
}
