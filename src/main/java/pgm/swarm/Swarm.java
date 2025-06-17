package pgm.swarm;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;

import pgm.swarm.pso.core.Particle;

import java.lang.reflect.Type;

public class Swarm<T extends Agent> implements Iterable{

	private int dimension;
	private Class<T> clazz;
	private ArrayList<T> agents;
	private double[] gbests = {10,10};
	private double gbest = 10;
	
	public Swarm(int size, Class<T> clazz) {
		this.clazz = clazz;
		agents = new ArrayList<>();
		
		for(int i = 0; i<size;i++) {
			try {
				agents.add((T) new SwarmFactory<T>().getAgent(this.clazz));
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Swarm(double start_x, double start_y, int size, Class<T> clazz) {
		this.clazz = clazz;
		agents = new ArrayList<T>();
		for (int i = 0; size > i; i++) {

			start_x = Math.sqrt(start_x);
			start_y = Math.sqrt(start_y);

			agents.add((T) new SwarmFactory().getAgent(this.clazz));
		}
	}
	
	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public void setAgents(ArrayList<T> agents) {
		this.agents = agents;
	}
	
	public ArrayList<T> getAgents(){
		return this.agents;
	}
	
	/**
	 * Sets the global optimum
	 */
	public void setGbests(double[] gbest) {
		System.arraycopy(gbest, 0, this.gbest, 0, this.gbests.length);
	}
	
	/**
	 * gets the global best
	 */
	public double[] getGbests() {
		return this.gbests;
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
	
	/**
	 * Returns the complete swarm and its values as a String.
	 */
	@Override
	public String toString() {
		String swarm_str = "\n";
		
		for(T particle : this.getAgents()) {
			swarm_str = swarm_str + particle.toString();
		}
		
		return swarm_str;
	}

	@Override
	public Iterator iterator() {
		return agents.iterator();
	}
}
