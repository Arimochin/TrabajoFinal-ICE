import java.util.Arrays;

public class Sample implements Comparable<Sample> {
    private int[] tour;
    private int fitness;




    public int[] getTour() {
        return tour;
    }

    public void setTour(int[] tour) {
        this.tour = tour;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }


    @Override
    public int compareTo(Sample o) {
        return Integer.compare(this.fitness, o.getFitness());
    }

    @Override
    public String toString() {
        return Arrays.toString(tour) + " F: " + fitness;
    }
}
