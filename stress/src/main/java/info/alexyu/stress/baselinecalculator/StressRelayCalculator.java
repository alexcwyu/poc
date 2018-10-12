package info.alexyu.stress.baselinecalculator;

public interface StressRelayCalculator<Portfolio, Instrument, Vector, Configuration> {

    double calculate(Portfolio portfolio, Instrument[] instruments, Vector[] vectos, Configuration configuration);
}
