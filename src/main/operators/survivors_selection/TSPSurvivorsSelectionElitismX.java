package main.operators.survivors_selection;

import main.Sample;
import main.interfaces.IPrintable;
import main.interfaces.ITSPMatingPool;
import main.interfaces.ITSPSurvivorSelection;

import java.util.ArrayList;
import java.util.List;

public class TSPSurvivorsSelectionElitismX implements ITSPSurvivorSelection, IPrintable {
    private ITSPMatingPool secondMethod;

    public TSPSurvivorsSelectionElitismX (ITSPMatingPool secondMethod) {
        this.secondMethod = secondMethod;
    }


    @Override
    public List<Sample> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix) {
        Sample bestPopulationSample = population.removeFirst();
        Sample bestKidsSample = kids.removeFirst();

        List<Sample> participants = new ArrayList<>();
        participants.addAll(population);
        participants.addAll(kids);
        System.out.println("Tamaño de los participantes para la seleccion de sobrevivientes: "+ participants.size());

        int originalSize = population.size() + 1;
        int neededSecondMethod = (participants.size() / 2) - 1;
        List<Sample> selectedSurvivors = secondMethod.getMatingPool(participants,matrix, neededSecondMethod);

        selectedSurvivors.add(bestKidsSample);
        selectedSurvivors.add(bestPopulationSample);

        return selectedSurvivors;
    }

    @Override
    public String getStringValue() {
        return "Survivor Selection: ElitismX" + " second method: " + secondMethod.getStringValue();
    }
}
