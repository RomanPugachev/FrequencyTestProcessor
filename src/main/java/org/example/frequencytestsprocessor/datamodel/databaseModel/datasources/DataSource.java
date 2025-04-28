package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents.AircraftModel;
import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="sources")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "sourceType", discriminatorType = DiscriminatorType.STRING)
public abstract class DataSource {
    @Id
    @GeneratedValue
    private Long sourceId;
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String sourceAddress;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime loadDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "parentAircraftModelId", insertable = false)
    private AircraftModel parentAircraftModel;

    public DataSource() {}

    public DataSource(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public abstract List<? extends AbstractDataset> getDatasets();
}
