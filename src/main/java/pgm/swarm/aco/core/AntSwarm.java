package pgm.swarm.aco.core;

import java.util.ArrayList;

import pgm.swarm.Swarm;
import pgm.swarm.SwarmFactory;

/**
 * Artifical AntSwarm used for cobinatorical problems
 * and for optimization of those.
 * 
 * @author lennart.hahner
 *
 */
public class AntSwarm implements Swarm<Ant>{

	/**
	 * List of Agents here Ants
	 */
	private ArrayList<Ant> ants;
	
	/**
	 * Best graph representing the best metrics evaluation for the swarm.
	 */
	private double gbest = 10;
	
	/**
	 * Constructor to create a specified amount of 
	 * Ants.
	 * 
	 * @param numberOfAnts Number of Ants that should be constructed
	 */
	public AntSwarm(int numberOfAnts) {
		ants = new ArrayList<Ant>();
		
		SwarmFactory sf = new SwarmFactory();
		
		for(int i = 0; i<numberOfAnts;i++) {
			ants.add((Ant)sf.getAgent("Ant"));
		}
	}
	
	/**
	 * Method return a list of Agents,
	 * in this context the Agents or of Type Ant.
	 * 
	 * @return List of Ant Agents.
	 */
	public ArrayList<Ant> getAgents() {
		return this.ants;
	}

	/**
	 * Return the current global best for the swarm
	 * 
	 * @return global best position
	 */
	public double getGbest() {
		return gbest;
	}

	/**
	 * Will set the gbest array for this swarm.
	 * 
	 * @param gbest The index of the global best position.
	 */
	public void setGbest(double gbest) {
		this.gbest = gbest;
	}

}
