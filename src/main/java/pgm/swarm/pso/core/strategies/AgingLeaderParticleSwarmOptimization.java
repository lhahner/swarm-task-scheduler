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
import pgm.swarm.pso.core.decorators.AgingLeaderParticle;
import pgm.swarm.pso.core.decorators.ChallengerParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Particle Swarm Optimization (PSO) strategy with an "aging leader" mechanism.
 *
 * <p>The strategy maintains a current leader ({@link AgingLeaderParticle}) which guides particle
 * updates. If the leader becomes "too old" (as defined by {@link AgingLeaderParticle#isTooOld()}),
 * the algorithm spawns a {@link ChallengerParticle} that temporarily assumes the leader role to
 * encourage exploration until a new aging leader emerges.</p>
 *
 * <p>The optimization is domain-agnostic. Quality of a particle position is assessed by the
 * configured {@link #evaluation}, typically using {@code cloudTasks} and {@code cloudVms} as
 * inputs for an objective such as makespan.</p>
 *
 * <p><strong>Thread-safety:</strong> Instances are not thread-safe. If you need concurrency, use
 * external synchronization.</p>
 */
@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class AgingLeaderParticleSwarmOptimization implements OptimizationStrategy<Particle> {

    /**
     * Optional visualization hook to render or log intermediate results of the optimization.
     */
    protected VisualizationStrategy visualizationStrategy;

    /**
     * Objective function used to evaluate particle positions (e.g., makespan).
     */
    protected final Evaluation evaluation = new Evaluation();

    /**
     * Cloud tasks used by {@link #evaluation} to score candidate solutions.
     */
    private ArrayList<CloudletSimple> cloudTasks;

    /**
     * Cloud virtual machines used by {@link #evaluation} to score candidate solutions.
     */
    private ArrayList<Vm> cloudVms;

    /**
     * The current "aging" leader guiding the swarm; may be replaced if it becomes too old.
     */
    private AgingLeaderParticle agingLeaderParticle;

    /**
     * A temporary challenger that guides the swarm when the aging leader is absent or replaced.
     */
    private ChallengerParticle challengerParticle;

    /**
     * Optimizes a swarm using an aging-leader dynamic over a fixed number of iterations.
     *
     * <p>This method re-initializes the provided {@code swarm} using the given {@code position} and
     * {@code velocity} templates and a swarm size, then iterates:</p>
     *
     * <ol>
     *   <li>Update each particle's personal best if the current position improves its score.</li>
     *   <li>Update the aging leader if any particle outperforms the current leader.</li>
     *   <li>If the aging leader becomes too old, clear it and spawn a {@link ChallengerParticle}.</li>
     *   <li>Update each particle's velocity towards either the aging leader or the challenger.</li>
     *   <li>Advance each particle to its new position.</li>
     * </ol>
     *
     * <p><strong>Preconditions:</strong> {@link #evaluation}, {@link #cloudTasks}, and
     * {@link #cloudVms} must be non-null and consistent with the dimensionality of
     * {@code position} and {@code velocity}.</p>
     *
     * @param position template of the initial position for each particle
     * @param velocity template of the initial velocity for each particle
     * @param swarmSize number of particles to create and the number of iterations to run
     */
    @Override
    public double optimize(
            List<Double> position, List<Double> velocity, int swarmSize) {
        Swarm<AgingLeaderParticle> swarm = new Swarm<AgingLeaderParticle>(position, velocity, swarmSize, AgingLeaderParticle.class);
        setAgingLeaderParticle((AgingLeaderParticle) swarm.getAgents().get(0));

        for (int i = 0; i < swarmSize; i++) {
            for (Particle particle : swarm.getAgents()) {
                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(particle.getParticlesBest(), cloudTasks, cloudVms)) {
                    List<Double> newParticlesBest = particle.getPosition();
                    particle.setParticlesBest(newParticlesBest);
                }

                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(agingLeaderParticle.getPosition(), cloudTasks, cloudVms)) {
                    setAgingLeaderParticle((AgingLeaderParticle) particle);
                } else if (agingLeaderParticle.isTooOld()) {
                    this.setAgingLeaderParticle(null);
                    spawnAndSetChallengerInSwarm(swarm);
                }

                if (agingLeaderParticle == null) {
                    particle.calculateVelocity(
                            particle.getVelocity(),
                            particle.getParticlesBest(),
                            particle.getPosition(),
                            challengerParticle.getPosition());
                } else {
                    particle.calculateVelocity(
                            particle.getVelocity(),
                            particle.getParticlesBest(),
                            particle.getPosition(),
                            agingLeaderParticle.getPosition());
                }
                particle.calculateNewPosition(particle.getPosition(), particle.getVelocity());
            }

            // Note: Ensure agingLeaderParticle is non-null before calling this in production code.
            this.agingLeaderParticle.incrementLeaderAge();
        }
        return 0.0;
    }

    /**
     * Spawns a {@link ChallengerParticle}, stores it, and appends a challenger to the swarm based on
     * the current first agent's state.
     *
     * @param swarm the swarm that will receive the new challenger agent
     */
    public void spawnAndSetChallengerInSwarm(Swarm<AgingLeaderParticle> swarm) {
        this.challengerParticle = new ChallengerParticle();
        swarm.getAgents().add(new ChallengerParticle(swarm.getAgents().get(0)));
    }

    /**
     * Assigns a visualization strategy for the current PSO run and returns it.
     *
     * @param visualizationStrategy the strategy to apply for visualization/logging
     * @return the same {@code visualizationStrategy} that was provided
     */
    protected VisualizationStrategy setAndGetVisualizationStrategy(
            VisualizationStrategy visualizationStrategy) {
        this.visualizationStrategy = visualizationStrategy;
        return this.visualizationStrategy;
    }
}
