package pgm.swarm.schedeuler;

/**
 * This factory will generate the Agents necassry for a
 * Swarm. So whenever a swarm is made up, this method
 * should be called to produce a swarm upon the required
 * agents.
 * 
 * @author Lennart Hahner
 * @version 1.1.0
 */
public class SwarmFactory {
	
	/**
	 * This method is espacailly used to create
	 * an Agent of the type particle, in the current
	 * @version 1.1.0 only one PSO is implemented 
	 * which leads to this descision and the overloading.
	 * 
	 * @param pos initial position of the Agent/Particle.
	 * @param velo initial velocity of the Agent/Particle.
	 * @return a new Particle.
	 */
	public Agent getAgent(double x, double y, double velo_x, double velo_y) {
		if((x >= 0 && y >= 0) && (velo_x >= 0 && velo_y >= 0)) {
			Particle particle = new Particle();
			particle.setPos(x, y);
			particle.setVelo(velo_x, velo_y);
			return particle;
		}
		return null;
	}
	
	/**
	 * TODO Check regarding a different method instead
	 * of using the String as an identfier for a certain
	 * Agent.
	 * 
	 * This method will produce the agent based upon 
	 * which swarm is required. It is returning the
	 * required object after providing the string.
	 * For example if "Particle" is given as input,
	 * the method will return a particle.
	 * 
	 * @param agentType the agent type like, ant, bee or particle.
	 * @return the object for the required particle.
	 */
	public Agent getAgent(String agentType) {
		if(agentType.equals("Particle")) {
			return new Particle();
		}
		else {
			return null;
		}
	}
}
