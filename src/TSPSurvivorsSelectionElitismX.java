import java.util.ArrayList;
import java.util.List;

public class TSPSurvivorsSelectionElitismX implements ITSPSurvivorSelection {
    private ITSPMatingPool secondMethod;

    public TSPSurvivorsSelectionElitismX (ITSPMatingPool secondMethod) {
        this.secondMethod = secondMethod;
    }


    @Override
    public List<int[]> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix) {
        int[] bestPopulationSample = population.removeFirst().getTour();
        int[] bestKidsSample = kids.removeFirst().getTour();

        List<int[]> participants = new ArrayList<>();
        participants.addAll(TSPHelper.toArrays(population));
        participants.addAll(TSPHelper.toArrays(kids));

        List<int[]> selectedSurvivors = secondMethod.getMatingPool(participants,matrix);

        selectedSurvivors.add(bestKidsSample);
        selectedSurvivors.add(bestPopulationSample);

        return selectedSurvivors;
    }
}
