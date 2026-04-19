package main.interfaces;

import main.Sample;

import java.util.List;

public interface ITSPSurvivorSelection  extends IPrintable {


    List<Sample> selectSurvivors(List<Sample> population, List<Sample> kids, int[][] matrix);

}
