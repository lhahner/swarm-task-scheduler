package pgm.swarm.pso.core.strategies;

import pgm.swarm.Agent;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;

public interface OptimizationStrategy<T extends Agent> {
    public void optimize(Swarm<T> swarm, double startPositionAtX, double startPositionAtY, int swarmSize);

}
