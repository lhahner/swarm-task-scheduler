package pgm.swarm.schedeuler;

import java.util.ArrayList;
import java.util.List;

public class SchedeulerApplication {
	
	public static void main(String[] args) {
		new ParticleSwarmOptimzation().optimize(new PatricleSwarm(), 0.25, 0.3, 5);
	}

}
