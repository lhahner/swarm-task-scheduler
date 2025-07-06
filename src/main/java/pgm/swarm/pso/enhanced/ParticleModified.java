package pgm.swarm.pso.enhanced;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pgm.swarm.Agent;
import pgm.swarm.pso.core.Particle;

/**
 * Represents a modified particle for an enhanced Particle Swarm Optimization (PSO) algorithm.
 * <p>
 * This implementation incorporates improvements proposed in the paper:
 * <a href="https://doi.org/10.1007/s12652-023-04541-9">
 * Improved Particle Swarm Optimization Algorithm (2023)</a>.
 * </p>
 * Extends the basic Particle and implements the Agent interface.
 * It introduces an inertia weight parameter to influence the velocity update.
 */
@Getter
@Setter
@NoArgsConstructor
public class ParticleModified extends Particle implements Agent {

	/**
	 * Inertia weight factor that controls the influence of the previous velocity on the current velocity.
	 */
	double interia_w;

	/**
	 * Calculates and updates the velocity of the particle based on the current velocity,
	 * cognitive and social components, and random coefficients.
	 * <p>
	 * The velocity is updated according to the formula:
	 * <pre>
	 * velocity = inertia_w * current_velocity
	 *          + c1 * r1 * (local_best_position - current_position)
	 *          + c2 * r2 * (global_best_position - current_position)
	 * </pre>
	 * where r1 and r2 are random values between 0 and 1.
	 * </p>
	 *
	 * @param cur_velo the current velocity vector of the particle.
	 * @param c_1 cognitive coefficient, weighting the attraction to the local best position.
	 * @param pos_best the particle's best known position (local best).
	 * @param pos the current position of the particle.
	 * @param c_2 social coefficient, weighting the attraction to the global best position.
	 * @param global_best the best known position found by any particle (global best).
	 * @param r_1 random factor for stochastic influence toward local best.
	 * @param r_2 random factor for stochastic influence toward global best.
	 */
	@Override
	public void calcVelo(double[] cur_velo, double c_1, double[] pos_best, double[] pos, double c_2, double[] global_best,
						 double r_1, double r_2) {
		this.calcInteriaWeight();
		double velo_x = (interia_w * cur_velo[0] + c_1 * r_1 * (pos_best[0] - pos[0]) + c_2 * r_2 * (global_best[0] - pos[0]));
		double velo_y = (interia_w * cur_velo[1] + c_1 * r_1 * (pos_best[1] - pos[1]) + c_2 * r_2 * (global_best[1] - pos[1]));

		this.setVelo(velo_x, velo_y);
	}

	/**
	 * Randomly initializes the inertia weight to a value between 0.9 and 1.2.
	 * <p>
	 * The inertia weight controls the trade-off between exploration and exploitation
	 * by scaling the contribution of the previous velocity.
	 * </p>
	 */
	public void calcInteriaWeight() {
		this.interia_w = ((Math.random() * (1.2 - 0.9)) + 0.9);
	}
}
