import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String filePath1 = "resources/br17.atsp";
        String filePath2 = "resources/p43.atsp";
        int [][] m = ATSPReader.init(filePath2);

        TSP tsp = new TSP();

        tsp.setCrossoverOperator(new TSPCrossoverOperatorPMX());
        tsp.setParentSelection(new TSPMatingPoolTournament(5,false,25));
        tsp.setCrossChance(0.8D);
        tsp.setSurvivorSelection(new TSPSurvivorsSelectionSteadyState(25));
        tsp.setMutationOperator(new TSPMutationOperatorInversion());
        tsp.setMutationChance(0.1);


        tsp.init(m);





    }
}