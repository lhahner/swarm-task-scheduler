package pgm.swarm.pso.core.strategies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.VmSimple;
import pgm.swarm.Population;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.MultiAdaptiveParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

import java.util.*;
import java.util.stream.IntStream;

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
 *   <li>Call {@link #optimize(List, List, int)} to initialize and iterate the swarm.</li>
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
    protected final Evaluation evaluation = new Evaluation();

    /**
     * Cloud tasks used by the {@link #evaluation} to score particle positions.
     */
    private List<CloudletSimple> cloudTasks;

    /**
     * Cloud VMs used by the {@link #evaluation} to score particle positions.
     */
    private List<VmSimple> cloudVms;

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
    private TreeMap<Double, MultiAdaptiveParticle> localDensities;

    /**
     * Optimizes a given swarm starting from a specified position and velocity over a defined swarm size.
     *
     * <p>This method re-initializes the provided {@code swarm} instance using the given
     * {@code position}, {@code velocity}, and {@code epochs}, partitions it into populations,
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
     * @param position the initial position template used to construct the swarm.
     * @param velocity the initial velocity template used to construct the swarm.
     * @param epochs the number of particles to create; also determines the iteration pass here.
     */
    public double optimize(
            List<Double> position, List<Double> velocity, int epochs) {
        Swarm<MultiAdaptiveParticle> swarm = new Swarm<MultiAdaptiveParticle>(position, velocity, cloudTasks.size(), MultiAdaptiveParticle.class);
        setCenterOfPopulations(swarm);
        joinCenterOfPopulations();
        for(int i = 0; i < epochs; i++) {
            for (Population<MultiAdaptiveParticle> population : populations) {
                for (MultiAdaptiveParticle particle : population.getAgents()) {
                    resetParticlesOutOfRange(particle, cloudVms, cloudTasks);
                    if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                            < evaluation.evaluateMakespan(particle.getParticlesBest(), cloudTasks, cloudVms)) {
                        List<Double> newParticlesBest = particle.getPosition();
                        particle.setParticlesBest(newParticlesBest);
                    }
                    if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                            < evaluation.evaluateMakespan(population.getLocalBest(), cloudTasks, cloudVms)) {
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
                log.info("iteration number {} and local best position vector {} with optimal makespan {}", i, population.getLocalBest(), evaluation.evaluateMakespan(population.getLocalBest(), cloudTasks, cloudVms));
            }

        }
        List<Double> localBests = new ArrayList<>();
        for(Population<MultiAdaptiveParticle> positionsBests : populations) {
            localBests.add(evaluation.evaluateMakespan(positionsBests.getLocalBest(), cloudTasks, cloudVms));
        }
        return localBests.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
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
            Particle particle, List<VmSimple> vmList, List<CloudletSimple> taskList) {
        int scalingFactor = Math.max(taskList.size(), vmList.size());

        IntStream.range(0, particle.getPosition().size()).forEach(i -> {
            if (particle.getPosition().get(i) >= vmList.size()
                    || particle.getPosition().get(i) >= taskList.size()){
                particle.getPosition().set(i, Math.random() * scalingFactor);
            }
        });
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

        // FIX: compute global cutoff distance once for the swarm (percentile = 1.0 as in your CUTOFF).
        final double dc = MultiAdaptiveParticle.computeCutoffDistance(swarm, 1.0);

        // Calculate densities (using shared d_c) and store in the map.
        int index = 0;
        for (MultiAdaptiveParticle p : swarm.getAgents()) {
            p.setLocalDensity(index, swarm, dc); // FIX: new overload that accepts d_c
            // FIX: avoid overwriting when densities are equal by nudging the key
            double key = p.getLocalDensity();
            while (localDensities.containsKey(key)) {
                key = Math.nextUp(key);
            }
            localDensities.put(key, p);
            index++;
        }

        // Largest local density -> first center.
        double largestLocalDensity =
                localDensities.isEmpty()
                        ? 0.0
                        : localDensities.lastKey();

        if (!localDensities.isEmpty()) {
            populations.add(initalizePopulation(localDensities.get(largestLocalDensity)));
            localDensities.remove(largestLocalDensity);
        }

        // FIX: iterate over a snapshot and use correct next keys (don’t call keySet().iterator().next() each time)
        for (Double key : new ArrayList<>(localDensities.keySet())) {
            if ((largestLocalDensity - key) >= SPLIT_THRESHOLD) {
                populations.add(initalizePopulation(localDensities.get(key)));
                localDensities.remove(key);
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
        // FIX: iterate on a snapshot to avoid ConcurrentModificationException
        List<Map.Entry<Double, MultiAdaptiveParticle>> entries = new ArrayList<>(localDensities.entrySet());

        for (Map.Entry<Double, MultiAdaptiveParticle> entry : entries) {
            MultiAdaptiveParticle particle = entry.getValue();
            Population<MultiAdaptiveParticle> populationToJoin = null;

            // FIX: use absolute difference and initialize to +infinity
            double bestDelta = Double.POSITIVE_INFINITY;

            for (Population<MultiAdaptiveParticle> population : populations) {
                double delta = Math.abs(population.getCenter().getLocalDensity() - particle.getLocalDensity());
                if (delta < bestDelta) {
                    bestDelta = delta;
                    populationToJoin = population;
                }
            }

            if (populationToJoin == null) {
                // Fallback: if no populations (shouldn’t happen), create one
                populationToJoin = initalizePopulation(particle);
                populations.add(populationToJoin);
            }

            populationToJoin.addAgent(particle);
            // FIX: don’t remove while iterating an original map; we’re iterating over a snapshot
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
        population.setLocalBest(center.getPosition());
        return population;
    }
}
