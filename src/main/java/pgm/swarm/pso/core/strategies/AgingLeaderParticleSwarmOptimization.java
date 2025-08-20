package pgm.swarm.pso.core.strategies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.VmSimple;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.AgingLeaderParticle;
import pgm.swarm.pso.core.decorators.ChallengerParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    private List<CloudletSimple> cloudTasks;

    /**
     * Cloud virtual machines used by {@link #evaluation} to score candidate solutions.
     */
    private List<VmSimple> cloudVms;

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
    public double optimize(List<Double> position, List<Double> velocity, int swarmSize) {
        Swarm<AgingLeaderParticle> swarm =
                new Swarm<>(position, velocity, swarmSize, AgingLeaderParticle.class);
        setAgingLeaderParticle(swarm.getAgents().get(0));
        int i = 0;
        for (; i < swarmSize; i++) {
            List<ChallengerParticle> toAdd = new ArrayList<>();
            for (AgingLeaderParticle particle : new ArrayList<>(swarm.getAgents())) {
                if(particle.getParticlesBest() == null){
                    particle.setParticlesBest(particle.getPosition());
                }
                resetParticlesOutOfRange(particle, cloudVms, cloudTasks);
                if (agingLeaderParticle != null) {
                    resetParticlesOutOfRange(agingLeaderParticle, cloudVms, cloudTasks);
                }

                // update personal bests
                if (evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                        < evaluation.evaluateMakespan(particle.getParticlesBest(), cloudTasks, cloudVms)) {
                    List<Double> newParticlesBest = particle.getPosition();
                    particle.setParticlesBest(newParticlesBest);
                }
                if (agingLeaderParticle != null &&
                        evaluation.evaluateMakespan(agingLeaderParticle.getPosition(), cloudTasks, cloudVms)
                                < evaluation.evaluateMakespan(agingLeaderParticle.getParticlesBest(), cloudTasks, cloudVms)) {
                    agingLeaderParticle.setParticlesBest(agingLeaderParticle.getPosition());
                }

                // leader update / aging
                if (agingLeaderParticle == null ||
                        evaluation.evaluateMakespan(particle.getPosition(), cloudTasks, cloudVms)
                                < evaluation.evaluateMakespan(agingLeaderParticle.getPosition(), cloudTasks, cloudVms)) {
                    setAgingLeaderParticle(particle);
                } else if (agingLeaderParticle.isTooOld()) {
                    setAgingLeaderParticle(null);
                    challengerParticle = spawnAndSetChallengerInSwarm(swarm); // no mutation
                    toAdd.add(new ChallengerParticle(swarm.getAgents().get(0))); // queue addition
                }

                // velocity & position
                if (agingLeaderParticle == null) {
                    if (challengerParticle == null) {
                        challengerParticle = spawnAndSetChallengerInSwarm(swarm);
                    }
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

            // apply queued additions AFTER the iteration
            if (!toAdd.isEmpty()) {
                swarm.getAgents().addAll(toAdd);
            }

            if (agingLeaderParticle != null) {
                agingLeaderParticle.incrementLeaderAge();
            }
        }
        log.info("iteration number {} and local best position vector {} with optimal makespan {}", i, agingLeaderParticle != null ? agingLeaderParticle.getParticlesBest()
                : challengerParticle.getPosition(), evaluation.evaluateMakespan(
                (agingLeaderParticle != null ? agingLeaderParticle.getParticlesBest()
                        : challengerParticle.getPosition()),
                cloudTasks, cloudVms));
        return evaluation.evaluateMakespan(
                (agingLeaderParticle != null ? agingLeaderParticle.getParticlesBest()
                        : challengerParticle.getPosition()),
                cloudTasks, cloudVms);
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
     * Spawns a {@link ChallengerParticle}, stores it, and appends a challenger to the swarm based on
     * the current first agent's state.
     *
     * @param swarm the swarm that will receive the new challenger agent
     */
    public ChallengerParticle spawnAndSetChallengerInSwarm(Swarm<AgingLeaderParticle> swarm) {
        ChallengerParticle challengerParticle = new ChallengerParticle();
        challengerParticle.setPosition((swarm.getGlobalBests() != null ? swarm.getGlobalBests() : swarm.getAgents().get(0).getPosition()));
        return challengerParticle;
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
