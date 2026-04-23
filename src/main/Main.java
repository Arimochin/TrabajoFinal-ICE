package main;

import main.eval.TSPRunner;
import main.init.ATSPReader;
import main.init.TSPBuilder;
import main.logs.LogWriter;
import main.operators.crossover.TSPCrossoverOperatorOrder;
import main.operators.crossover.TSPCrossoverOperatorPMX;
import main.operators.mutation.TSPMutationOperatorInversion;
import main.operators.parents_selection.TSPMatingPoolRoulette;
import main.operators.parents_selection.TSPMatingPoolTournament;
import main.operators.survivors_selection.TSPSurvivorsSelectionElitismX;
import main.operators.survivors_selection.TSPSurvivorsSelectionSteadyState;

import java.util.logging.Logger;

public class Main {

    private static String filePath;
    private static int maxIt;
    private static int samplesPerGroup;

    public static void main(String[] args) {

        if(args.length > 0){
            filePath = args[0];
        } else {
            throw new RuntimeException("Missing file path argument. Please provide the path to the .atsp file as the first argument.");
        }

        maxIt = (args.length > 1) ? Integer.parseInt(args[1]) : 2000;
        samplesPerGroup = (args.length > 2) ? Integer.parseInt(args[2]) : 25;

        int [][] m = ATSPReader.init(filePath);
        TSPBuilder builder = new TSPBuilder();
        TSPRunner runner = new TSPRunner(maxIt, samplesPerGroup, m);

        System.out.println("Running TSP with file: " + filePath + ", max iterations: " + maxIt + ", samples per group: " + samplesPerGroup);

        LogWriter.resetLogFile("output.txt");



            TSP tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolTournament(6, false))
                    .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.7D)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionSteadyState(25))
                    .build();
            runner.runGroup(tsp);


             tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolRoulette(1.5))
                    .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.8)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionElitismX(new TSPMatingPoolRoulette(1.5)))
                    .build();
            runner.runGroup(tsp);




            tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolRoulette(1.5))
                    .setCrossoverOperator(new TSPCrossoverOperatorOrder()).setCrossChance(0.7)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionSteadyState(25))
                    .build();
            runner.runGroup(tsp);




            tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolTournament(3, false))
                    .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.80D)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionElitismX(new TSPMatingPoolTournament(6, true)))
                    .build();
            runner.runGroup(tsp);


            System.out.println("Finished all groups. Check output.txt for results.");

    }
}