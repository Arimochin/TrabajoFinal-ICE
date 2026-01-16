import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPMatingPoolTournament implements ITSPMatingPool{
    private int k;
    private boolean withReplacement;

    public TSPMatingPoolTournament(int k, boolean withReplacement){
        this.k = k;
        this.withReplacement = withReplacement;
    }

    @Override
    public List<int[]> getMatingPool(List<int[]> population, int[][] matrix) {
        List<int[]> matingPool = new ArrayList<>();

        for (int iteration = 0; iteration < population.size()/2; iteration++) {
            Random source = new Random();
            List<int[]> randomSamples = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                int randomSample = source.nextInt(population.size());
                randomSamples.add(population.get(randomSample));
            }

            int bestFitness = Integer.MAX_VALUE;
            int[] bestSample = new int[0];
            for (int[] sample : randomSamples) {
                int aux = getFitness(sample, matrix);
                if (aux < bestFitness) {
                    bestFitness = aux;
                    bestSample = sample;
                }
            }

            matingPool.add(bestSample);

            if (!withReplacement) {
                // Con reemplazo (vuelve a la poblacion) - No hago nada
                // ---
                // Sin reemplazo, hay que quitarlo de la poblacion
                population.remove(bestSample);
            }
        }

        return matingPool;
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
