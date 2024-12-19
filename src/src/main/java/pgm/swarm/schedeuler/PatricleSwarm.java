package pgm.swarm.schedeuler;

import java.util.ArrayList;

public class PatricleSwarm {
	
	/**
	 * This Array stores all the particles of the swarm.
	 */
	private ArrayList<Particle> particles;

	/**
	 * Sets the swarm of particles 
	 * 
	 * 
	 * @param start_x x position to start.
	 * @param start_y y position to start.
	 * @param size the size of the array.
	 */
	public void setParticles(double start_x, double start_y, int size) {

		particles = new ArrayList<Particle>();
		
		for (int i = 0; size > i; i++) {

			start_x = Math.sqrt(start_x);
			start_y = Math.sqrt(start_y);

			particles.add((Particle) new ParticleFactory().getParticle(start_x, start_y, Math.random()));
		}
	}
	
	/**
	 * Provides the Particle Swarm.
	 * 
	 * @return The swarm of particles.
	 */
	public ArrayList<Particle> getParticles(){
		return this.particles;
	}
	
	/**
	 * Returns the complete swarm and its values as a String.
	 */
	@Override
	public String toString() {
		String swarm_str = "\n";
		
		for(Particle particle : this.getParticles()) {
			swarm_str = swarm_str + particle.toString();
		}
		
		return swarm_str;
	}
}
