package pgm.swarm.schedeuler;

/**
 * Particle which is part of the Particle-Swarm-Optimization 
 * algorithm.
 * 
 * @version 1.0.0
 * @author Lennart Hahner
 */
public class Particle {
	
	/**
	 * Stores the current position of the particle.
	 */
	long pos;
	
	/**
	 * stores the current velocity of the particle. 
	 */
	long velo;
	
	/**
	 * Constructs a Particle with a position an velocity
	 * at the beginning of its lifetime.
	 * 
	 * @param pos
	 * @param velo
	 */
	public Particle(long pos, long velo) {
		if(pos > 0 && velo > 0) {
			this.pos = pos;
			this.velo = velo;
		}
	}
	
	/**
	 * Gets the current position of the particle.
	 * 
	 * @return the current position of the particle.
	 */
	public long getPos() {
		return pos;
	}
	
	/**
	 * Set the current position by adding the velocity
	 * 
	 * @param cur_pos The initial position of the particle.
	 * @param new_velo The new velocity upon it will change its position.
	 */
	public void setPosition(long cur_pos, long new_velo) {
		this.pos = cur_pos + new_velo;
	}
	
	/**
	 * Get the current velocity of the particle.
	 * 
	 * @return the current velocity of a particle.
	 */
	public long getVelo() {
		return velo;
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
	public void setVelo(long cur_velo, long c_1, long pos_best, long pos, long c_2, long global_best) {
		long r_1 = (long)Math.random();
		long r_2 = (long)Math.random();
		this.velo = cur_velo + c_1 * r_1 * (pos_best - pos) + c_2 * r_2 * (global_best - pos);
	}	
}
