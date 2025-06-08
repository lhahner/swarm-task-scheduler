package pgm.visualization;

import pgm.swarm.pso.core.*;
import java.util.*;

public interface VisualizationStrategy {
	public void visualize(ArrayList<Particle> particles, int dimension);
	public void updateAndVisualize(ArrayList<Particle> particles);
}
