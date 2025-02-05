package pgm.swarm.schedeuler;

import java.util.ArrayList;

public interface Swarm <T> {
	
	public ArrayList<T> getAgents();
	public void setAgents(double start_x, double start_y, int size);
	public double[] getGbest();
	public void setGbest(double[] gbest);
}
