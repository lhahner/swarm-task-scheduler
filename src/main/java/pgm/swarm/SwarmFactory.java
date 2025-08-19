package pgm.swarm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import pgm.swarm.aco.core.Ant;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.AgingLeaderParticle;
import pgm.swarm.pso.core.decorators.MultiAdaptiveParticle;
import pgm.swarm.pso.core.decorators.MultiobjectParticle;

/**
 * Factory for generating agents necessary for a swarm.
 *
 * <p>Whenever a swarm is created, this factory should be called to produce the required agents.
 *
 * @author Lennart Hahner
 * @version 1.1.0
 */
public class SwarmFactory<T> {

	/**
	 * Creates a new {@link Particle} agent with the given position and velocity.
	 *
	 * @param position the initial position of the agent
	 * @param velocity the initial velocity of the agent
	 * @return a new {@link Particle}
	 * @throws IllegalArgumentException if {@code position} and {@code velocity} have different sizes
	 */
	public Agent getAgent(@NotNull List<Double> position, @NotNull List<Double> velocity) {
		if (position.size() != velocity.size()) {
			throw new IllegalArgumentException("position and velocity sizes don't match");
		}
		Particle particle = new Particle();
		particle.setPosition(position);
		particle.setVelocity(velocity);
		return particle;
	}

	/**
	 * Creates a new agent of the given type with the specified position and velocity.
	 *
	 * <p>The type is identified by the {@code agentType} parameter. For example, if
	 * {@code Particle.class} is passed, this method will return a {@link Particle}.
	 *
	 * @param position the initial position of the agent
	 * @param velocity the initial velocity of the agent
	 * @param agentType the class type of the agent (e.g., {@link Particle}, {@link Ant})
	 * @return the created agent
	 * @throws UnsupportedOperationException if the requested agent type is not yet implemented
	 * @throws IllegalArgumentException if the given agent type is not supported
	 */
	public Agent getAgent(
			@NotNull List<Double> position, @NotNull List<Double> velocity, Class<T> agentType) {
		if (agentType.equals(Particle.class)) {
			Particle particle = new Particle();
			particle.setPosition(position);
			particle.setParticlesBest(position);
			particle.setVelocity(velocity);
			return particle;
		} else if (agentType.equals(MultiobjectParticle.class)) {
			MultiobjectParticle particle = new MultiobjectParticle();
			particle.setPosition(position);
			particle.setParticlesBest(position);
			particle.setVelocity(velocity);
			return particle;
		} else if (agentType.equals(MultiAdaptiveParticle.class)) {
			MultiAdaptiveParticle particle = new MultiAdaptiveParticle();
			// Only here required because we compute the distance
			List<Double> multiplied = position.stream()
					.map(n -> n * Math.random())
					.toList();
			particle.setPosition(multiplied);
			particle.setParticlesBest(position);
			particle.setVelocity(velocity);
			return particle;
		} else if (agentType.equals(AgingLeaderParticle.class)) {
			AgingLeaderParticle particle = new AgingLeaderParticle();
			particle.setPosition(position);
			particle.setParticlesBest(position);
			particle.setVelocity(velocity);
			return particle;
		} else if (agentType.equals(Ant.class)) {
			Ant particle = new Ant();
		}
		throw new IllegalArgumentException("Agent type not supported");
	}
}
