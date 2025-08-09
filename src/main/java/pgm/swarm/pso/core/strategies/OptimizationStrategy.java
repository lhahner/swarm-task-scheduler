package pgm.swarm.pso.core.strategies;

import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;

public interface OptimizationStrategy {
    public void optimize(Swarm<Particle> swarm, double startPositionAtX, double startPositionAtY, int swarmSize);
}
