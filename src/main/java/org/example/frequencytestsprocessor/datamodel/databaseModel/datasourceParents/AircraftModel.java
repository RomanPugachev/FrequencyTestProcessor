//package org.example.frequencytestsprocessor.datamodel.databaseModel.datasourceParents;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.example.frequencytestsprocessor.datamodel.databaseModel.sharedEntities.AbstractDataset;
//
//import java.util.List;
//
//@Entity
//@Table(name = "aircraftModels")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class AircraftModel {
//    private String aircraftModelName;
//
//    @OneToMany
//    @JoinColumn(name = "parentAircraftModelId")
//    private List<? extends AbstractDataset> datasets;
//}
