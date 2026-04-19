package main.init;

import main.Sample;
import main.interfaces.IPrintable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TSPInitialPop implements IPrintable {
    private final int goodSolutions;
    private final int randomSolutions;


    TSPInitialPop (int goodSolutions, int populationSize){
        this.goodSolutions = goodSolutions;
        this.randomSolutions = populationSize-goodSolutions;
    }

    public List<Sample> getInitialPop(int[][] matriz){
        List<Sample> population = new ArrayList<>();
        population = generateGreedySubset(matriz, goodSolutions);
        population.addAll(generateRandomSubset(matriz,randomSolutions));
        //printSubsets(population);
        return population;
    }

    private static List<Sample> generateGreedySubset(int[][] matrix, int goodSolutions) {
        int n = matrix.length;
        int iterations = Math.min(n, goodSolutions);
        List<Sample> subset = new ArrayList<>();

        for (int startNode = 0; startNode < iterations; startNode++) {
            int[] tour = new int[n];
            boolean[] visited = new boolean[n];
            int current = startNode;
            tour[0] = current;
            visited[current] = true;
            Sample s = new Sample();

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

                s.setTour(tour);
            }
            subset.add(s);
        }
        return subset;
    }

    private static List<Sample> generateRandomSubset(int[][] matrix, int randomSolutions) {
        int n = matrix.length;
        List<Sample> subset = new ArrayList<>();

        for (int iteration = 0; iteration < randomSolutions; iteration++){
            Random source = new Random();
            boolean[] visited = new boolean[n];
            int[] tour = new int[n];
            Sample s = new Sample();

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
            s.setTour(tour);
            subset.add(s);
        }

        return subset;
    }

    public static void printSubsets(List<Sample> subsets){
        for (Sample a : subsets){
            System.out.println(Arrays.toString(a.getTour()));
        }
    }

    @Override
    public String getStringValue() {
        return "Initial Population: " + (goodSolutions + randomSolutions) + "  Greedy initial solutions number: " + goodSolutions;
    }
}
