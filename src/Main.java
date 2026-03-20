


public class Main {

    private static String filePath1 = "resources/br17.atsp";
    private static String filePath2 = "resources/p43.atsp";


    public static void main(String[] args) {
        int [][] m = ATSPReader.init(filePath2);

        TSPBuilder builder = new TSPBuilder();
        TSPRunner runner = new TSPRunner(2000, 25, m);

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


    }
}