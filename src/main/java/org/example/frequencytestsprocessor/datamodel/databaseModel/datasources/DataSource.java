package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


// TODO: implement DataSource and contunue implementing the rest of the data model
@Entity
@Table(name="sources")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "sourceType", discriminatorType = DiscriminatorType.STRING)
public class DataSource {
    @Id
    @GeneratedValue
    private Long sourceId;
    @Getter
    @Column(nullable = false, unique = true)
    private String sourceName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime loadDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    public DataSource() {}

    public DataSource(String sourceName) {
        this.sourceName = sourceName;
    }
}
