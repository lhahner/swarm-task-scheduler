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
import pgm.visualization.VisualizationStrategy;

import java.util.ArrayList;

@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MultiAdaptiveParticleSwarmOptimization implements OptimizationStrategy {
    protected VisualizationStrategy visualizationStrategy;

    /**
     * Optimizes a given swarm starting from a specified position over a defined number of iterations.
     * This is domain independent.
     *
     * @param swarm The swarm to be optimized.
     * @param startPositionAtX The initial x-coordinate of the swarm.
     * @param startPositionAtY The initial y-coordinate of the swarm.
     * @param swarmSize The number of particles in the swarm, which also determines the number of iterations.
     */
    public void optimize(Swarm<Particle> swarm, double startPositionAtX, double startPositionAtY, int swarmSize) {

    }

    /**
     * This method will check if the position of the particle is too large to be in the scope
     * of the provided VMs and provided Tasks and afterward will set the position of the particles
     * to random.
     *
     * @param particle The particle which should be checked and changed.
     * @param vmList The List of VMs used.
     * @param taskList The List of Tasks used
     */
    protected void resetParticlesOutOfRange(Particle particle, ArrayList<Vm> vmList, ArrayList<CloudletSimple> taskList) {
        int scalingFactor = taskList.size() > vmList.size() ? taskList.size() : vmList.size();
        if (particle.getPos()[0] >= vmList.size()) {
            particle.setPosX(Math.random() * scalingFactor);
        }
        if (particle.getPos()[1] >= taskList.size()) {
            particle.setPosY(Math.random() * scalingFactor);
        }
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
