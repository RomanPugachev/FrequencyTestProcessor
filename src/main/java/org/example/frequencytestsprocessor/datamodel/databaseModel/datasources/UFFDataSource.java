package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;


import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFF58;
import org.example.frequencytestsprocessor.datamodel.databaseModel.UFFDatasets.UFFDataset;


import static org.example.frequencytestsprocessor.commons.StaticStrings.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "uffSources")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@DiscriminatorValue("UFF")
public class UFFDataSource extends DataSource implements Iterable<UFF58> {
    @Getter
    @Setter
    @OneToMany(mappedBy = "parentUFF", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UFFDataset> datasets;

    public UFFDataSource() {super();}
    public UFFDataSource(String sourceAddress) {super(sourceAddress);}

    public void addUFFDataset(UFFDataset dataset) {
        if (datasets == null) {
            datasets = new ArrayList<>();
        }
        datasets.add(dataset);
        dataset.setParentUFF(this);
    }

    public Iterator<UFF58> iterator() {
        return datasets.stream()
                .filter(datasets -> datasets.getClass().getSimpleName().equals(UFF58.class.getSimpleName()))
                .map(datasets -> (UFF58) datasets)
                .iterator();
    }

    @Override
    public String toString() {
         return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                 .append("source_file", getSourceAddress())
                 .append(DATASETS, datasets)
                 .toString();
    }
}