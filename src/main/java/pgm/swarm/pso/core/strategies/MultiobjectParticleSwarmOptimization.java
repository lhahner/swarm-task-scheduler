package pgm.swarm.pso.core.strategies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import org.jetbrains.annotations.NotNull;
import pgm.swarm.Agent;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.MultiobjectParticle;
import pgm.visualization.VisualizationStrategy;

import java.util.*;
import java.util.stream.IntStream;

/**
 *
 */
@Setter
@Getter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class MultiobjectParticleSwarmOptimization implements OptimizationStrategy<MultiobjectParticle> {
    protected VisualizationStrategy visualizationStrategy;

    /*Stores the set of all objective vectors corresponding to the pareto optimal solutions. */
    private Map<Integer, Double> paretoFront;

    /**
     * Optimizes a given swarm starting from a specified position over a defined number of iterations.
     * This is domain independent.
     *
     * @param swarm The swarm to be optimized.
     * @param startPositionAtX The initial x-coordinate of the swarm.
     * @param startPositionAtY The initial y-coordinate of the swarm.
     * @param swarmSize The number of particles in the swarm, which also determines the number of iterations.
     */
    public void optimize(Swarm<MultiobjectParticle> swarm, double startPositionAtX, double startPositionAtY, int swarmSize) {
        swarm = new Swarm<MultiobjectParticle>(startPositionAtX, startPositionAtY, swarmSize, MultiobjectParticle.class);
        for (int i = 0; i < swarmSize; i++) { //Epochs
            for (MultiobjectParticle particle : swarm.getAgents()) {
                if (particle.evaluate(particle.getPos()) < particle.evaluate(particle.getPbest())) {
                    double[] pbest = particle.getPos();
                    particle.setPbest(pbest);
                }
                if (particle.evaluate(particle.getPos()) < particle.evaluate(swarm.getGlobalBests())) {
                    swarm.setGlobalBests(particle.getPos());
                }
                particle.calculateVelocity(particle.getVelo(), 2, particle.getPbest(), particle.getPos(), 2, swarm.getGlobalBests(),
                        Math.random(), Math.random());
                particle.calcPos(particle.getPos(), particle.getVelo());
            }
        }
    }

    public List<Double> updateParetoFront(List<Double> paretoFront, double candidate) {
        for(double archive : paretoFront) {
            if(this.doesDominate(archive, candidate)) {
                return paretoFront;
            }
            else if(this.doesDominate(candidate, archive)) {
                paretoFront.remove(archive);
            }
        }
        paretoFront.add(candidate);
        return paretoFront;
    }

    /**
     * Evaluates if found optima are dominating the optimal from the archive. It is required to
     * bring both the optima on the equal target, so to speak, you need to minimize both or not.
     *
     * @param archive The archived the best solutions so far, based on the number of Objective Functions
     * @param candidate A new candidate to give the best solution so far
     * @return true if the new Solution does dominate
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
     * This method will check if the position of the particle is too large to be in the scope
     * of the provided VMs and provided Tasks and afterward will set the position of the particles
     * to random.
     *
     * @param particle The particle which should be checked and changed.
     * @param vmList The List of VMs used.
     * @param taskList The List of Tasks used
     */
    protected void resetParticlesOutOfRange(Particle particle, ArrayList<Vm> vmList, ArrayList<CloudletSimple> taskList) {
        int scalingFactor = Math.max(taskList.size(), vmList.size());
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
