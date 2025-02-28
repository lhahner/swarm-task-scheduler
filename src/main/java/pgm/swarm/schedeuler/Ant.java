package pgm.swarm.schedeuler;

import java.util.ArrayList;
import java.util.*;

/**
 * This class is representing one Agent of the Ant
 * colony optimization algorithm.
 * 
 * @author lennart
 */
public class Ant implements Agent{
	//stores all the nodes the Ant has visited
	private Map<Integer, Boolean> visitedNode = new HashMap<>();
	
	//current tour of the ant
	private ArrayList<Integer> tour = new ArrayList<>();
	
	//indicates the node the Ant resides
	private int position = 0;
	
	//next postion in the graph
	private int nextpos = 0;
	
	//current pheromone level
	double pheromoneLevel = 0;
	
	//The sum of pheromones of all possible next moves
	double pheromoneSum = 0;
	
	/**
	 * Will calculate the probility of the next node to visit
	 * 
	 * @param pheromoneLevel level of phernome on the graphs edge
	 * @param distance the distance from current to node
	 * @param pheronomeSum all possible phernome values and distances summed
	 * @return
	 */
	public double calcPheronme(double pheromoneLevel, double distance, double pheronomeSum) {
		return ((pheromoneLevel*distance))/(pheronomeSum);
	}
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getNextpos() {
		return nextpos;
	}

	public void setNextpos(int nextpos) {
		this.nextpos = nextpos;
	}
}
