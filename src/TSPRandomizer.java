import java.util.Random;

public class TSPRandomizer {

    // --- Configuration Placeholders ---

    // Probabilities
    private static final double CROSSOVER_CHANCE_MIN = 0.60;
    private static final double CROSSOVER_CHANCE_MAX = 0.95;
    private static final double MUTATION_CHANCE_MIN = 0.01;
    private static final double MUTATION_CHANCE_MAX = 0.10;

    // Roulette Operator Parameters
    private static final double ROULETTE_FACTOR_S_MIN = 1.0;
    private static final double ROULETTE_FACTOR_S_MAX = 2.0;

    // Tournament Operator Parameters
    private static final int TOURNAMENT_K_MIN = 2;
    private static final int TOURNAMENT_K_MAX = 6;
    private static final int TOURNAMENT_N_MIN = 10;
    private static final int TOURNAMENT_N_MAX = 100;

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

        builder.setCrossChance(getRandomDouble(CROSSOVER_CHANCE_MIN, CROSSOVER_CHANCE_MAX));
        builder.setMutationChance(getRandomDouble(MUTATION_CHANCE_MIN, MUTATION_CHANCE_MAX));

        return builder.build();
    }

    private static ITSPCrossoverOperator getRandomCrossover() {
        if (random.nextBoolean()) {
            return new TSPCrossoverOperatorOrder();
        }
        return new TSPCrossoverOperatorPMX();
    }

    private static ITSPMutationOperator getRandomMutation() {
        if (random.nextBoolean()) {
            return new TSPMutationOperatorInsertion();
        }
        return new TSPMutationOperatorInversion();
    }

    private static ITSPMatingPool getRandomMatingPool() {
        if (random.nextBoolean()) {
            double s = getRandomDouble(ROULETTE_FACTOR_S_MIN, ROULETTE_FACTOR_S_MAX);
            return new TSPMatingPoolRoulette(s);
        } else {
            int k = getRandomInt(TOURNAMENT_K_MIN, TOURNAMENT_K_MAX);

            boolean replacement = random.nextBoolean();
            return new TSPMatingPoolTournament(k, replacement, n);
        }
    }

    private static ITSPSurvivorSelection getRandomSurvivorSelection() {
        if (random.nextBoolean()) {
            int n = getRandomInt(STEADY_STATE_REPLACEMENT_MIN, STEADY_STATE_REPLACEMENT_MAX);
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
}