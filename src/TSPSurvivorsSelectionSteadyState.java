import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TSPSurvivorsSelectionSteadyState implements ITSPSurvivorSelection, IPrintable{
    private int n;

    public TSPSurvivorsSelectionSteadyState(int n){
        this.n = n;
    }

    @Override
    public List<Sample> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix) {

        // Reemplazar peores n de la poblacion actual con los n mejores de los hijos
        for (int i = 0; i < n; i++ ) {
            population.set(population.size() - i - 1, kids.get(i));
        }

        return population;
    }

    @Override
    public String getStringValue() {
        return "Survivor Selection: Steady State n: " + n;
    }
}
