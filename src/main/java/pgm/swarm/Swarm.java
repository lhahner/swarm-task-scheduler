package pgm.swarm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class Swarm<T extends Agent> implements Iterable<T> {

	private int seed = ThreadLocalRandom.current().nextInt(10, 100 + 1);
	private int dimension;
	private Class<T> clazz;
	private List<T> agents;
	private double[] globalBests = {seed, seed};
	private double globalBest = seed;

	public Swarm(int size, Class<T> clazz) {
		this.clazz = clazz;
		this.agents = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			try {
				agents.add((T) new SwarmFactory<T>().getAgent(this.clazz));
			} catch (Exception e) {
				log.warn("Failed to create agent", e);
			}
		}
	}

	public Swarm(double start_x, double start_y, int size, Class<T> clazz) {
		this.clazz = clazz;
		this.agents = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			start_x = Math.sqrt(start_x);
			start_y = Math.sqrt(start_y);

			try {
				agents.add((T) new SwarmFactory<T>().getAgent(this.clazz));
			} catch (Exception e) {
				log.warn("Failed to create agent", e);
			}
		}
	}

	/**
	 * Sets the global optimum.
	 */
	public void setGlobalBests(double[] globalBests) {
		int len = Math.min(globalBests.length, this.globalBests.length);
		System.arraycopy(globalBests, 0, this.globalBests, 0, len);
	}

	/**
	 * Returns the complete swarm and its values as a String.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n");
		for (T particle : this.getAgents()) {
			sb.append(particle);
		}
		return sb.toString();
	}

	@Override
	public @NotNull Iterator<T> iterator() {
		return agents.iterator();
	}
}
