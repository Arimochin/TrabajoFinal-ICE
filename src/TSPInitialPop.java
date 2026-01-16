import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TSPInitialPop {

    public static List<int[]> getInitialPop(int[][] matriz){
        List<int[]> population = new ArrayList<>();
        population = generateGreedySubset(matriz);
        population.addAll(generateRandomSubset(matriz));
        printSubsets(population);
        return population;
    }

    private static List<int[]> generateGreedySubset(int[][] matrix) {
        int n = matrix.length;
        int maxSubsetSize = 5;
        int iterations = Math.min(n, maxSubsetSize);
        List<int[]> subset = new ArrayList<>();

        for (int startNode = 0; startNode < iterations; startNode++) {
            int[] tour = new int[n];
            boolean[] visited = new boolean[n];
            int current = startNode;
            tour[0] = current;
            visited[current] = true;

            for (int step = 1; step < n; step++) {
                int nextNode = -1;
                int minDist = Integer.MAX_VALUE;

                for (int candidate = 0; candidate < n; candidate++) {
                    if (!visited[candidate]) {
                        int dist = matrix[current][candidate];
                        if (dist < minDist) {
                            minDist = dist;
                            nextNode = candidate;
                        }
                    }
                }
                tour[step] = nextNode;
                visited[nextNode] = true;
                current = nextNode;
            }
            subset.add(tour);
        }
        return subset;
    }

    private static List<int[]> generateRandomSubset(int[][] matrix) {
        int n = matrix.length;
        int iterations = 45;
        List<int[]> subset = new ArrayList<>();

        for (int iteration = 0; iteration < iterations; iteration++){
            Random source = new Random();
            boolean[] visited = new boolean[n];
            int[] tour = new int[n];

            for (int i = 0; i < n ; i++) {
                boolean stepComplete = false;
                while (!stepComplete) {
                    int randomNode = source.nextInt(n);
                    if (!visited[randomNode]) {
                        tour[i] = randomNode;
                        visited[randomNode] = true;
                        stepComplete = true;
                    }
                }
            }
            subset.add(tour);
        }

        return subset;
    }

    public static void printSubsets(List<int[]> subsets){
        for (int[] a : subsets){
            System.out.println(Arrays.toString(a));
        }
    }
}
