import java.util.Arrays;

public class Register  {
    public static  void evaluate(TSP tsp){

        // LogWriter.writeInfo("results.txt", );

        // Popuation size
        LogWriter.writeInfo("output.txt", tsp.getTspInitialPop().getStringValue());

        // Nro de iteraciones
        LogWriter.writeInfo("output.txt", "Iterations number: "+ tsp.getIt());

        // Seleccion de padres
        LogWriter.writeInfo("output.txt", tsp.getParentSelection().getStringValue());

        // Cruce
        LogWriter.writeInfo("output.txt", tsp.getCrossoverOperator().getStringValue() + " Chance: " + tsp.getCrossChance());

        // Mutacion
        LogWriter.writeInfo("output.txt", tsp.getMutationOperator().getStringValue() + " Chance: " + tsp.getMutationChance());

        // Seleccion de sobrevivientes
        LogWriter.writeInfo("output.txt", tsp.getSurvivorSelectionOperator().getStringValue());

        // Mejor fitness en cada iteracion
        LogWriter.writeInfo("output.txt", Arrays.toString(tsp.getBestFitnessHistory()));

        // Mejor solucion lograda
        LogWriter.writeInfo("output.txt", "Best Composition: " + Arrays.toString(tsp.getBestSolution().getTour()));
        LogWriter.writeInfo("output.txt", "Best Fitness: " + tsp.getBestSolution().getFitness());

        // Tiempo de ejecucion


        // Separador
        LogWriter.writeInfo("output.txt", "-----------------------------------------------------------------------------");

    }
}