package pgm.swarm.pso.core.strategies;

import pgm.swarm.Agent;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import java.util.List;

public interface OptimizationStrategy<T extends Agent> {
    public void optimize(Swarm<Particle> swarm, List<Double> position, List<Double> velocity, int swarmSize);
}
