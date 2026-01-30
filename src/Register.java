import java.util.Arrays;

public class Register  {
    public static  void evaluate(TSP tsp){

        // LogWriter.writeInfo("results.txt", );

        // Popuation size
        LogWriter.writeInfo("results.txt", tsp.getTspInitialPop().getStringValue());

        // Nro de iteraciones
        LogWriter.writeInfo("results.txt", "Iterations number: "+ tsp.getIt());

        // Seleccion de padres
        LogWriter.writeInfo("results.txt", tsp.getParentSelection().getStringValue());

        // Cruce
        LogWriter.writeInfo("results.txt", tsp.getCrossoverOperator().getStringValue() + " Chance: " + tsp.getCrossChance());

        // Mutacion
        LogWriter.writeInfo("results.txt", tsp.getMutationOperator().getStringValue() + " Chance: " + tsp.getMutationChance());

        // Seleccion de sobrevivientes
        LogWriter.writeInfo("results.txt", tsp.getSurvivorSelectionOperator().getStringValue());

        // Mejor fitness en cada iteracion
        LogWriter.writeInfo("results.txt", Arrays.toString(tsp.getBestFitnessHistory()));

        // Mejor solucion lograda
        LogWriter.writeInfo("results.txt", "Best Composition: " + Arrays.toString(tsp.getBestSolution().getTour()));
        LogWriter.writeInfo("results.txt", "Best Fitness: " + tsp.getBestSolution().getFitness());

        // Tiempo de ejecucion


        // Separador
        LogWriter.writeInfo("results.txt", "-----------------------------------------------------------------------------");

    }
}