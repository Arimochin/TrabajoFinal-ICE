import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSPSurvivorsSelectionSteadyState implements ITSPSurvivorSelection{
    private int n;

    public TSPSurvivorsSelectionSteadyState(int n){
        this.n = n;
    }

    @Override
    public List<int[]> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix) {
        List<int[]> survivors = new ArrayList<>();

        // Reemplazar peores n de la poblacion actual con los n mejores de los hijos
        for (int i = 0; i < n; i++ ) {
            population.set(population.size() - i - 1, kids.get(i));
        }

        for (int i = 0; i < population.size(); i++){
            survivors.add(kids.get(i).getTour());
        }

        return survivors;
    }
}
