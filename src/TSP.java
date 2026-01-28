import java.util.*;

public class TSP {
    private static int[] representacion;
    public static int dimension;
    private static int[][] matriz;
    private static List<Sample> population;
    private static List<Sample> matingPool;
    private double crossChance;
    private double mutationChance;

    private ITSPMatingPool parentSelection;
    private ITSPCrossoverOperator crossoverOperator;
    private ITSPMutationOperator mutationOperator;
    private ITSPSurvivorSelection survivorSelectionOperator;


    public void init(int[][] m){
        matriz = m;
        TSPHelper.setMatrix(matriz);
        dimension = matriz.length;
        representacion = new int[dimension];

        population = TSPInitialPop.getInitialPop(matriz, 5, 45);
        int i = 0;
        while(i < 150000) {
            // Seleccion de Padres
            matingPool = parentSelection.getMatingPool(population, matriz);

            // Cruce
            List<Sample> kids = getMatingResults();

            // Mutacion
            kids = getMutationResults(kids);

            //List<Sample> samplesP = TSPHelper.toSamples(population, matriz);
            Collections.sort(population);

            //List<Sample> samplesK = TSPHelper.toSamples(kids, matriz);
            Collections.sort(kids);

            population = survivorSelectionOperator.selectSurvivors(population, kids, matriz);
            i++;
        }
        System.out.println("Valor minimo: "+TSPHelper.getMinValue(population, matriz));
        System.out.println(i);
    }

    private List<Sample> getMatingResults(){
        List<Sample> kids = new ArrayList<>();

        while (kids.size() < matingPool.size() * 2) {
            int p1 = (int) (Math.random() * matingPool.size());
            int p2 = (int) (Math.random() * matingPool.size());
            while (p2 ==p1 ){
                p2 = (int) (Math.random() * matingPool.size());
            }

            int[] k1;
            int[] k2;
            if(Math.random() < crossChance) {
                k1 = crossoverOperator.getCrossover(matingPool.get(p1).getTour(), matingPool.get(p2).getTour());
                k2 = crossoverOperator.getCrossover(matingPool.get(p2).getTour(), matingPool.get(p1).getTour());
            } else {
                k1 = matingPool.get(p1).getTour();
                k2 = matingPool.get(p2).getTour();
            }

            Sample sk1 = new Sample();
            Sample sk2 = new Sample();
            sk1.setTour(k1);
            sk2.setTour(k2);
            kids.add(sk1);
            kids.add(sk2);
        }

        return kids;

    }

    private List<Sample> getMutationResults(List<Sample> kids) {
        for (int i = 0; i < kids.size(); i++) {
            if (Math.random() < mutationChance) {
                Sample s = new Sample();
                s.setTour(mutationOperator.getMutation(kids.get(i).getTour()));
                kids.set(i, s);
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
