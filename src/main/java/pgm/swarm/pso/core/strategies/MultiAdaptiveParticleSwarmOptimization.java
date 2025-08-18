package pgm.swarm.pso.core.strategies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import pgm.swarm.Population;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.decorators.MultiAdaptiveParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

import java.util.*;

/**
 * Multi-population Particle Swarm Optimization (PSO) strategy that operates on
 * {@link MultiAdaptiveParticle} agents.
 *
 * <p>This strategy partitions a {@link Swarm} into multiple {@link Population}s based on each
 * particle's local density, selects population centers, and updates particle velocities and positions
 * according to both individual and population-level bests. The approach is domain-agnostic and relies
 * on an {@link Evaluation} to score candidate solutions (e.g., makespan for scheduling).
 *
 * <p>Usage pattern:
 * <ol>
 *   <li>Instantiate this strategy and set {@link #evaluation}, cloud resources, and (optionally)
 *       a {@link #visualizationStrategy}.</li>
 *   <li>Call {@link #optimize(Swarm, List, List, int)} to initialize and iterate the swarm.</li>
 * </ol>
 *
 * <p><strong>Note:</strong> This class assumes that the provided {@link Evaluation} is deterministic for
 * identical inputs and that the {@code cloudTasks} and {@code cloudVms} lists are consistent across calls.
 *
 * @see MultiAdaptiveParticle
 * @see Population
 * @see Swarm
 * @see Evaluation
 */
