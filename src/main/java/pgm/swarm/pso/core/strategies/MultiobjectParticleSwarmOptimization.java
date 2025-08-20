package pgm.swarm.pso.core.strategies;

import java.util.*;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import pgm.swarm.Agent;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.MultiobjectParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

/** Performs multi-objective Particle Swarm Optimization (PSO). */
@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MultiobjectParticleSwarmOptimization
        implements OptimizationStrategy<MultiobjectParticle> {

    /**
     * From which the particles best are selected.
     */
    private static final int SOLUTIONS_HEAD = 10;

    /** Stores the set of all objective vectors corresponding to the Pareto-optimal solutions. */
    private TreeMap<Double, List<Double>> paretoFront; // 1 Solution 2 Position
    private List<Double> objectives;
    private ArrayList<CloudletSimple> cloudTasks;
    private ArrayList<Vm> cloudVms;
    private double makespan, costs;
    protected VisualizationStrategy visualizationStrategy;
    protected final Evaluation evaluation = new Evaluation();

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
            List<Double> position,
            List<Double> velocity,
            int swarmSize) {
        Swarm<MultiobjectParticle> swarm = new Swarm<MultiobjectParticle>(
                        position, velocity, swarmSize, MultiobjectParticle.class);
        for (int i = 0; i < swarmSize; i++) {
            for (MultiobjectParticle particle : swarm.getAgents()) {
                resetParticlesOutOfRange(particle, cloudVms, cloudTasks);
                if (updateParetoFront(
                        paretoFront,
                        particle.getPosition(),
                        evaluation.evaluateMakespan(
                                particle.getPosition(), cloudTasks, cloudVms))
                        || updateParetoFront(
                        paretoFront,
                        particle.getPosition(),
                        evaluation.executionCosts(
                                5,
                                cloudVms.size(),
                                evaluation.taskExecutionTime(
                                        particle.getPosition(), cloudTasks, cloudVms)))) {
                    particle.setParticlesBest(particle.getPosition());
                    swarm.setGlobalBests(particle.getPosition());
                }
                swarm.setGlobalBests(getRandomOfBestTenSolution(paretoFront));
                particle.calculateVelocity(
                        particle.getVelocity(),
                        particle.getParticlesBest(),
                        particle.getPosition(),
                        swarm.getGlobalBests());
                particle.calculateNewPosition(
                        particle.getPosition(), particle.getVelocity());
            }
        }
        return evaluation.evaluateMakespan(swarm.getGlobalBests(), cloudTasks, cloudVms);
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
     * Attempts to update the given Pareto front with a new candidate solution.
     *
     * <p>The method checks if the candidate dominates (i.e., is less than or equal to) any existing
     * solution in the Pareto front. If the candidate is strictly better (strictly smaller than at least
     * one existing solution), the dominated solution is removed and the candidate is added. If the
     * candidate is only equal (not strictly smaller), it is added alongside existing solutions.
     *
     * @param paretoFront the current Pareto front, mapping objective values to their corresponding
     *     positions
     * @param candidatePosition the position vector associated with the candidate solution
     * @param candidate the objective value of the candidate solution
     * @return {@code true} if the candidate was added to the Pareto front; {@code false} otherwise
     */
    public boolean updateParetoFront(
            Map<Double, List<Double>> paretoFront,
            List<Double> candidatePosition,
            double candidate) {
        double archive;
        Double archiveKeyToRemove = null;
        boolean doesDominante = false,
                isStrictlySmaller = false;
        if(paretoFront == null){
            paretoFront = new TreeMap<>();
            paretoFront.put(candidate, candidatePosition);
            this.paretoFront = (TreeMap<Double, List<Double>>) paretoFront;
            return true;
        }
        for (Map.Entry<Double, List<Double>> entry : paretoFront.entrySet()) {
            archive = entry.getKey();
            if (candidate <= archive) {
                doesDominante = true;
            }
            if (candidate < archive) {
                isStrictlySmaller = true;
            }
            if (doesDominante && isStrictlySmaller) {
                archiveKeyToRemove = archive;
            }
        }
        if (doesDominante) {
            if (archiveKeyToRemove != null) {
                paretoFront.remove(archiveKeyToRemove);
                paretoFront.put(candidate, candidatePosition);
                this.paretoFront = (TreeMap<Double, List<Double>>) paretoFront;
            } else {
                paretoFront.put(candidate, candidatePosition);
                this.paretoFront = (TreeMap<Double, List<Double>>) paretoFront;
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
   public List<Double> getRandomOfBestTenSolution(Map<Double, List<Double>> paretoFront) {
       if (paretoFront.size() < SOLUTIONS_HEAD) {
           Object[] values = paretoFront.values().toArray();
           return (List<Double>) values[new Random().nextInt(values.length)];
       }
       Object[] values = paretoFront.values().toArray();
       return (List<Double>) values[new Random().nextInt(SOLUTIONS_HEAD)];
    }

    /**
     *
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
