package pgm.swarm.pso.core.strategies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.MultiAdaptiveParticle;
import pgm.visualization.VisualizationStrategy;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MultiAdaptiveParticleSwarmOptimization implements OptimizationStrategy<MultiAdaptiveParticle> {
    protected VisualizationStrategy visualizationStrategy;

    /**
     * Optimizes a given swarm starting from a specified position over a defined number of iterations.
     * This is domain independent.
     *
     * @param swarm The swarm to be optimized.
     * @param swarmSize The number of particles in the swarm, which also determines the number of iterations.
     */
    public void optimize(Swarm<MultiAdaptiveParticle> swarm, List<Double> position, List<Double> velocity, int swarmSize) {

    }

    /**
     * Should provide and assign a Visualization strategy specified for the PSO algorithm.
     *
     * @param visualizationStrategy the strategy to be performed for the current use-case
     * @return the strategy to be performed for the current use-case
     */
    protected VisualizationStrategy setAndGetVisualizationStrategy(VisualizationStrategy visualizationStrategy) {
        this.visualizationStrategy = visualizationStrategy;
        return this.visualizationStrategy;
    }
}
