package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.Embedded;

public interface FRFProvider {
    RawFrequencyData getRawFrequencyData();
    Long getSourceId();
}
