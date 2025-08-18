package pgm.swarm.pso.core.decorators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Population;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    public void updateVelocityLocalBest(List<Double> velocity,
                                        List<Double> particlesBest,
                                        List<Double> position,
                                        List<Double> globalBest,
                                        int numberOfPopulations,
                                        List<Population<MultiAdaptiveParticle>> population) {
        if (velocity.size() != position.size()
                || position.size() != particlesBest.size()
                || position.size() != globalBest.size()) {
            throw new IllegalArgumentException("All input arrays must have the same length");
        }
        List<Double> newVelocity = (List<Double>) IntStream.range(0, velocity.size()).mapToObj(
                i -> {
                    return this.getVelocity().set(i, (super.getInertiaWeight() *
                            (super.inertiaWeight * velocity.get(i)) +
                            (COGNITIVE_LEARNING_FACTOR * Math.random() *
                            (this.calculateAverageGlobalBest(population, numberOfPopulations) - position.get(i)))
                    ));
                }).toList();
        this.setVelocity(newVelocity);
        log.info("Ran calculateVelocity() in class Particle, calculated: {}", List.of(velocity));
    }

    /**
     * This function will set the localDensity for a particle.
     * Local density represents the number of particles that exist at a given distance to
     * particle i. Based upon Euclidean and cutoff distance from this particle to another.
     *
     * @param swarm The swarm in which the particle is part of.
     * @return The density calculated for the particle
     */
    public void setLocalDensity(Swarm<MultiAdaptiveParticle> swarm) {
        double localDensity = 0;
        for (MultiAdaptiveParticle particle : swarm.getAgents()) {
            localDensity = localDensity + Math.exp(
                    (-1) * Math.pow((getDistance(particle) / this.getCutoffDistance(particle, CUTOFF)), 2)
            );
        }
        this.localDensity = localDensity;
    }

    /**
     * The Average Global best is defined by summing all local bests of each subpopulation.
     *
     * @param populations         a list of populations which separates the swarm.
     * @param numberOfPopulations the number of populations which divide the swarm.
     * @return a float value which represents the average global best.
     */
    public double calculateAverageGlobalBest(List<Population<MultiAdaptiveParticle>> populations, int numberOfPopulations) {
        List<Double> globalBests = new ArrayList<Double>();
        for (Population<MultiAdaptiveParticle> population : populations) {
            globalBests.add(population.getLocalBest().stream().mapToDouble(Double::doubleValue).sum());

        }
        return globalBests.stream().mapToDouble(Double::doubleValue).sum() / numberOfPopulations;
    }

    /**
     * Compute the cutoff distance based on given percentile of all
     * pairwise Euclidean distances between points.
     *
     * @param particle   The particle to which we calculate the distance
     * @param percentile e.g.: 2.0 for the 2% cutoff
     * @return The computed cutoff distance d_c
     */
    public double getCutoffDistance(Particle particle, double percentile) {
        int n = particle.getPosition().size();
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
     * @param particle the particle to which this should be measured to
     * @return the Euclidean distance
     */
    public double getDistance( Particle particle) {
        List<Double> p = this.getPosition();
        List<Double> q = particle.getPosition();
        if (p.size() != q.size()) throw new IllegalArgumentException("Dim mismatch");

        double sumSq = IntStream.range(0, p.size())
                .mapToDouble(i -> {
                    double d = p.get(i) - q.get(i);
                    return d * d;
                })
                .sum();

        return Math.sqrt(sumSq);
    }

}
