package net.alexyu.poc.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "instruments")
public class Instrument implements Serializable {

    public enum InstType {
        STK,
        CSH,
        OPT,
        ETF,
        IDX,
        FUT,
        FWD,
        FOP,
        WNT,
        ADR,
        GDR,
        CDR,
        SWP,
        FXO,
        CDS,
        CDO,
        CB
    }

    public enum OptionType {
        UndefinedOptType,
        Call,
        Put,
    }

    public enum OptionStyle {
        UndefinedOptStyle,
        European,
        American
    }

    @Id
    @Column(name = "inst_id")
    private String instId;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "exch_id")
    private String exchId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private InstType type;

    @Column(name = "ccy_id")
    private String ccyId;

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "und_inst_id")
    private String undInstId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "option_type")
    private OptionType optionType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "option_style")
    private OptionStyle optionStyle;

    @Column(name = "strike")
    private double strike;

    @Column(name = "exp_date")
    private int expDate;

    @Column(name = "conversion_ratio")
    private double conversionRatio;

    @Column(name = "gics_sector")
    private int gicsSector;

    @Column(name = "gics_industry_group")
    private int gicsIndustryGroup;

    @Column(name = "gics_industry")
    private int gicsIndustry;

    @Column(name = "gics_sub_industry")
    private int gicsSubIndustry;

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchId() {
        return exchId;
    }

    public void setExchId(String exchId) {
        this.exchId = exchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstType getType() {
        return type;
    }

    public void setType(InstType type) {
        this.type = type;
    }

    public String getCcyId() {
        return ccyId;
    }

    public void setCcyId(String ccyId) {
        this.ccyId = ccyId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }


    public int getGicsSector() {
        return gicsSector;
    }

    public void setGicsSector(int gicsSector) {
        this.gicsSector = gicsSector;
    }

    public int getGicsIndustryGroup() {
        return gicsIndustryGroup;
    }

    public void setGicsIndustryGroup(int gicsIndustryGroup) {
        this.gicsIndustryGroup = gicsIndustryGroup;
    }

    public int getGicsIndustry() {
        return gicsIndustry;
    }

    public void setGicsIndustry(int gicsIndustry) {
        this.gicsIndustry = gicsIndustry;
    }

    public int getGicsSubIndustry() {
        return gicsSubIndustry;
    }

    public void setGicsSubIndustry(int gicsSubIndustry) {
        this.gicsSubIndustry = gicsSubIndustry;
    }

    public String getUndInstId() {
        return undInstId;
    }

    public void setUndInstId(String undInstId) {
        this.undInstId = undInstId;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public OptionStyle getOptionStyle() {
        return optionStyle;
    }

    public void setOptionStyle(OptionStyle optionStyle) {
        this.optionStyle = optionStyle;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public int getExpDate() {
        return expDate;
    }

    public void setExpDate(int expDate) {
        this.expDate = expDate;
    }

    public double getConversionRatio() {
        return conversionRatio;
    }

    public void setConversionRatio(double conversionRatio) {
        this.conversionRatio = conversionRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return Double.compare(that.strike, strike) == 0 &&
                expDate == that.expDate &&
                Double.compare(that.conversionRatio, conversionRatio) == 0 &&
                gicsSector == that.gicsSector &&
                gicsIndustryGroup == that.gicsIndustryGroup &&
                gicsIndustry == that.gicsIndustry &&
                gicsSubIndustry == that.gicsSubIndustry &&
                Objects.equals(instId, that.instId) &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(exchId, that.exchId) &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(ccyId, that.ccyId) &&
                Objects.equals(countryId, that.countryId) &&
                Objects.equals(undInstId, that.undInstId) &&
                optionType == that.optionType &&
                optionStyle == that.optionStyle;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "instId='" + instId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", exchId='" + exchId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", ccyId='" + ccyId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", undInstId='" + undInstId + '\'' +
                ", optionType=" + optionType +
                ", optionStyle=" + optionStyle +
                ", strike=" + strike +
                ", expDate=" + expDate +
                ", conversionRatio=" + conversionRatio +
                ", gicsSector=" + gicsSector +
                ", gicsIndustryGroup=" + gicsIndustryGroup +
                ", gicsIndustry=" + gicsIndustry +
                ", gicsSubIndustry=" + gicsSubIndustry +
                '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(instId, symbol, exchId, name, type, ccyId, countryId, undInstId, optionType, optionStyle, strike, expDate, conversionRatio, gicsSector, gicsIndustryGroup, gicsIndustry, gicsSubIndustry);


    }
}
