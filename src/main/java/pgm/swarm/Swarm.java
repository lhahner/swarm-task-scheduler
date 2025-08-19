package pgm.swarm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import pgm.swarm.pso.core.Particle;

/**
 * A social-behavior–based set of agents that move in an N-dimensional space to optimize toward a
 * minimal or maximal value.
 *
 * @param <T> the type of the swarm
 */
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class Swarm<T extends Agent> implements Iterable<T> {

	/** Initial boundary value used to initialize the particle’s personal best. */
	private static final double UPPER_STARTING_BOUNDARY = 100;

	/** Type of the swarm. */
	private Class<T> type;

	/** Agents inside a swarm. */
	private List<T> agents;

	/** List of the best solutions for this swarm. */
	private List<Double> globalBests =
			List.of(UPPER_STARTING_BOUNDARY, UPPER_STARTING_BOUNDARY);

	/** Best scalar solution. */
	private double globalBest = UPPER_STARTING_BOUNDARY;

	/**
	 * Constructs a swarm based on the social behavior of insects or animals.
	 *
	 * @param position the initial position for all agents
	 * @param velocity the initial velocity for all agents
	 * @param size the number of agents
	 * @param type the type of the swarm
	 */
	public Swarm(List<Double> position, List<Double> velocity, int size, Class<T> type) {
		this.type = type;
		this.agents = new ArrayList<>();

		IntStream.range(0, size)
				.forEach(
						i -> {
							try {
								agents.add((T) new SwarmFactory<T>().getAgent(position, velocity, this.type));
							} catch (Exception e) {
								log.warn("Failed to create agent {} causing {}", i, e);
							}
						});
		this.setGlobalBests(position);
	}

	/** Returns the complete swarm and its values as a string. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n");
		for (T particle : this.getAgents()) {
			sb.append(particle);
		}
		return sb.toString();
	}


	/** Makes swarm iterable. */
	@Override
	public @NotNull Iterator<T> iterator() {
		return agents.iterator();
	}
}
