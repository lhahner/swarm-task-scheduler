package pgm.swarm.pso.core.enhanced;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Agent;
import pgm.swarm.pso.core.Particle;

import java.util.Arrays;

/**
 * Represents a modified particle for an enhanced Particle Swarm Optimization (PSO) algorithm.
 * <p>
 * This implementation incorporates improvements proposed in the paper:
 * <a href="https://doi.org/10.1007/s12652-023-04541-9">
 * Improved Particle Swarm Optimization Algorithm (2023)</a>.
 * </p>
 * Extends the base Particle class and implements the Agent interface.
 * Adds dynamic inertia weight to influence the particle's velocity during the optimization process.
 */
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class ParticleModified extends Particle implements Agent {

	/**
	 * Inertia weight factor that scales the influence of the previous velocity.
	 * Helps balance exploration (global search) and exploitation (local refinement).
	 */
	private double inertiaWeight;

	/**
	 * Constants defining the range within which the inertia weight is initialized.
	 */
	private final static double START_RANGE = 0.9;
	private final static double END_RANGE = 1.2;

	/**
	 * Default constructor.
	 * Initializes the particle with a randomly generated inertia weight within the defined range.
	 */
	public ParticleModified() {
		this.setInertiaWeight(this.calculateInertiaWeight(START_RANGE, END_RANGE));
	}

	/**
	 * Calculates the new velocity of the particle based on the PSO velocity update rule.
	 * The formula considers inertia, cognitive, and social components.
	 *
	 * @param currentVelocity     Current velocity of the particle (2D vector).
	 * @param cognitiveLearningFactor          Cognitive coefficient (attraction to local best).
	 * @param bestPositions     Local best position of the particle (2D).
	 * @param position          Current position of the particle (2D).
	 * @param socialLearningFactor          Social coefficient (attraction to global best).
	 * @param globalBestPositions  Global best position found by the swarm (2D).
	 * @param randomValueOne          Random value [0, 1] for stochastic effect on local influence.
	 * @param randomValueTwo          Random value [0, 1] for stochastic effect on global influence.
	 */
	@Override
	public void calculateVelocity(
			double[] currentVelocity,
			double cognitiveLearningFactor,
			double[] bestPositions,
			double[] position,
			double socialLearningFactor,
			double[] globalBestPositions,
			double randomValueOne,
			double randomValueTwo) {
		if (currentVelocity.length != position.length
				|| position.length != bestPositions.length
				|| position.length != globalBestPositions.length) {
			throw new IllegalArgumentException("All input arrays must have the same length");
		}
		double[] newVelocity = new double[currentVelocity.length];
		for (int i = 0; i < currentVelocity.length; i++) {
			newVelocity[i] = inertiaWeight * currentVelocity[i]
					+ cognitiveLearningFactor * randomValueOne * (bestPositions[i] - position[i])
					+ socialLearningFactor * randomValueTwo * (globalBestPositions[i] - position[i]);
		}
		this.setVelo(newVelocity);
		log.info("Ran calculateVelocity() in class ParticleModified, calculated: {}", Arrays.toString(this.getVelo()));
	}

	/**
	 * Randomly generates an inertia weight within a specified range.
	 * This randomness can help promote diversity in the swarm's behavior,
	 * balancing between global exploration and local exploitation.
	 *
	 * @param min Lower bound of inertia weight.
	 * @param max   Upper bound of inertia weight.
	 * @return A random inertia weight within [min, end_range].
	 */
	public double calculateInertiaWeight(double min, double max) {
		return ((Math.random() * (max - min)) + min);
	}
}
