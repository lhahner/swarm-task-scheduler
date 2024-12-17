package pgm.swarm.schedeuler;

/** 
 * Particle which is part of the Particle-Swarm-Optimization 
 * algorithm.
 * 
 * @version 1.0.0
 * @author Lennart Hahner
 */
public class Particle implements Agent{
	
	/**
	 * Stores the current position of the particle.
	 */
	private double pos;
	
	/**
	 * stores the current velocity of the particle. 
	 */
	private double velo;
	
	/**
	 * Gets the current position of the particle.
	 * 
	 * @return the current position of the particle.
	 */
	public double getPos() {
		return pos;
	}
	
	/**
	 * Get the current velocity of the particle.
	 * 
	 * @return the current velocity of a particle.
	 */
	public double getVelo() {
		return velo;
	}
	
	/**
	 * Sets the current position of the particle.
	 * 
	 * @param the new position
	 */
	public void setPos(double pos) {
		this.pos = pos;
	}
	
	/**
	 * Set the current velocity of the particle.
	 * 
	 * @param the new velocity
	 */
	public void setVelo(double velo) {
		this.velo = velo;
	}
	
	/**
	 * Set the current position by adding the velocity
	 * 
	 * @param cur_pos The initial position of the particle.
	 * @param new_velo The new velocity upon it will change its position.
	 */
	public void calcPos(double cur_pos, double new_velo) {
		this.pos = cur_pos + new_velo;
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
	public void calcVelo(double cur_velo, double c_1, double pos_best, double pos, double c_2, double global_best) {
		long r_1 = (long)Math.random();
		long r_2 = (long)Math.random();
		this.velo = cur_velo + c_1 * r_1 * (pos_best - pos) + c_2 * r_2 * (global_best - pos);
	}
	
	/**
	 * Converts the Object values to a String.
	 * 
	 * @return The Object as a String with its values.
	 */
	@Override
	public String toString() {
		return "Particle{position=" + this.pos + ", velocity=" + this.velo + "}";
	}
}
