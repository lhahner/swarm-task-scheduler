package pgm.visualization;

import java.util.ArrayList;
import pgm.swarm.pso.core.Particle;

public class NoVisualizationStrategy implements VisualizationStrategy{

	@Override
	public void visualize(ArrayList<Particle> particles, int dimension) {
		return;
	}

	@Override
	public void updateAndVisualize(ArrayList<Particle> particles) {
		return;
	}
}
