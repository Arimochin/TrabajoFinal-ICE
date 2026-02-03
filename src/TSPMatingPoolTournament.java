import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPMatingPoolTournament implements ITSPMatingPool, IPrintable{
    private int k;
    private boolean withReplacement;
    private int n;

    public TSPMatingPoolTournament(int k, boolean withReplacement, int n){
        this.k = k;
        this.withReplacement = withReplacement;
        this.n = n;
    }

    @Override
    public List<Sample> getMatingPool(List<Sample> population, int[][] matrix) {
        List<Sample> matingPool = new ArrayList<>();
        List<Sample> populationForTournament = new ArrayList<>(population);

        for (int iteration = 0; iteration < n; iteration++) {
            Random source = new Random();
            List<Sample> randomSamples = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                int randomSample = source.nextInt(populationForTournament.size());
                randomSamples.add(populationForTournament.get(randomSample));
            }

            int bestFitness = Integer.MAX_VALUE;
            Sample bestSample = new Sample();
            for (Sample sample : randomSamples) {
                int aux = getFitness(sample.getTour(), matrix);
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
                populationForTournament.remove(bestSample);
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

    @Override
    public String getStringValue() {
        return "Mating Pool Operator: Tournament " +"k: "+ k + " n: " + n+" withReplacement: "+withReplacement;
    }
}
