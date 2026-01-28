import java.util.List;

public interface ITSPMatingPool {

     List<Sample> getMatingPool(List<Sample> population, int[][] matrix);
}
