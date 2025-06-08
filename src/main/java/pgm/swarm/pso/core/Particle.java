package pgm.swarm.pso.core;

import pgm.swarm.Agent;

/** 
 * Particle which is part of the Particle-Swarm-Optimization 
 * algorithm. This version of the Particle implements a two-dimensional
 * Array for representing the position of the Ant.
 * 
 * 
 * @version 1.0.0
 * @author Lennart Hahner
 */
public class Particle implements Agent{
	
	/**
	 * Stores the current position of the particle.
	 */
	private double[] pos = new double[2];
	
	/**
	 * stores the current velocity of the particle. 
	 */
	private double[] velo = new double[2];
	
	/**
	 * local best values
	 */
	private double[] pbest = {10, 10};

	/**
	 * Gets the current position of the particle.
	 * 
	 * @return the current position of the particle.
	 */
	public double[] getPos() {
		return this.pos;
	}
	
	/**
	 * Get the current velocity of the particle.
	 * 
	 * @return the current velocity of a particle.
	 */
	public double[] getVelo() {
		return velo;
	}
	
	/**
	 * Sets the current position of the particle.
	 * 
	 * @param the new position
	 */
	public void setPos(double x, double y) {
		this.pos[0] = x;
		this.pos[1] = y;
	}
	
	/**
	 * Set the current velocity of the particle.
	 * 
	 * @param the new velocity
	 */
	public void setVelo(double velo_x, double velo_y) {
		this.velo[0] = velo_x;
		this.velo[1] = velo_y;
	}
	
	/**
	 * Set the x coordinate
	 * @param x the value
	 */
	public void setPosX(double x) {
		this.pos[0] = x;
	}
	
	/**
	 * Set the y coordinate
	 * @param y the value
	 */
	public void setPosY(double y) {
		this.pos[1] = y;
	}
	
	/**
	 * Set the current position by adding the velocity
	 * 
	 * @param cur_pos The initial position of the particle.
	 * @param new_velo The new velocity upon it will change its position.
	 */
	public void calcPos(double[] cur_pos, double[] new_velo) {
		for(int i = 0; i<this.pos.length;i++) {
			this.pos[i] = cur_pos[i] + new_velo[i];
		}
	}
	
	/**
	 * Calculates and gets the new velocity assigned to the particle.
	 * 
	 * @param cur_velo current velocity of the particle.
	 * @param c_1 constant for weighting the values.
	 * @param pos_best possible local best position.
	 * @param pos current position of the particle.
	 * @param c_2 constant for weighting the values.
	 * @param global_best possible global best position. 
	 */
	public void calcVelo(double[] cur_velo, double c_1, double[] pos_best, double[] pos, double c_2, double[] global_best
			,double r_1, double r_2) {
		
		double velo_x = (cur_velo[0] + c_1 * r_1 * (pos_best[0] - pos[0]) + c_2 * r_2 * (global_best[0] - pos[0]));
		double velo_y = (cur_velo[1] + c_1 * r_1 * (pos_best[1] - pos[1]) + c_2 * r_2 * (global_best[1] - pos[1]));
		
		this.setVelo(velo_x, velo_y);
		
	}
	
	/**
	 * Converts the Object values to a String.
	 * 
	 * @return The Object as a String with its values.
	 */
	@Override
	public String toString() {
		return "Particle{position=" + this.pos[0] + "," + this.pos[1] + ",velocity=" + this.velo[0] + "," + this.velo[1] + 
			    ",pbest=" + this.pbest[0] + "," + this.pbest[1] + "} \n";
	}
	
	/**
	 * Gets the current value of pbest.
	 * 
	 * @return the local best position pbest.
	 */
	public double[] getPbest() {
		return pbest;
	}
	
	/**
	 * Sets the values for pbest.
	 * 
	 * @param x The value on the x-axis.
	 * @param y The value on the y-axis.
	 */
	public void setPbest(double[] pbest) {
		System.arraycopy(pbest, 0, this.pbest, 0, this.pbest.length);;
	}
	
	/**
	 * Example of a fitness function. The smaller the returned value, the better.
	 * 
	 * @param positions the current positions of the particle
	 * @return the new positions
	 */
	public double evaluate(double[] positions) {

		double fitness = 0.0;
		for (double pos : positions) {
			fitness += pos * pos;
		}
		return fitness;
	}
}
