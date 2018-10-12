package info.alexyu.stress.baselinecalculator;

import java.util.List;
import java.util.Objects;

public class Portfolio {

    private List<Position> positions;

    public Portfolio(List<Position> positions) {
        this.positions = positions;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(positions, portfolio.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positions);
    }
}
