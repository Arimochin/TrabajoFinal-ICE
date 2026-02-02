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

        List<Sample> selectedSurvivors = secondMethod.getMatingPool(participants,matrix);

        selectedSurvivors.add(bestKidsSample);
        selectedSurvivors.add(bestPopulationSample);

        return selectedSurvivors;
    }

    @Override
    public String getStringValue() {
        return "Survivor Selection: ElitismX" + " second method: " + secondMethod.getStringValue();
    }
}
