package info.alexyu.stress;

public interface Calculator {

    void init(int cusipUniverseSize, int historicalVectorDepth, int accountUniverseSize, int accountPortfolioSize);

    void addVector(String instrumentIdentifier, float[] pnlVector);

    double calculate(String[] instrumentIdentifiersp, float[] attributionFactorVector, float confidence);

    void destory();
}
