package info.alexyu.stress.baselinecalculator;

import java.util.Objects;

public class Position {
    private final Instrument instrument;
    private final int quantity;
    private final float conversionRatio;

    public Position(Instrument instrument, int quantity, float conversionRatio) {
        this.instrument = instrument;
        this.quantity = quantity;
        this.conversionRatio = conversionRatio;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getConversionRatio() {
        return conversionRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return quantity == position.quantity &&
                Float.compare(position.conversionRatio, conversionRatio) == 0 &&
                Objects.equals(instrument, position.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, quantity, conversionRatio);
    }
}
