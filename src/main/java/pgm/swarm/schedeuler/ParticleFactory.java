package pgm.swarm.schedeuler;

/**
 * Particle Factory to create new Particles.
 * 
 * @author Lennart Hahner
 */
public class ParticleFactory {
	
	/**
	 * Produces Particle with certain values.
	 * 
	 * @param pos initial position of the Agent/Particle.
	 * @param velo initial velocity of the Agent/Particle.
	 * @return a new Particle.
	 */
	public Agent getParticle(double x, double y, double velo_x, double velo_y) {
		if((x >= 0 && y >= 0) && (velo_x >= 0 && velo_y >= 0)) {
			Particle particle = new Particle();
			particle.setPos(x, y);
			particle.setVelo(velo_x, velo_y);
			return particle;
		}
		return null;
	}
}
