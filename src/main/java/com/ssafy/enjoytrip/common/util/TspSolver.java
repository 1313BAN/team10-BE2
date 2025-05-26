package com.ssafy.enjoytrip.common.util;

import java.util.List;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;

public class TspSolver {

	public static List<Integer> solveTsp(double[][] distanceMatrix) {
		
		int nodeCount = distanceMatrix.length;
		
		// nodeCount, vehicleNumber, depot
		RoutingIndexManager manager = new RoutingIndexManager(nodeCount, 1, 0);
		RoutingModel routing = new RoutingModel(manager);
		
		int transitCallbackIndex = routing.registerTransitCallback(
				(long fromIndex, long toIndex) -> {
				    int fromNode = manager.indexToNode((int) fromIndex);
				    int toNode = manager.indexToNode((int) toIndex);
				    return (long) distanceMatrix[fromNode][toNode];
				}
			);
		
		routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
		
		RoutingSearchParameters searchParameters = 
				main.defaultRoutingSearchParameters()
				.toBuilder()
				.setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
				.build();
		
		Assignment solution = routing.solveWithParameters(searchParameters);
		
		return null;
	}
}
