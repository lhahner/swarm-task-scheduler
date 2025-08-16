package pgm.swarm.pso.core.strategies;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.MultiobjectParticle;
import pgm.visualization.VisualizationStrategy;

/** Performs multi-objective Particle Swarm Optimization (PSO). */
@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MultiobjectParticleSwarmOptimization
        implements OptimizationStrategy<MultiobjectParticle> {

    /** Stores the set of all objective vectors corresponding to the Pareto-optimal solutions. */
    private Map<Integer, Double> paretoFront;

    protected VisualizationStrategy visualizationStrategy;

    /**
     * Optimizes a given swarm starting from a specified position over a defined number of iterations.
     * This is domain independent.
     *
     * @param swarm the swarm to be optimized
     * @param position the starting position for the particles
     * @param velocity the starting velocity for the particles
     * @param swarmSize the number of particles in the swarm, which also determines the number of
     *     iterations
     */
    public void optimize(
            Swarm<Particle> swarm, List<Double> position, List<Double> velocity, int swarmSize) {}

    /**
     * Updates the Pareto front with a new candidate solution.
     *
     * @param paretoFront the current Pareto front
     * @param candidate the new candidate solution
     * @return the updated Pareto front
     */
    public List<Double> updateParetoFront(List<Double> paretoFront, double candidate) {
        for (double archive : paretoFront) {
            if (this.doesDominate(archive, candidate)) {
                return paretoFront;
            } else if (this.doesDominate(candidate, archive)) {
                paretoFront.remove(archive);
            }
        }
        paretoFront.add(candidate);
        return paretoFront;
    }

    /**
     * Evaluates whether a candidate dominates a solution in the archive.
     *
     * <p>Both objectives must be minimized to be comparable.
     *
     * @param archive the archived solution (best found so far)
     * @param candidate the new candidate solution
     * @return true if the candidate dominates the archive solution
     */
    public boolean doesDominate(double archive, double candidate) {
        boolean doesDominant = false;
        boolean isStrictlySmaller = false;

        if (candidate <= archive) {
            doesDominant = true;
        }
        if (candidate < archive) {
            isStrictlySmaller = true;
        }
        return doesDominant && isStrictlySmaller;
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
