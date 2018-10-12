package info.alexyu.stress.util;

public class Partitioner {

    public static int[] partitions(int totalLength, int parallelism){
        int [] partitions = new int[parallelism];

        int increment = totalLength / parallelism;
        int idx = 0;
        for (int i = 0; i<parallelism-1; i++){
            idx += increment;
            partitions[i] = idx;
        }
        partitions[parallelism-1] = totalLength;
        return partitions;
    }
}
