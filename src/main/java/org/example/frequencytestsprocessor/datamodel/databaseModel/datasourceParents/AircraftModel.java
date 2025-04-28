package org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents;

import jakarta.persistence.*;
import lombok.*;
import org.example.frequencytestsprocessor.datamodel.databaseModel.datasources.DataSource;
import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aircraftModels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftModel {
    @Id
    @GeneratedValue
    private Long aircraftModelId;

    @Column(unique = true, nullable = false)
    private String aircraftModelName;

    @OneToMany
    @JoinColumn(name = "parentAircraftModelId")
    private List<DataSource> dataSources = new ArrayList<>();

    public AircraftModel(String aircraftModelName) {
        this.aircraftModelName = aircraftModelName;
    }

    public void addDataSource(DataSource dataSource) {
        dataSources.add(dataSource);
        dataSource.setParentAircraftModel(this);
    }
}
