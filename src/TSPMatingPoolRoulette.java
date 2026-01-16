import java.util.*;

public class TSPMatingPoolRoulette implements ITSPMatingPool{

    @Override
    public List<int[]> getMatingPool(List<int[]> population, int[][] matrix) {
        List<int[]> matingPool = new ArrayList<>();

        List<Sample> samples = new ArrayList<>();
        for (int[] sample : population) {
            Sample s = new Sample();
            s.setTour(sample);
            s.setFitness(getFitness(sample, matrix));
            samples.add(s);
        }
        Collections.sort(samples, Collections.reverseOrder());

        double[] prob = getProbLinealMapping(samples, 1.5);
        double[] probAcum = getProbAcum(prob);

        System.out.println("Ranking");
        for(Sample s : samples){
            System.out.println(s.toString());
        }
        System.out.println("Probabilidades" + "\n" + Arrays.toString(prob));
        System.out.println("Probabilidades Acumuladas " + "\n" + Arrays.toString(probAcum));

        for (int iteration = 0; iteration < population.size()/2; iteration++) {
            int selected = spinRoulette(probAcum);
            matingPool.add(samples.get(selected).getTour());
        }

        return matingPool;
    }

    private double[] getProbLinealMapping(List<Sample> ranking, double s){
        double populationSize = ranking.size();
        double[] prob = new double[(int)populationSize];
        for (int i = 0; i < populationSize; i++) {
            prob[i] = (2-s) / populationSize + (2 * i * (s - 1)) / (populationSize * (populationSize - 1));
            System.out.println("Sample "+ i + ":" +ranking.get(i) +" "+ prob[i]);
        }
        return prob;
    }

    private double[] getProbAcum(double[] prob){
        double[] probAcum = new double[prob.length];
        probAcum[0] = prob[0];
        for (int i = 1; i < prob.length; i++) {
            probAcum[i] = prob[i] + probAcum[i-1];
        }
        return probAcum;
    }

    private int spinRoulette(double[] probAcum){
        double spin = Math.random();
        for (int i = 0; i < probAcum.length; i++){
            if (spin < probAcum[i]) {
                return i;
            }
        }
        return probAcum.length - 1;
    }

    private int getFitness(int[] sample, int[][] matrix){
        int acum = 0;
        for(int i = 0; i < sample.length-1;i++){
            acum += matrix[sample[i]][sample[i+1]];
        }
        acum+= matrix[sample[sample.length-1]][sample[0]];
        return acum;
    }
}
