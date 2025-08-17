package pgm.swarm.pso.core.strategies;

import java.util.*;

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

    private static final double SOLUTIONS_HEAD = 10;

    /** Stores the set of all objective vectors corresponding to the Pareto-optimal solutions. */
    private TreeMap<Double, List<Double>> paretoFront; // 1 Solution 2 Position
    private List<Double> objectives;
    private ArrayList<CloudletSimple> cloudTasks;
    private ArrayList<Vm> cloudVms;
    private double makespan, costs;
    protected VisualizationStrategy visualizationStrategy;
    protected Evaluation evaluation;

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
            Swarm<MultiobjectParticle> swarm,
            List<Double> position,
            List<Double> velocity,
            int swarmSize) {
        swarm = new Swarm<MultiobjectParticle>(
                        position, velocity, swarmSize, MultiobjectParticle.class);
        for (int i = 0; i < swarmSize; i++) {
            for (MultiobjectParticle particle : swarm.getAgents()) {
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
    }

    /**
     * Updates the Pareto front with a new candidate solution.
     *
     * @param paretoFront the current Pareto front
     * @param candidate the new candidate solution
     * @return the updated Pareto front
     */
    public boolean updateParetoFront(Map<Double, List<Double>> paretoFront, List<Double> candidatePosition, double candidate) {
        double archive;
        for (Map.Entry<Double, List<Double>> entry : paretoFront.entrySet()) {
            archive = entry.getKey();
            if (this.doesDominate(archive, candidate)) {
                return false;
            } else if (this.doesDominate(candidate, archive)) {
                paretoFront.remove(archive);
            }
        }
        paretoFront.put(candidate, candidatePosition);
        return true;
    }

    /**
     * Evaluates whether a candidate dominates a solution in the archive.
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

   public List<Double> getRandomOfBestTenSolution(Map<Double, List<Double>> paretoFront) {
       if (paretoFront.size() < SOLUTIONS_HEAD) {
           return paretoFront.get(new Random().nextDouble(paretoFront.size()));
       }
       return paretoFront.get(new Random().nextDouble(SOLUTIONS_HEAD));
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
