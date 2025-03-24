package org.example.frequencytestsprocessor.datamodel.databaseModel.datasources;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.example.frequencytestsprocessor.datamodel.databaseModel.timeSeriesDatasets.TimeSeriesDataset;

import java.util.List;

@Entity
@Table(name = "timeSeriesSources")
@DiscriminatorValue("TimeSeriesSource")
public class TimeSeriesDataSource extends DataSource {
    @OneToMany(mappedBy = "parentTimeSeries")
    private List<TimeSeriesDataset> timeSeries;
}
