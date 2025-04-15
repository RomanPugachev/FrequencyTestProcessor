package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "frequencyDataRecords")
@DiscriminatorColumn(name = "frfType", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FrequencyDataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String recordId;

    @Embedded
    @Transient
    private RawFrequencyData rawFrequencyData;

    public FrequencyDataRecord() {}

    @PrePersist
    @PreUpdate
    public abstract void refreshRawFrequencyData();

}
