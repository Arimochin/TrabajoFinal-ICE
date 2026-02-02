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


        TSP tsp = builder.setInitialPop(5,50).setParentSelection(new TSPMatingPoolTournament(5,false,25))
                .setCrossoverOperator(new TSPCrossoverOperatorPMX()).setCrossChance(0.8D)
                .setMutationOperator(new TSPMutationOperatorInversion()).setMutationChance(0.1)
                .setSurvivorSelectionOperator(new TSPSurvivorsSelectionSteadyState(25))
                .build();
        tsp.init(m,10000);
        Register.evaluate(tsp);


        for(int i = 0; i < 10; i++){
            TSP randomTSP = TSPRandomizer.createRandomTSP();
            randomTSP.init(m,10000);
            Register.evaluate(randomTSP);
        }


    }
}