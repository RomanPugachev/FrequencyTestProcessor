package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class FrequencyDataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String frfId;

    private Long sourceId;

    @Enumerated(EnumType.STRING)
    private FRFSourceType sourceType;

    @Transient
    private FRFProvider frfProvider;

    @Embedded
    private RawFrequencyData rawFrequencyData;

    public FrequencyDataRecord() {}

    public FrequencyDataRecord(FRFSourceType sourceType, FRFProvider frfProvider) {
        this.sourceType = sourceType;
        this.sourceId = frfProvider.getSourceId();
        this.frfProvider = frfProvider;
        this.rawFrequencyData = frfProvider.getRawFrequencyData();
    }

    @PrePersist
    @PreUpdate
    private void updateRawFrequencyData() {
        frfProvider.getRawFrequencyData();
    }
}
