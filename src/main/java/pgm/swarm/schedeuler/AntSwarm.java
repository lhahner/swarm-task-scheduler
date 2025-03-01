package pgm.swarm.schedeuler;

import java.util.ArrayList;

public class AntSwarm implements Swarm<Ant>{

	/**
	 * List of Agents here Ants
	 */
	private ArrayList<Ant> ants;
	
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
	@Override
	public ArrayList<Ant> getAgents() {
		return this.ants;
	}

}
