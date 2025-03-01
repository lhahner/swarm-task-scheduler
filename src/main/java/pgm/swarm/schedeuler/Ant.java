package pgm.swarm.schedeuler;

import java.util.ArrayList;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.*;

/**
 * This class is representing one Agent of the Ant
 * colony optimization algorithm.
 * 
 * @author lennart
 * @version 1.0.0
 */
public class Ant implements Agent{
	
	//Constant Q
	private final static double Q = 100;
	
	//stores all the nodes the Ant has visited
	private List<Double> trail;
	
	//indicates the node the Ant resides
	private int pos;
	
	//The rate at which the pheromoneLevel decreases over time
	double evaporationRate = 0;
	
	/**
	 * Will calculate the probility of the next node to visit
	 * and then assign the position to that node for the Ant.
	 * 
	 * @param node index so dimension i of adjacent matrix.
	 * @param node array at dimension i, so values j for i.
	 */
	public void calcPossibleNextVisit(int nodeIndex, double[] node) throws IndexOutOfBoundsException {
		int nextPos = -1;
		
		double[] origin_pos = {nodeIndex, nodeIndex};
		double[] target_pos = new double[2];
		double sum = 0;
		target_pos[0] = nodeIndex;
		
		//calc sum for all edges
		for(int i = 0; i<node.length;i++) {	
			target_pos[1] = i;
			double d = new EuclideanDistance().compute(origin_pos, target_pos);
			sum =+ (node[i] * (1/d));
		}
		double probability = 0;
		
		//calc probibility, largest probilitly is next path.
		for(int i = 0; i<node.length;i++) {
			target_pos[1] = i;
			double d = new EuclideanDistance().compute(origin_pos, target_pos);
			if(probability < (node[i] * (1/d)/sum)) {
				probability = (node[i] * (1/d)/sum);
				nextPos = i;
			}
		}
		if(nextPos != -1) {
			this.setPos(nextPos);
		}
		else {
			throw new IndexOutOfBoundsException("next Postion for Ant at node: " + nodeIndex + "not specified, Ant stuck");
		}
	}
	
	/**
	 * Gets the current poistion of the ant
	 * in a graph.
	 * 
	 * @return A Array of i and j values.
	 */
	public int getPos() {
		return pos;
	}
	
	/**
	 * This sets/copies the positions array
	 * with new values.
	 * 
	 * @param position The new position array
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	
}
