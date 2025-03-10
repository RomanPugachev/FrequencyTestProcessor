package org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.UFF;

@Entity
@Table(name = "uffDatasets")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dataset_type", discriminatorType = DiscriminatorType.INTEGER)
public class UFFDataset {
    @ManyToOne
    @JoinColumn(name = "sourceId", insertable = false, updatable = false)
    private UFF parentUFF;
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long datasetId;
    @Getter
    @Setter
    private String rawDataset;

    public UFFDataset() {}

    public UFFDataset(UFF parentUFF) {
        this.parentUFF = parentUFF;
    }
}
