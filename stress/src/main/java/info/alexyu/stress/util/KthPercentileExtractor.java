package info.alexyu.stress.util;

public class KthPercentileExtractor {

    public static double percentile(float[] sortedArray, double confidence){
        final int l = sortedArray.length;
        final double k = confidence * l;
        final double rank = Math.floor(k);
        final int i = Double.valueOf(rank).intValue();
        return sortedArray[i];
    }
}
