package pgm.swarm;

import pgm.swarm.aco.core.Ant;
import pgm.swarm.pso.core.Particle;

/**
 * This factory will generate the Agents necessary for a
 * Swarm. So whenever a swarm is made up, this method
 * should be called to produce a swarm upon the required
 * agents.
 * 
 * @author Lennart Hahner
 * @version 1.1.0
 */
public class SwarmFactory <T> {
	
	/**
	 * This method is especially used to create
	 * an Agent of the type particle, in the current
	 *
	 * @param positionAtX,positionAtY initial position of the Agent/Particle.
	 * @param velocityAtX, the initial velocity of the Agent/Particle.
	 * @return a new Particle.
	 */
	public Agent getAgent(double positionAtX, double positionAtY, double velocityAtX, double velocityAtY) {
		if((positionAtX >= 0 && positionAtY >= 0) && (velocityAtX >= 0 && velocityAtY >= 0)) {
			Particle particle = new Particle();
			particle.setPos(positionAtX, positionAtY);
			particle.setVelo(velocityAtX, velocityAtY);
			return particle;
		}
		throw new IllegalArgumentException("position or velocity are invalid");
	}
	
	/**
	 * Of using the String as an identifier for a certain
	 * Agent. This method will produce the agent based upon
	 * which swarm is required. It is returning the
	 * required object after providing the string.
	 * For example, if "Particle" is given as input,
	 * the method will return a particle.
	 * 
	 * @param agentType the agent type like, ant, bee or particle.
	 * @return the object for the required particle.
	 */
	public Agent getAgent(Class<T> agentType) {
		if(agentType.equals(Particle.class)) {
			return new Particle();
		}
		else if(agentType.equals(Ant.class)) {
			return new Ant();
		}
		else {
			throw new IllegalArgumentException("Agent type not supported");
		}
	}
}
