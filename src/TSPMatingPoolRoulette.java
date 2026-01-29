import java.util.*;

public class TSPMatingPoolRoulette implements ITSPMatingPool, IPrintable{
    private double s;

    public TSPMatingPoolRoulette(double factorS) {
        this.s = factorS;
    }

    @Override
    public List<Sample> getMatingPool(List<Sample> population, int[][] matrix) {
        List<Sample> matingPool = new ArrayList<>();


        Collections.sort(population, Collections.reverseOrder());

        double[] prob = getProbLinealMapping(population, s);
        double[] probAcum = getProbAcum(prob);



        for (int iteration = 0; iteration < population.size()/2; iteration++) {
            int selected = spinRoulette(probAcum);
            matingPool.add(population.get(selected));
        }

        return matingPool;
    }

    private double[] getProbLinealMapping(List<Sample> ranking, double s){
        double populationSize = ranking.size();
        double[] prob = new double[(int)populationSize];
        for (int i = 0; i < populationSize; i++) {
            prob[i] = (2-s) / populationSize + (2 * i * (s - 1)) / (populationSize * (populationSize - 1));
            //System.out.println("Sample "+ i + ":" +ranking.get(i) +" "+ prob[i]);
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

    @Override
    public String getStringValue() {
        return "Mating Pool Operator: Roulette "+"Factor S: "+s;
    }


}
