package org.example.frequencytestsprocessor.datamodel.databaseModel.FRFs;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "frequencyDataRecords")
@DiscriminatorColumn(name = "frfType", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FrequencyDataRecord extends AbstractDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recordId;

    @Embedded
    @Transient
    private RawFrequencyData rawFrequencyData;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "expectingAndProvidingDataFrequencyDataRecords",
            joinColumns = { @JoinColumn(name = "expectingRecordId") },
            inverseJoinColumns = { @JoinColumn(name = "providingRecordId") }
    )
    private Set<FrequencyDataRecord> providingDataFrequencyDataRecords = new HashSet<>();

    @ManyToMany(mappedBy = "providingDataFrequencyDataRecords")
    private Set<FrequencyDataRecord> expectingDataFrequencyDataRecords = new HashSet<>();

    public FrequencyDataRecord() {}

    @PrePersist
    @PreUpdate
    public abstract void refreshRawFrequencyData();

    public FrequencyDataRecord addProvidingDataFrequencyDataRecord(FrequencyDataRecord frequencyDataRecord) {
        providingDataFrequencyDataRecords.add(frequencyDataRecord);
        frequencyDataRecord.getExpectingDataFrequencyDataRecords().add(this);
        return this;
    }

    public FrequencyDataRecord removeProvidingDataFrequencyDataRecord(FrequencyDataRecord frequencyDataRecord) {
        providingDataFrequencyDataRecords.remove(frequencyDataRecord);
        frequencyDataRecord.getExpectingDataFrequencyDataRecords().remove(this);
        return this;
    }

    public FrequencyDataRecord addExpectingDataFrequencyDataRecord(FrequencyDataRecord frequencyDataRecord) {
        expectingDataFrequencyDataRecords.add(frequencyDataRecord);
        frequencyDataRecord.getProvidingDataFrequencyDataRecords().add(this);
        return this;
    }

    public FrequencyDataRecord removeExpectingDataFrequencyDataRecord(FrequencyDataRecord frequencyDataRecord) {
        expectingDataFrequencyDataRecords.remove(frequencyDataRecord);
        frequencyDataRecord.getProvidingDataFrequencyDataRecords().remove(this);
        return this;
    }
}
