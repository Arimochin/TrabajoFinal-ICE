import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String filePath1 = "resources/br17.atsp";
        String filePath2 = "resources/p43.atsp";
        int [][] m = ATSPReader.init(filePath2);


        TSPBuilder builder = new TSPBuilder();

        LogWriter.resetLogFile("output.txt");


      // TSP tsp = builder.setInitialPop(5,50).setParentSelection(new TSPMatingPoolTournament(5,false,25))
      //         .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.8D)
      //         .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
      //         .setSurvivorSelectionOperator(new TSPSurvivorsSelectionSteadyState(25))
      //         .build();
      // tsp.init(m,2000);
      // Register.evaluate(tsp);

        for(int i = 0; i < 25; i++) {
            TSP tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolTournament(6, false))
                    .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.7D)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionSteadyState(25))
                    .build();
            tsp.init(m, 2000);
            Register.evaluateGroup(tsp,0);
        }

        for(int i = 0; i < 25; i++) {
            TSP tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolRoulette(1.5))
                    .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.8)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionElitismX(new TSPMatingPoolRoulette(1.5)))
                    .build();
            tsp.init(m, 2000);
            Register.evaluateGroup(tsp,1);
        }

        for(int i = 0; i < 25; i++) {
            TSP tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolRoulette(1.5))
                    .setCrossoverOperator(new TSPCrossoverOperatorOrder()).setCrossChance(0.7)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionSteadyState(25))
                    .build();
            tsp.init(m, 2000);
            Register.evaluateGroup(tsp,2);
        }

        for(int i = 0; i < 25; i++) {
            TSP tsp = builder.setInitialPop(5, 50).setParentSelection(new TSPMatingPoolTournament(3, false))
                    .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.80D)
                    .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                    .setSurvivorSelectionOperator(new TSPSurvivorsSelectionElitismX(new TSPMatingPoolTournament(6, true)))
                    .build();
            tsp.init(m, 2000);
            Register.evaluateGroup(tsp,3);
        }


//        for(int i = 0; i < 100; i++){
//            TSP randomTSP = TSPRandomizer.createRandomTSP();
//            randomTSP.init(m,2000);
//            Register.evaluate(randomTSP);
//        }


    }
}