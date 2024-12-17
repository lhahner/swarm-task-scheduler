package pgm.swarm.schedeuler;

public interface Agent {
	public void calcVelo(double cur_velo, double c_1, double pos_best, double pos, double c_2, double global_best);
	public void calcPos(double cur_pos, double new_velo);
}
