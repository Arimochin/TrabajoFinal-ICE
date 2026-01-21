import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TSP {
    private static int[] representacion;
    public static int dimension;
    private static int[][] matriz;
    private static List<int[]> population;
    private static List<int[]> matingPool;




    public static void init(int[][] m){
        matriz = m;
        dimension = matriz.length;
        representacion = new int[dimension];
        population = TSPInitialPop.getInitialPop(matriz);

//        ITSPMatingPool parentSelection = new TSPMatingPoolTournament(2, true);
//        List<int[]> matingPool = parentSelection.getMatingPool(population, matriz);
//        System.out.println("-----------------------------");
//        TSPInitialPop.printSubsets(matingPool);


        ITSPMatingPool parentSelection = new TSPMatingPoolRoulette();
        List<int[]> matingPool = parentSelection.getMatingPool(population, matriz);
        System.out.println("-----------------------------");
        TSPInitialPop.printSubsets(matingPool);

        int[] p1 = matingPool.getFirst();
        int[] p2 = matingPool.getLast();
        matingPool.getLast();

        System.out.println("parent 1: " + Arrays.toString(p1));
        System.out.println("parent 2: " + Arrays.toString(p2));


        ITSPCrossoverOperator op = new TSPCrossoverOperatorPMX();
        System.out.println("children: "+Arrays.toString(op.getCrossover(p1, p2)));

        ITSPMutationOperator mt = new TSPMutationOperatorInsertion();
        System.out.println("resultado Mutacion: " + Arrays.toString(mt.getMutation(p1)));
    }





}
