package main.interfaces;

import main.Sample;

import java.util.List;

public interface ITSPMatingPool extends IPrintable {

     List<Sample> getMatingPool(List<Sample> population, int[][] matrix, int n);
}
