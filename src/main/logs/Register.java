package main.logs;

import main.TSP;
import main.eval.TSPGroupEvaluator;

import java.util.Arrays;

public class Register  {
    private static final String OUTPUT_FILE = "output.txt";
    private static final String SEPARATOR = "-----------------------------------------------------------------------------";

    public static void evaluate(TSP tsp){
        writeCommonData(tsp);
        LogWriter.writeInfo(OUTPUT_FILE, SEPARATOR);
    }

    public static  void evaluateGroup(TSPGroupEvaluator groupEvaluator, int groupNumber){

        // Grupo
        LogWriter.writeInfo(OUTPUT_FILE, "Group:  " + groupNumber);
        // Mejor fitness del grupo
        LogWriter.writeInfo(OUTPUT_FILE, "Best fitness: " + groupEvaluator.getBestFitness());
        // Peor fitness del grupo
        LogWriter.writeInfo(OUTPUT_FILE, "Worst fitness: " + groupEvaluator.getWorstFitness());
        // Fitness promedio del grupo
        LogWriter.writeInfo(OUTPUT_FILE, "Average fitness: " + groupEvaluator.getAverageFitness());
        // Desviacion estandar del fitness del grupo
        LogWriter.writeInfo(OUTPUT_FILE, "Standard Deviation: " + groupEvaluator.getDesviationFitness());
        // Tiempo promedio de ejecucion del grupo
        LogWriter.writeInfo(OUTPUT_FILE, "Average execution time(Ms): " + groupEvaluator.getAverageComputingTime() + " ms");

        LogWriter.writeInfo(OUTPUT_FILE, SEPARATOR);
    }

    public static void writeCommonData(TSP tsp){
        // Population size
        LogWriter.writeInfo(OUTPUT_FILE, tsp.getTspInitialPop().getStringValue());

        // Nro de iteraciones
        LogWriter.writeInfo(OUTPUT_FILE, "Iterations number: "+ tsp.getIt());

        // Seleccion de padres
        LogWriter.writeInfo(OUTPUT_FILE, tsp.getParentSelection().getStringValue());

        // Cruce
        LogWriter.writeInfo(OUTPUT_FILE, tsp.getCrossoverOperator().getStringValue() + " Chance: " + tsp.getCrossChance());

        // Mutacion
        LogWriter.writeInfo(OUTPUT_FILE, tsp.getMutationOperator().getStringValue() + " Chance: " + tsp.getMutationChance());

        // Seleccion de sobrevivientes
        LogWriter.writeInfo(OUTPUT_FILE, tsp.getSurvivorSelectionOperator().getStringValue());

        // Mejor fitness en cada iteracion
        LogWriter.writeInfo(OUTPUT_FILE, Arrays.toString(tsp.getBestFitnessHistory()));

        // Mejor solucion lograda
        LogWriter.writeInfo(OUTPUT_FILE, "Best Composition: " + Arrays.toString(tsp.getBestSolution().getTour()));
        LogWriter.writeInfo(OUTPUT_FILE, "Best Fitness: " + tsp.getBestSolution().getFitness());

        //Tiempo de ejecucion

    }

}