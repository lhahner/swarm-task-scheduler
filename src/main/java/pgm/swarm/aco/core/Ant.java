package pgm.swarm.aco.core;

import java.util.ArrayList;
import java.util.List;

import pgm.swarm.Agent;

/**
 * This class is representing one Agent of the Ant
 * colony optimization algorithm.
 * 
 * @author lennart
 * @version 1.0.0
 */
public class Ant implements Agent{
	
	/**
	 * stores all the nodes the Ant has visited
	 */
	private List<ArrayList<Integer>> trail = new ArrayList<ArrayList<Integer>>();
	
	/**
	 * indicates the node the Ant resides
	 */
	private int pos;

	/**
	 * The rate at which the pheromoneLevel decreases over time
	 */
	double evaporationRate = 0.5;
	
	/**
	 * Will calculate the probility of the next node to visit
	 * and then assign the position to that node for the Ant.
	 * 
	 * @param node index so dimension i of adjacent matrix.
	 * @param node array at dimension i, so values j for i.
	 */
	public void calcPossibleNextVisit(int nodeIndex, double[][] node) throws IndexOutOfBoundsException {
		int edge = -1;
		double[] possibleEdge = new double[2];
		double sum = 0, probability = 0;

		possibleEdge[0] = nodeIndex;
		
		for(int i = 0; i<node.length;i++) {	
			possibleEdge[1] = i;
			double d = node[i][0];
			sum =+ (node[i][1] * (1/d));
		}
		
		//calc probibility, largest probilitly is next path.
		for(int i = 0; i<node.length;i++) {
			possibleEdge[1] = i;
			double d = node[i][0];
			if(probability < (node[i][1] * (1/d)/sum)) {
				probability = (node[i][1] * (1/d)/sum);
				edge = i;
			}
		}
		if(edge != -1) {
			this.setPos(edge);
		}
		else {
			throw new IndexOutOfBoundsException("next Postion for Ant at node: " + nodeIndex + "not specified, Ant stuck");
		}
	}
	
	/**
	 * This will return a new Pheronome value which can be assigned
	 * to a path. It will ensure that a ant does not traverse multiple
	 * paths twice since value will increase whenever the ant has
	 * vistied.
	 * 
	 * @param phernomeValue The phernome of the visited edge 
	 * @param Q constant Q to scale the evaporation rate
	 * @return the new phernome value
	 */
	public double updatePheronome(double phernomeValue, double Q) {
		double trail_length = 1;
		
		if(!trail.isEmpty()) {
			for(ArrayList<Integer> p : this.trail)
				trail_length += p.get(0);
		}
		return (1-evaporationRate)*phernomeValue+(Q/trail_length);
	}
	
	/**
	 * This will return a new Pheronome value which can be assigned
	 * to a path. It will ensure that a ant does not traverse multiple
	 * paths twice since value will increase whenever the ant has
	 * vistied.
	 * 
	 * @verison 1.0.1
	 * @param phernomeValue The phernome of the visited edge 
	 * @param Q constant Q to scale the evaporation rate
	 * @return the new phernome value
	 */
	public double updatePheronome(double phernomeValue, double Q, double[][][] graph) {
		double trail_length = 1;
		
		if(!trail.isEmpty()) {
			for(ArrayList<Integer> p : this.trail)
				trail_length += graph[p.get(0)][p.get(1)][1];
		}
		return (1-evaporationRate)*phernomeValue+(Q/trail_length);
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
	
	/**
	 * Add edge to trail which the ant 
	 * has already visteded and will not visit in future
	 * 
	 * @param i
	 * @param j
	 */
	public void addToTrail(int i, int j) {
		ArrayList<Integer> edge = new ArrayList<Integer>();
		edge.add(i);
		edge.add(j);
		this.trail.add(edge);
	}
	
	/**
	 * Removes all visited nodes from the list
	 * and clears the tour.
	 */
	public void clearTrail() {
		this.trail.clear();
	}	
	
	/**
	 * Removes all visited nodes from the list
	 * and clears the tour.
	 */
	public List<ArrayList<Integer>> getTrail() {
		return this.trail;
	}	
}
