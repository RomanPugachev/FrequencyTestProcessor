package org.example.frequencytestsprocessor.datamodel.UFFDatasets;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DatasetId implements Serializable {

    @Column(name = "sourceId")
    @Getter
    private Long sourceId;

    @Column(name = "orderIndex")
    @Getter
    private Integer orderIndex;

    public DatasetId() {}

    public DatasetId(Long sourceId, Integer orderIndex) {
        this.sourceId = sourceId;
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatasetId that = (DatasetId) o;
        return Objects.equals(sourceId, that.sourceId) &&
                Objects.equals(orderIndex, that.orderIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, orderIndex);
    }
}

