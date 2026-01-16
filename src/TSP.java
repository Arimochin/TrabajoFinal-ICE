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
    }





}
