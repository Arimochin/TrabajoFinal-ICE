import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TSPHelper {
    private static int[][] matrix;

    public static void setMatrix(int[][] m) {
        matrix = m;
    }

    public static boolean exists(int v, int[] a){
        for(int i : a){
            if(i == v){
                return true;
            }
        }
        return false;
    }

    public static int searchIndex(int value, int[] array) {
        int index = -1;
        for(int i = 0; i < array.length; i++){
            if (array[i] == value) {
                index = i;
            }
        }
        return index;
    }

    public static int getFitness(int[] sample){
        int acum = 0;
        for(int i = 0; i < sample.length-1;i++){
            acum += matrix[sample[i]][sample[i+1]];
        }
        acum+= matrix[sample[sample.length-1]][sample[0]];
        return acum;
    }

    public static List<Sample> toSamples(List<int[]> arrays, int[][] matrix) {
        List<Sample> samples = new ArrayList<>();

        for (int[] sample : arrays) {
            Sample s = new Sample();
            s.setTour(sample);
            s.setFitness(TSPHelper.getFitness(sample));
            samples.add(s);
        }

        return samples;
    }

    public static List<int[]> toArrays(List<Sample> samples) {
        List<int[]> arrays = new ArrayList<>();

        for (Sample sample : samples) {
            int[] a = sample.getTour();
            arrays.add(a);
        }

        return arrays;
    }

    public static Sample getMinValue (List<Sample>population){
        return Collections.min(population);
    }

    public static double getAvgFitness(List<Sample> samples){
        int sum = 0;
        for(Sample sample : samples){
            sum+=sample.getFitness();
        }
        return (double) sum /samples.size();
    }
}

