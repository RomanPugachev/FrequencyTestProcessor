package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.Embedded;

public interface FRFProvider {
    @Embedded
    RawFrequencyData getRawFrequencyData();
    Long getSourceId();
}
