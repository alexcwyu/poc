package net.alexyu.poc.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "instrument_relations")
public class Position implements Serializable {

    @Embeddable
    public static class PositionId implements Serializable {

        @Column(name = "acct_id", nullable = false)
        private String acctId;

        @Column(name = "inst_id", nullable = false)
        private String instId;

        public String getAcctId() {
            return acctId;
        }

        public void setAcctId(String acctId) {
            this.acctId = acctId;
        }

        public String getInstId() {
            return instId;
        }

        public void setInstId(String instId) {
            this.instId = instId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionId that = (PositionId) o;
            return Objects.equals(acctId, that.acctId) &&
                    Objects.equals(instId, that.instId);
        }

        @Override
        public int hashCode() {

            return Objects.hash(acctId, instId);
        }

        @Override
        public String toString() {
            return "PositionId{" +
                    "acctId='" + acctId + '\'' +
                    ", instId='" + instId + '\'' +
                    '}';
        }
    }


    @EmbeddedId
    private PositionId positionId;

    @Column(name = "total_qty")
    private double totalQty;

    @Column(name = "avg_price")
    private double avgPrice;

    @Column(name = "create_timestamp")
    private long createTimestamp;

    @Column(name = "update_timestamp")
    private long updateTimestamp;

    public PositionId getPositionId() {
        return positionId;
    }

    public void setPositionId(PositionId positionId) {
        this.positionId = positionId;
    }

    public double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(double totalQty) {
        this.totalQty = totalQty;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.totalQty, totalQty) == 0 &&
                Double.compare(position.avgPrice, avgPrice) == 0 &&
                createTimestamp == position.createTimestamp &&
                updateTimestamp == position.updateTimestamp &&
                Objects.equals(positionId, position.positionId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(positionId, totalQty, avgPrice, createTimestamp, updateTimestamp);
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionId=" + positionId +
                ", totalQty=" + totalQty +
                ", avgPrice=" + avgPrice +
                ", createTimestamp=" + createTimestamp +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}
