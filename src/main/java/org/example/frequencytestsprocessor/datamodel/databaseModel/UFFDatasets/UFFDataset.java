package org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.UFFDataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;

@Entity
@Table(name = "uffDatasets")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dataset_type", discriminatorType = DiscriminatorType.INTEGER)
public class UFFDataset extends AbstractDataset {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long datasetId;

    @Transient
    @Getter
    @Setter
    protected String type;

    @Setter
    @ManyToOne
    @JoinColumn(name = "sourceId", insertable = false)
    private UFFDataSource parentUFF;

    @Getter
    @Setter
    private String rawDataset;

    public UFFDataset() {}

    public UFFDataset(UFFDataSource parentUFF) {
        this.parentUFF = parentUFF;
    }

    @Override
    public String getDatasetName(){
        return this.parentUFF.getSourceAddress() + "_" + this.datasetId;
    }
}
