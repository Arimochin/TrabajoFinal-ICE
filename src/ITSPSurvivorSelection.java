import java.util.List;

public interface ITSPSurvivorSelection {


    List<Sample> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix);

}
