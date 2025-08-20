package pgm.swarm.pso.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Agent;

/**
 * Represents a particle in the Particle Swarm Optimization (PSO) algorithm.
 *
 * <p>Each particle has a position and a velocity in the search space. It remembers its own best
 * position found so far and adjusts its movement based on three components:
 *
 * <ul>
 *   <li><b>Inertia</b> – momentum from its previous velocity
 *   <li><b>Cognitive learning</b> – attraction towards its own best-known position
 *   <li><b>Social learning</b> – attraction towards the global best position found by the swarm
 * </ul>
 *
 * <p>This implementation models the particle’s state as lists of doubles.
 */
@Setter
@Getter
@Log4j2
public class Particle implements Agent {

    /** Lower bound for random initialization of inertia weight. */
    protected static final double INERTIA_WEIGHT_START_RANGE = 0.9;

    /** Upper bound for random initialization of inertia weight. */
    protected static final double INERTIA_WEIGHT_END_RANGE = 1.2;

    /**
     * Cognitive learning factor (c1).
     *
     * <p>Controls the particle’s tendency to move towards its personal best position.
     */
    protected static final double COGNITIVE_LEARNING_FACTOR = 2.0;

    /**
     * Social learning factor (c2).
     *
     * <p>Controls the particle’s tendency to move towards the global best position.
     */
    protected static final double SOCIAL_LEARNING_FACTOR = 2.0;

    /** Initial boundary value used to initialize the particle’s personal best. */
    protected static final double UPPER_STARTING_BOUNDARY = 10;

    /** Current position of the particle in the search space. */
    private List<Double> position;

    /** Current velocity of the particle in the search space. */
    private List<Double> velocity;

    /**
     * Inertia weight factor that scales the previous velocity.
     *
     * <p>Higher values encourage exploration of the search space, while lower values encourage
     * exploitation around known good solutions.
     */
    protected double inertiaWeight;

    /** Best position found so far by this particle (personal best). */
    private List<Double> particlesBest;

    /**
     * Constructs a new particle with a randomly generated inertia weight within the predefined range.
     */
    public Particle() {
        this.setInertiaWeight(
                this.calculateInertiaWeight(INERTIA_WEIGHT_START_RANGE, INERTIA_WEIGHT_END_RANGE));
    }

    /**
     * Updates the particle’s position by adding the velocity vector to it.
     *
     * @param currentPosition the current position of the particle
     * @param velocity the velocity vector to apply
     * @throws IllegalArgumentException if {@code currentPosition} and {@code particlesBest} have
     *     different dimensions
     */
    public void calculateNewPosition(List<Double> currentPosition, List<Double> velocity) {
        if (currentPosition.size() != particlesBest.size()) {
            throw new IllegalArgumentException("currentPosition.size() != particlesBest.size()");
        }
        List<Double> newPosition = new ArrayList<>(currentPosition);
        IntStream.range(0, particlesBest.size())
                .forEach(i -> newPosition.set(i, currentPosition.get(i) + velocity.get(i)));
        this.position = newPosition;
    }

    /**
     * Updates the particle’s velocity using the standard PSO update rule:
     *
     * <pre>
     * v(t+1) = inertia * v(t)
     *        + c1 * rand() * (personalBest - position)
     *        + c2 * rand() * (globalBest - position)
     * </pre>
     *
     * @param velocity the current velocity (modified in place)
     * @param particlesBest the particle’s personal best position
     * @param position the particle’s current position
     * @param globalBest the global best position found by the swarm
     * @throws IllegalArgumentException if the input lists do not all have the same dimension
     */
    public void calculateVelocity(
            List<Double> velocity,
            List<Double> particlesBest,
            List<Double> position,
            List<Double> globalBest) {
        if(velocity == null){
           velocity = new ArrayList<>();
           velocity = position;
        }
        List<Double> newVelocity = new ArrayList<Double>(velocity);
        if (velocity.size() != position.size()
                || position.size() != particlesBest.size()
                || position.size() != globalBest.size()) {
            throw new IllegalArgumentException("All input arrays must have the same length");
        }

        for (int i = 0; i < velocity.size(); i++) {
            newVelocity.set(
                    i,
                    (inertiaWeight * velocity.get(i))
                            + (COGNITIVE_LEARNING_FACTOR
                            * Math.random()
                            * (particlesBest.get(i) - position.get(i)))
                            + (SOCIAL_LEARNING_FACTOR
                            * Math.random()
                            * (globalBest.get(i) - position.get(i))));
        }
        this.velocity = newVelocity;
        log.info("Ran calculateVelocity() in class Particle, calculated: {}", List.of(velocity));
    }

    /**
     * Returns a string representation of the particle, including its position, velocity, inertia
     * weight, and personal best.
     *
     * @return a formatted string representation of the particle
     */
    @Override
    public String toString() {
        return "Particle{"
                + "position=" + position
                + ", velocity=" + velocity
                + ", inertiaWeight=" + inertiaWeight
                + ", particlesBest=" + particlesBest
                + "} \n";
    }

    /**
     * Generates a random inertia weight within the given range. The randomness helps diversify the
     * swarm’s behavior by balancing exploration and exploitation.
     *
     * @param min the lower bound of the inertia weight
     * @param max the upper bound of the inertia weight
     * @return a random inertia weight within the range {@code [min, max]}
     */
    public double calculateInertiaWeight(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }
}
