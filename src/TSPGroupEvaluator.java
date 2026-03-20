import java.util.List;

public class TSPGroupEvaluator {

    private double averageFitness;
    private double bestFitness;
    private double worstFitness;
    private double desviationFitness;
    private double averageComputingTime;

    public TSPGroupEvaluator(List<TSPResult> results) {
        averageFitness = getAverageFitness(results);
        bestFitness = getBestFitness(results);
        worstFitness = getWorstFitness(results);
        desviationFitness = getDesviationFitness(results);
        averageComputingTime = getAverageComputingTime(results);
    }



    private double getAverageFitness(List<TSPResult> results) {
        double acum = 0;
        for (TSPResult result : results) {
            acum += result.bestFitness();
        }
        return acum / results.size();
    }

    private double getBestFitness(List<TSPResult> results) {
        double best = Double.MAX_VALUE;
        for (TSPResult result : results) {
            if (result.bestFitness() < best) {
                best = result.bestFitness();
            }
        }
        return best;
    }

    private double getWorstFitness(List<TSPResult> results) {
        double worst = Double.MIN_VALUE;
        for (TSPResult result : results) {
            if (result.bestFitness() > worst) {
                worst = result.bestFitness();
            }
        }
        return worst;
    }

    private double getDesviationFitness(List<TSPResult> results) {
        double acum = 0;
        for (TSPResult result : results) {
            acum += Math.pow(result.bestFitness() - averageFitness, 2);
        }
        return Math.sqrt(acum / results.size());
    }

    private double getAverageComputingTime(List<TSPResult> results) {
        double acum = 0;
        for (TSPResult result : results) {
            acum += result.computationTimeMs();
        }
        return acum / results.size();
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public double getWorstFitness() {
        return worstFitness;
    }

    public double getDesviationFitness() {
        return desviationFitness;
    }

    public double getAverageComputingTime() {
        return averageComputingTime;
    }








}
