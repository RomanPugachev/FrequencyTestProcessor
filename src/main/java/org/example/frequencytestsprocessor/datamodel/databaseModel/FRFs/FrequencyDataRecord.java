package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.*;

@Entity
public class FrequencyDataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String frfId;

    private FRFSourceType sourceType;

    @OneToOne
    @JoinColumn(name = "frfProviderId")
    private FRFProvider frfProvider;

    @Embedded
    private RawFrequencyData rawFrequencyData;

    @PrePersist
    @PreUpdate
    private void updateRawFrequencyData() {
        frfProvider.getRawFrequencyData();
    }
}
