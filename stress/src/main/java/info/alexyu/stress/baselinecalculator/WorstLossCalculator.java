package info.alexyu.stress.baselinecalculator;

public class WorstLossCalculator implements StressRelayCalculator<Portfolio, Instrument, PnlVector, WorstLossConfiguration> {

    @Override
    public double calculate(Portfolio portfolio, Instrument[] instruments, PnlVector[] vectos, WorstLossConfiguration worstLossConfiguration) {
        return 0;
    }


}
