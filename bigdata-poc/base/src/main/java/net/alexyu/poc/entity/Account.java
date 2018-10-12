package net.alexyu.poc.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "accounts")
public class Account implements Serializable {


    public enum AccountType {
        MARGIN_ACCOUNT,
        DATA_ACCOUNT
    }


    public enum BusGroup {
        PB,
        PWM,
        IED,
        LD,
        FID
    }


    public enum Region {
        NY,
        LN,
        HK
    }

    @Id
    @Column(name = "acct_id")
    private String acctId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "acct_type")
    private AccountType acctType;

    @Column(name = "ultimate_client_id")
    private String ultimateClientId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "bus_group")
    private BusGroup busGroup;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "region")
    private Region region;

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public AccountType getAcctType() {
        return acctType;
    }

    public void setAcctType(AccountType acctType) {
        this.acctType = acctType;
    }

    public String getUltimateClientId() {
        return ultimateClientId;
    }

    public void setUltimateClientId(String ultimateClientId) {
        this.ultimateClientId = ultimateClientId;
    }

    public BusGroup getBusGroup() {
        return busGroup;
    }

    public void setBusGroup(BusGroup busGroup) {
        this.busGroup = busGroup;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(acctId, account.acctId) &&
                acctType == account.acctType &&
                Objects.equals(ultimateClientId, account.ultimateClientId) &&
                busGroup == account.busGroup &&
                Objects.equals(region, account.region);
    }

    @Override
    public int hashCode() {

        return Objects.hash(acctId, acctType, ultimateClientId, busGroup, region);
    }

    @Override
    public String toString() {
        return "Account{" +
                "acctId='" + acctId + '\'' +
                ", acctType=" + acctType +
                ", ultimateClientId='" + ultimateClientId + '\'' +
                ", busGroup=" + busGroup +
                ", region='" + region + '\'' +
                '}';
    }
}
