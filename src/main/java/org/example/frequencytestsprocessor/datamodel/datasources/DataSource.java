package org.example.frequencytestsprocessor.datamodel.datasources;


import jakarta.persistence.*;
import lombok.Getter;


// TODO: implement DataSource and contunue implementing the rest of the data model
@Entity
@Table(name="sources")
@Inheritance(strategy = InheritanceType.JOINED)
public class DataSource {
    @Id
    @GeneratedValue
    private Long sourceId;
    @Getter
    @Column(nullable = false)
    private String sourceName;
    @Getter
    @Enumerated(EnumType.STRING)
    private DataSourceType dataSourceType;

    public enum DataSourceType {
        UFF,
        TIMESERIES,
    }
}
