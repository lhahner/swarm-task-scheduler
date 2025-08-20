package pgm.swarm.pso.core.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

/**
 * Class for performing Particle Swarm Optimization (PSO) on different problems.
 *
 * <p>This class makes use of the Particle Swarm and performs domain-specific or domain-independent
 * optimization.
 */
@Setter
@Getter
@Log4j2
@AllArgsConstructor
public class ParticleSwarmOptimization implements OptimizationStrategy<Particle> {

    protected VisualizationStrategy visualizationStrategy;
    protected Evaluation evaluation;
    private ArrayList<CloudletSimple> cloudTasks;
    private ArrayList<Vm> cloudVms;

    /**
     * Optimizes a given swarm starting from a specified position over a defined number of iterations.
     * This is domain independent.
     *
     * @param position the starting position for the particles
     * @param velocity the starting velocity for the particles
     * @param swarmSize the number of particles in the swarm, which also determines the number of
     *     iterations
     */
    public double optimize(
            List<Double> position, List<Double> velocity, int swarmSize) {
        Swarm<Particle> swarm = new Swarm<>(position, velocity, swarmSize, Particle.class);
        for (int i = 0; i < swarmSize; i++) {
            for (Particle particle : swarm.getAgents()) {
                resetParticlesOutOfRange(particle, cloudVms, cloudTasks);
                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(particle.getParticlesBest(), cloudTasks, cloudVms)) {
                    List<Double> newParticlesBest = particle.getPosition();
                    particle.setParticlesBest(newParticlesBest);
                }
                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(swarm.getGlobalBests(), cloudTasks, cloudVms)) {
                    swarm.setGlobalBests(particle.getPosition());
                }
                particle.calculateVelocity(
                        particle.getVelocity(),
                        particle.getParticlesBest(),
                        particle.getPosition(),
                        swarm.getGlobalBests());
                particle.calculateNewPosition(particle.getPosition(), particle.getVelocity());
            }
        }
        return 0.0;
    }

    /**
     * Checks if the position of the particle is too large to be in the scope of the provided VMs and
     * tasks. If so, reset the particle’s position randomly within the valid range.
     *
     * @param particle the particle to be checked and potentially reset
     * @param vmList the list of VMs used
     * @param taskList the list of tasks used
     */
    protected void resetParticlesOutOfRange(
            Particle particle, ArrayList<Vm> vmList, ArrayList<CloudletSimple> taskList) {
        int scalingFactor = Math.max(taskList.size(), vmList.size());

        IntStream.range(0, particle.getPosition().size()).forEach(i -> {
            if (particle.getPosition().get(i) >= vmList.size()
                    || particle.getPosition().get(i) >= taskList.size()){
                particle.getPosition().set(i, Math.random() * scalingFactor);
            }
        });
    }

    /**
     * Provides and assigns a visualization strategy specified for the PSO algorithm.
     *
     * @param visualizationStrategy the strategy to be performed for the current use case
     * @return the visualization strategy set for this instance
     */
    protected VisualizationStrategy setAndGetVisualizationStrategy(
            VisualizationStrategy visualizationStrategy) {
        this.visualizationStrategy = visualizationStrategy;
        return this.visualizationStrategy;
    }
}
