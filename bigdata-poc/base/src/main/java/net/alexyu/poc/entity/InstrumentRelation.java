package net.alexyu.poc.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "instrument_relations")
public class InstrumentRelation implements Serializable {


    @Embeddable
    public static class InstrumentRelationId implements Serializable{

        @Column(name = "inst_id")
        private String instId;

        @Column(name = "const_inst_id")
        private String constInstId;

        public InstrumentRelationId(String instId, String constInstId) {
            this.instId = instId;
            this.constInstId = constInstId;
        }

        public InstrumentRelationId() {
        }

        public String getInstId() {
            return instId;
        }

        public void setInstId(String instId) {
            this.instId = instId;
        }

        public String getConstInstId() {
            return constInstId;
        }

        public void setConstInstId(String constInstId) {
            this.constInstId = constInstId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InstrumentRelationId instrumentRelationId = (InstrumentRelationId) o;
            return Objects.equals(instId, instrumentRelationId.instId) &&
                    Objects.equals(constInstId, instrumentRelationId.constInstId);
        }

        @Override
        public int hashCode() {

            return Objects.hash(instId, constInstId);
        }

        @Override
        public String toString() {
            return "InstrumentRelationId{" +
                    "instId='" + instId + '\'' +
                    ", constInstId='" + constInstId + '\'' +
                    '}';
        }
    }


    @EmbeddedId
    private InstrumentRelationId instrumentRelationId;

    @Column(name = "weight")
    private double weight;

    public InstrumentRelation(InstrumentRelationId instrumentRelationId, double weight) {
        this.instrumentRelationId = instrumentRelationId;
        this.weight = weight;
    }

    public InstrumentRelation() {
    }

    public InstrumentRelationId getInstrumentRelationId() {
        return instrumentRelationId;
    }

    public void setInstrumentRelationId(InstrumentRelationId instrumentRelationId) {
        this.instrumentRelationId = instrumentRelationId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentRelation that = (InstrumentRelation) o;
        return Double.compare(that.weight, weight) == 0 &&
                Objects.equals(instrumentRelationId, that.instrumentRelationId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(instrumentRelationId, weight);
    }

    @Override
    public String toString() {
        return "InstrumentRelation{" +
                "instrumentRelationId=" + instrumentRelationId +
                ", weight=" + weight +
                '}';
    }
}
