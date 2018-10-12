package info.alexyu.stress.baselinecalculator;

import java.util.Arrays;

public class PnlVector {

    private final float[] pnls;

    public PnlVector(float[] pnls) {
        this.pnls = pnls;
    }

    public float[] getPnls() {
        return pnls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PnlVector pnlVector = (PnlVector) o;
        return Arrays.equals(pnls, pnlVector.pnls);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pnls);
    }
}
