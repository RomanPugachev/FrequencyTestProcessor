package org.example.frequencytestsprocessor.datamodel.datasources;


import jakarta.persistence.*;
import lombok.Getter;


// TODO: implement DataSource and contunue implementing the rest of the data model
@Table(name="source")
@Entity
public class DataSource {
    @Id
    @GeneratedValue
    @Column(name = "sourceId", nullable = false)
    private Long sourceId;
    @Getter
    private String sourceName;


    public enum DataSourceType {
        UFF,
        TIMESERIES,
    }
}
