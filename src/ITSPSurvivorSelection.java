import java.util.List;

public interface ITSPSurvivorSelection {


    List<int[]> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix);

}
