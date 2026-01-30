public class TSPBuilder {

    private ITSPMatingPool parentSelection;
    private ITSPCrossoverOperator crossoverOperator;
    private ITSPMutationOperator mutationOperator;
    private ITSPSurvivorSelection survivorSelectionOperator;
    private double crossChance;
    private double mutationChance;

    public TSPBuilder() {
    }

    public TSPBuilder setParentSelection(ITSPMatingPool parentSelection) {
        this.parentSelection = parentSelection;
        return this;
    }

    public TSPBuilder setCrossoverOperator(ITSPCrossoverOperator crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
        return this;
    }

    public TSPBuilder setMutationOperator(ITSPMutationOperator mutationOperator) {
        this.mutationOperator = mutationOperator;
        return this;
    }

    public TSPBuilder setSurvivorSelectionOperator(ITSPSurvivorSelection survivorSelectionOperator) {
        this.survivorSelectionOperator = survivorSelectionOperator;
        return this;
    }
    public TSPBuilder setCrossChance(double crossChance) {
        this.crossChance = crossChance;
        return this;
    }
    public TSPBuilder setMutationChance(double mutationChance) {
        this.mutationChance = mutationChance;
        return this;
    }


    public TSP build() {
        TSP tsp = new TSP(parentSelection, crossoverOperator, mutationOperator, survivorSelectionOperator, crossChance, mutationChance);
        return tsp;
    }
}
