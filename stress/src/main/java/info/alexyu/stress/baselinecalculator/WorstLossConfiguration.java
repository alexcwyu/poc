package info.alexyu.stress.baselinecalculator;

public class WorstLossConfiguration {

    private final float confidence;

    public WorstLossConfiguration(float confidence) {
        this.confidence = confidence;
    }

    public float getConfidence() {
        return confidence;
    }
}
