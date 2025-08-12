package pgm.swarm.pso.core.decorators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Population;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation is based on the Paper "An improved particle swarm
 * optimization algorithm for task scheduling in cloud computing"
 * Improves native Particle class by adding population assignment capabilities
 * as assigned in the paper.
 *
 * @Source <a href="https://doi.org/10.1007/s12652-023-04541-9">Paper Reference</a>
 */
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class MultiAdaptiveParticle extends Particle {

    /**
     * Specifies whether this particle is the local best
     * Particle leading the population.
     */
    private boolean isLocalBest;

    /**
     * Represents the number of particles that exist a given distance to particle i.
     */
    private double localDensity;

    /**
     * Different cutoff values of d and observe how cluster center change.
     */
    private final static double CUTOFF = 1.0;

    /**
     * Distance is considered as the minimum distance between particle i and each particle with the highes density.
     */
    private double distance;

    /**
     * Calculates the velocity for every localBest particle if it's not local best it uses the normal update velocity.
     */
    public void updateVelocityLocalBest(double[] cur_velo, double c_1, double[] pos_best, double[] pos, double c_2, double[] global_best, double r_1, double r_2, int numberOfPopulations, List<Population<Particle>> population){
        if (cur_velo.length != pos.length || pos.length != pos_best.length || pos.length != global_best.length) {
            throw new IllegalArgumentException("All input arrays must have the same length");
        }
        this.setVelo(
                (super.getInertiaWeight() * cur_velo[0] + c_1 * r_1 * (pos_best[0] - pos[0]) + c_2 * r_2 * (this.calculateAverageGlobalBest(population, numberOfPopulations) - pos[0])),
                (super.getInertiaWeight() * cur_velo[1] + c_1 * r_1 * (pos_best[1] - pos[1]) + c_2 * r_2 * (this.calculateAverageGlobalBest(population, numberOfPopulations) - pos[1]))
        );
        log.info("Ran calculateVelocity() in class ParticleModified, calculated: {}", Arrays.toString(this.getVelo()));
    }

    /**
     * This function will set the localDensity for a particle.
     * Local density represents the number of particles that exist at a given distance to
     * particle i. Based upon Euclidean and cutoff distance from this particle to another.
     *
     * @param swarm The swarm in which the particle is part of.
     * @return The density calculated for the particle
     */
    public double setLocalDensity(Swarm<Particle> swarm) {
        double localDensity = 0;
        for(Particle particle : (ArrayList<Particle>)swarm.getAgents()){
            localDensity = localDensity + Math.exp(
                    (-1)*Math.pow((getDistance(particle)/this.getCutoffDistance(particle, CUTOFF)), 2)
            );
        }
        this.localDensity = localDensity;
        return localDensity;
    }

    /**
     * The Average Global best is defined by summing all local bests of each subpopulation.
     *
     * @param populations a list of populations which separates the swarm.
     * @param numberOfPopulations the number of populations which divide the swarm.
     * @return a float value which represents the average global best.
     */
    public double calculateAverageGlobalBest(List<Population<Particle>> populations, int numberOfPopulations) {
        List<Double> globalBests = new ArrayList<Double>();
        for(Population<Particle> population : populations) {
            globalBests.add(Arrays.stream(population.getLocalBest()).sum());
        }
        return globalBests.stream().mapToDouble(Double::doubleValue).sum()/numberOfPopulations;
    }

    /**
     * Compute the cutoff distance based on given percentile of all
     * pairwise Euclidean distances between points.
     *
     * @param particle The particle to which we calculate the distance
     * @param percentile e.g.: 2.0 for the 2% cutoff
     * @return The computed cutoff distance d_c
     */
    public double getCutoffDistance(Particle particle, double percentile) {
        int n = particle.getPos().length;
        List<Double> distances = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = this.getDistance(particle);
                distances.add(dist);
            }
        }
        Collections.sort(distances);

        int index = (int) Math.floor((percentile / 100.0) * distances.size());
        index = Math.max(0, Math.min(index, distances.size() - 1));

        return distances.get(index);
    }

    /**
     * Calculates and returns the Euclidean distance between particles.
     *
     * @param particle the other particle to which this should be measured to
     * @return the Euclidean distance
     */
    public double getDistance(Particle particle) {
        double ac = (this.getPos()[0] - particle.getPos()[0]);
        double cb = (this.getPos()[1] - particle.getPos()[1]);
        return Math.hypot(ac,cb);
    }
}
