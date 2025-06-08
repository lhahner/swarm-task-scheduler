package pgm.swarm;

import java.util.ArrayList;

public interface Swarm <T> {
	
	/**
	 * Will get all agents that are inside a Swarm of type T.
	 * 
	 * @return All Agents that are inside a swarm.
	 */
	public ArrayList<T> getAgents();
}
