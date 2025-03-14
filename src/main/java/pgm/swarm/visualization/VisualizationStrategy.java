package pgm.swarm.visualization;

import pgm.swarm.schedeuler.PSO.*;
import java.util.*;

public interface VisualizationStrategy {
	public void visualize(ArrayList<Particle> particles, int dimension);
	public void updateAndVisualize(ArrayList<Particle> particles);
}