@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MultiAdaptiveParticleSwarmOptimization implements OptimizationStrategy<MultiAdaptiveParticle> {

    /**
     * Threshold on the difference between local densities that determines whether to split
     * a new population instead of joining an existing one. The value should be in the range [0, 1].
     */
    private static final double SPLIT_THRESHOLD = .5;

    /**
     * Optional visualization callback for reporting intermediate and final results.
     */
    protected VisualizationStrategy visualizationStrategy;

    /**
     * Objective function used to evaluate candidate solutions (e.g., makespan).
     */
    protected Evaluation evaluation;

    /**
     * Cloud tasks used by the {@link #evaluation} to score particle positions.
     */
    private ArrayList<CloudletSimple> cloudTasks;

    /**
     * Cloud VMs used by the {@link #evaluation} to score particle positions.
     */
    private ArrayList<Vm> cloudVms;

    /**
     * Discovered populations derived from the swarm via local density analysis.
     */
    private List<Population<MultiAdaptiveParticle>> populations;

    /**
     * Map of local density to particle. The map is used as a working set while selecting centers and
     * joining remaining particles to populations. Keys are local densities; values are particles.
     *
     * <p>Implementation detail: a {@link TreeMap} is used so densities are ordered.
     */
    private Map<Double, MultiAdaptiveParticle> localDensities;

    /**
     * Optimizes a given swarm starting from a specified position and velocity over a defined swarm size.
     *
     * <p>This method re-initializes the provided {@code swarm} instance using the given
     * {@code position}, {@code velocity}, and {@code swarmSize}, partitions it into populations,
     * and then performs a single pass over each {@link Population} to update each particle:
     * <ul>
     *   <li>Update personal best if current position is better.</li>
     *   <li>Update population local best if current position improves the global best proxy.</li>
     *   <li>Recompute velocity using personal and local bests.</li>
     *   <li>Optionally adjust velocity considering multiple populations.</li>
     *   <li>Advance to a new position.</li>
     * </ul>
     *
     * <p><strong>Preconditions:</strong>
     * <ul>
     *   <li>{@link #evaluation}, {@link #cloudTasks}, and {@link #cloudVms} are non-null and consistent.</li>
     *   <li>{@code position} and {@code velocity} match the dimensionality expected by the evaluation.</li>
     * </ul>
     *
     * @param swarm the swarm to be optimized; will be re-initialized inside this method.
     * @param position the initial position template used to construct the swarm.
     * @param velocity the initial velocity template used to construct the swarm.
     * @param swarmSize the number of particles to create; also determines the iteration pass here.
     */
    public void optimize(
            Swarm<MultiAdaptiveParticle> swarm, List<Double> position, List<Double> velocity, int swarmSize) {
        swarm = new Swarm<>(position, velocity, swarmSize, MultiAdaptiveParticle.class);
        setCenterOfPopulations(swarm);
        joinCenterOfPopulations();

        for (Population<MultiAdaptiveParticle> population : populations) {
            for (MultiAdaptiveParticle particle : population.getPopulation()) {
                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(particle.getParticlesBest(), cloudTasks, cloudVms)) {
                    List<Double> newParticlesBest = particle.getPosition();
                    particle.setParticlesBest(newParticlesBest);
                }
                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(swarm.getGlobalBests(), cloudTasks, cloudVms)) {
                    population.setLocalBest(particle.getPosition());
                }
                particle.calculateVelocity(
                        particle.getVelocity(),
                        particle.getParticlesBest(),
                        particle.getPosition(),
                        population.getLocalBest());
                particle.updateVelocityLocalBest(
                        particle.getVelocity(),
                        particle.getParticlesBest(),
                        particle.getPosition(),
                        population.getLocalBest(),
                        populations.size(),
                        populations
                );
                particle.calculateNewPosition(particle.getPosition(), particle.getVelocity());
            }
        }
    }

    /**
     * Partitions the given {@link Swarm} into one or more {@link Population}s by:
     * <ol>
     *   <li>Computing local densities for all particles.</li>
     *   <li>Selecting the particle with the largest density as the first population center.</li>
     *   <li>Optionally creating additional populations when the next highest density differs from
     *       the largest by at least {@link #SPLIT_THRESHOLD}.</li>
     * </ol>
     *
     * <p>Populations created here contain only their center at first. Remaining particles are later
     * assigned via {@link #joinCenterOfPopulations()} based on nearest (by density difference) center.
     *
     * @param swarm the swarm whose agents will be analyzed for population centers.
     */
    public void setCenterOfPopulations(Swarm<MultiAdaptiveParticle> swarm) {
        List<Population<MultiAdaptiveParticle>> populations = new ArrayList<>();
        localDensities = new TreeMap<>();
        // Calculate densities.
        for (MultiAdaptiveParticle multiAdaptiveParticle : swarm.getAgents()) {
            multiAdaptiveParticle.setLocalDensity(swarm);
            localDensities.put(multiAdaptiveParticle.getLocalDensity(), multiAdaptiveParticle);
        }
        // Largest local density.
        double largestLocalDensity =
                localDensities.keySet().stream().mapToDouble(Double::doubleValue).max().getAsDouble();
        // Add largest as center of the first population.
        populations.add(initalizePopulation(localDensities.get(largestLocalDensity)));
        // Remove from the density list to avoid joining it later.
        localDensities.remove(largestLocalDensity);
        // Optionally create additional populations based on split threshold.
        for (Map.Entry<Double, MultiAdaptiveParticle> entry : localDensities.entrySet()) {
            double nextKey = localDensities.keySet().iterator().next();
            if ((largestLocalDensity - nextKey) >= SPLIT_THRESHOLD) {
                populations.add(initalizePopulation(localDensities.get(nextKey)));
                localDensities.remove(nextKey);
            }
        }
        this.populations = populations;
    }

    /**
     * Assigns remaining particles (those not selected as centers) to the nearest population
     * center according to the absolute difference in local density.
     *
     * <p>For each particle in {@link #localDensities}, the population whose center has the smallest
     * density difference is selected and the particle is added to that population.
     *
     * <p><strong>Implementation note:</strong> This method assumes {@link #populations} and
     * {@link #localDensities} were prepared by {@link #setCenterOfPopulations(Swarm)}.
     */
    public void joinCenterOfPopulations() {
        for (Map.Entry<Double, MultiAdaptiveParticle> entry : localDensities.entrySet()) {
            Population<MultiAdaptiveParticle> populationToJoin = null;
            double differenceOfLocalDensity = populations.get(0).getCenter().getLocalDensity();
            for (Population<MultiAdaptiveParticle> population : populations) {
                if (population.getCenter().getLocalDensity() - entry.getKey() < differenceOfLocalDensity) {
                    differenceOfLocalDensity = population.getCenter().getLocalDensity() - entry.getKey();
                    populationToJoin = population;
                }
            }
            assert populationToJoin != null;
            populationToJoin.addAgent(entry.getValue());
            localDensities.remove(entry.getKey());
        }
    }

    /**
     * Creates a new {@link Population} with the given center and initializes it by adding the center
     * as the first agent.
     *
     * @param center the particle designated as the population center.
     * @return a new population containing the provided center.
     */
    public Population<MultiAdaptiveParticle> initalizePopulation(MultiAdaptiveParticle center) {
        Population<MultiAdaptiveParticle> population = new Population<MultiAdaptiveParticle>();
        population.setCenter(center);
        population.addAgent(center);
        return population;
    }
}
