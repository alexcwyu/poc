package info.alexyu.stress.baselinecalculator;

import java.util.Objects;

public class Instrument {

    private final String cusip;

    public Instrument(String cusip) {
        this.cusip = cusip;
    }

    public String getCusip() {
        return cusip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instrument that = (Instrument) o;
        return Objects.equals(cusip, that.cusip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cusip);
    }
}
