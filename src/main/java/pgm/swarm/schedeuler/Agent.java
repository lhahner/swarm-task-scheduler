package pgm.swarm.schedeuler;

public interface Agent {
	public double[] getPos();
	public void setPos(double x, double y);
	public double[] getVelo();
	public void setVelo(double velo_x, double velo_y);
	
}
