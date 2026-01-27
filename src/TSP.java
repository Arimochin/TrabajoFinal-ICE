import java.util.*;

public class TSP {
    private static int[] representacion;
    public static int dimension;
    private static int[][] matriz;
    private static List<int[]> population;
    private static List<int[]> matingPool;
    private double crossChance;
    private double mutationChance;

    private ITSPMatingPool parentSelection;
    private ITSPCrossoverOperator crossoverOperator;
    private ITSPMutationOperator mutationOperator;
    private ITSPSurvivorSelection survivorSelectionOperator;


    public void init(int[][] m){
        matriz = m;
        dimension = matriz.length;
        representacion = new int[dimension];

        population = TSPInitialPop.getInitialPop(matriz);
        int i = 0;
        while(i < 150000) {

            matingPool = parentSelection.getMatingPool(population, matriz);

            List<int[]> kids = getMatingResults();

            kids = getMutationResults(kids);

            List<Sample> samplesP = TSPHelper.toSamples(population, matriz);
            Collections.sort(samplesP);

            List<Sample> samplesK = TSPHelper.toSamples(kids, matriz);
            Collections.sort(samplesK);

            List<int[]> survivors = survivorSelectionOperator.selectSurvivors(samplesP, samplesK, matriz);

            population = survivors;
            i++;
        }
        System.out.println("Valor minimo: "+TSPHelper.getMinValue(population,matriz));
        System.out.println(i);
    }

    private List<int[]> getMatingResults(){
        List<int[]> kids = new ArrayList<>();

        while (kids.size() < matingPool.size() * 2) {
            int p1 = (int) (Math.random() * matingPool.size());
            int p2 = (int) (Math.random() * matingPool.size());
            while (p2 ==p1 ){
                p2 = (int) (Math.random() * matingPool.size());
            }

            int[] k1;
            int[] k2;
            if(Math.random() < crossChance) {
                k1 = crossoverOperator.getCrossover(matingPool.get(p1), matingPool.get(p2));
                k2 = crossoverOperator.getCrossover(matingPool.get(p2), matingPool.get(p1));
            } else {
                k1 = matingPool.get(p1);
                k2 = matingPool.get(p2);
            }

            kids.add(k1);
            kids.add(k2);
        }

        return kids;

    }

    private List<int[]> getMutationResults(List<int[]> kids) {
        for (int i = 0; i < kids.size(); i++) {
            if (Math.random() < mutationChance) {
                kids.set(i, mutationOperator.getMutation(kids.get(i)));
            }
        }
        return kids;
    }


    public void setParentSelection(ITSPMatingPool parentSelection) {
        this.parentSelection = parentSelection;
    }

    public void setCrossoverOperator(ITSPCrossoverOperator crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    public void setMutationOperator(ITSPMutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    public void setCrossChance(double crossChance) {
        this.crossChance = crossChance;
    }

    public void setMutationChance(double mutationChance) {
        this.mutationChance = mutationChance;
    }

    public void setSurvivorSelection(ITSPSurvivorSelection survivorSelection) {
        this.survivorSelectionOperator = survivorSelection;
    }
}
