import java.util.ArrayList;
import java.util.List;

public class TSPRunner {

    private int iterations;
    private int groupNumber = 0;
    private int samplesPerGroup;
    private int[][] m;

    public TSPRunner(int iterations, int samplesPerGroup, int[][] m) {
        this.samplesPerGroup = samplesPerGroup;
        this.m = m;
        this.iterations = iterations;
    }


    public void run(TSP tsp){
        tsp.init(m, iterations);
        Register.evaluate(tsp);
    }

    public void runGroup(TSP tsp) {
        List<TSPResult> groupResults = new ArrayList<>();

        for (int i = 0; i < samplesPerGroup; i++) {
            tsp.init(m, iterations);

            long startTime = System.currentTimeMillis();
            Register.evaluate(tsp);
            long endTime = System.currentTimeMillis();

            long computationTimeMs = endTime - startTime;
            double finalFitness = tsp.getBestSolution().getFitness();

            groupResults.add(new TSPResult(finalFitness, computationTimeMs));
        }

        Register.evaluateGroup(new TSPGroupEvaluator(groupResults), groupNumber);
        groupNumber++;
    }
}
