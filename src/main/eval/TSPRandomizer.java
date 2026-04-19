package main.eval;

import main.TSP;
import main.init.TSPBuilder;
import main.interfaces.ITSPCrossoverOperator;
import main.interfaces.ITSPMatingPool;
import main.interfaces.ITSPMutationOperator;
import main.interfaces.ITSPSurvivorSelection;
import main.operators.crossover.TSPCrossoverOperatorOrder;
import main.operators.crossover.TSPCrossoverOperatorPMX;
import main.operators.mutation.TSPMutationOperatorInsertion;
import main.operators.mutation.TSPMutationOperatorInversion;
import main.operators.parents_selection.TSPMatingPoolRoulette;
import main.operators.parents_selection.TSPMatingPoolTournament;
import main.operators.survivors_selection.TSPSurvivorsSelectionElitismX;
import main.operators.survivors_selection.TSPSurvivorsSelectionSteadyState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class TSPRandomizer {

    // --- Configuration Placeholders ---

    // Probabilities
    private static final double CROSSOVER_CHANCE_MIN = 0.60;
    private static final double CROSSOVER_CHANCE_MAX = 0.95;
    private static final double MUTATION_CHANCE_MIN = 0.01;
    private static final double MUTATION_CHANCE_MAX = 0.10;

    private static final double CROSSOVER_CHANCE_1 = 0.8;
    private static final double CROSSOVER_CHANCE_2 = 0.7;
    private static final double MUTATION_CHANCE_1 = 0.1;
    private static final double MUTATION_CHANCE_2 = 0.05;

    // Roulette Operator Parameters
    private static final double ROULETTE_FACTOR_S_MIN = 1.0;
    private static final double ROULETTE_FACTOR_S_MAX = 2.0;

    // Tournament Operator Parameters
    private static final int TOURNAMENT_K_MIN = 2;
    private static final int TOURNAMENT_K_MAX = 6;

    private static final int n = 25;

    // Steady State Survivor Parameters
    private static final int STEADY_STATE_REPLACEMENT_MIN = 1;
    private static final int STEADY_STATE_REPLACEMENT_MAX = 10;


    // ----------------------------------

    private static final Random random = new Random();

    public static TSP createRandomTSP() {
        TSPBuilder builder = new TSPBuilder();
        builder.setInitialPop(5,50);
        builder.setCrossoverOperator(getRandomCrossover());
        builder.setMutationOperator(getRandomMutation());
        builder.setParentSelection(getRandomMatingPool());
        builder.setSurvivorSelectionOperator(getRandomSurvivorSelection());

//        BigDecimal crossChance = BigDecimal.valueOf(getRandomDouble(CROSSOVER_CHANCE_MIN, CROSSOVER_CHANCE_MAX)).setScale(2, RoundingMode.DOWN);
//        BigDecimal mutationChance = BigDecimal.valueOf(getRandomDouble(MUTATION_CHANCE_MIN, MUTATION_CHANCE_MAX)).setScale(2, RoundingMode.DOWN);

        BigDecimal crossChance = BigDecimal.valueOf(getRandomBetween2(CROSSOVER_CHANCE_1, CROSSOVER_CHANCE_2));
        BigDecimal mutationChance = BigDecimal.valueOf(getRandomBetween2(MUTATION_CHANCE_1 , MUTATION_CHANCE_2));

        builder.setCrossChance(crossChance.doubleValue());
        builder.setMutationChance(mutationChance.doubleValue());

        return builder.build();
    }

    private static ITSPCrossoverOperator getRandomCrossover() {
        if (random.nextBoolean()) {
            // Order
            return new TSPCrossoverOperatorOrder();
        }
        // PMX
        return new TSPCrossoverOperatorPMX();
    }

    private static ITSPMutationOperator getRandomMutation() {
        if (random.nextBoolean()) {
            // Insertion
            return new TSPMutationOperatorInsertion();
        }
        // Invertion
        return new TSPMutationOperatorInversion();
    }

    private static ITSPMatingPool getRandomMatingPool() {
        if (random.nextBoolean()) {
            // Roulette
            BigDecimal sFactor = BigDecimal.valueOf(getRandomDouble(ROULETTE_FACTOR_S_MIN, ROULETTE_FACTOR_S_MAX)).setScale(1, RoundingMode.DOWN);
            return new TSPMatingPoolRoulette(sFactor.doubleValue());
        } else {
            // Tournament
            int k = getRandomInt(TOURNAMENT_K_MIN, TOURNAMENT_K_MAX);
            //int k = getRandomBetween2(2, 4);
            boolean replacement = random.nextBoolean();
            return new TSPMatingPoolTournament(k, replacement);
        }
    }

    private static ITSPSurvivorSelection getRandomSurvivorSelection() {
        if (random.nextBoolean()) {
            // Steady State
            return new TSPSurvivorsSelectionSteadyState(n);
        } else {
            // Recursive call to get a random mating pool for ElitismX
            return new TSPSurvivorsSelectionElitismX(getRandomMatingPool());
        }
    }

    private static double getRandomDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    private static int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private static double getRandomBetween2(double one, double two){
        double random = Math.random();
        if (random < 0.5) {
            return one;
        } else {
            return two;
        }
    }

    private static int getRandomBetween2(int one, int two){
        double random = Math.random();
        if (random < 0.5) {
            return one;
        } else {
            return two;
        }
    }
}