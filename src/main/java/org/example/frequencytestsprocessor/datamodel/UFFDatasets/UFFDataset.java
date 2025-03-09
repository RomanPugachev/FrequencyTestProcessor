package org.example.frequencytestsprocessor.datamodel.UFFDatasets;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.datasources.UFF;

@Entity
@Table(name = "uffDatasets")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dataset_type", discriminatorType = DiscriminatorType.INTEGER)
public class UFFDataset {
    @EmbeddedId
    private DatasetId datasetId;

    @ManyToOne
    @JoinColumn(name = "sourceId", insertable = false, updatable = false)
    private UFF parentUFF;

    @Id
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
