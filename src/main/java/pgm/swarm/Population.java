package pgm.swarm;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * A swarm population refers to the number of individuals in a swarm-based optimization algorithm,
 * such as CPU-PSO and GPU-PSO.
 *
 * <p>The size of the population can vary depending on the specific problem being solved. Larger
 * populations may be necessary to achieve better optimization results. However, increasing the
 * swarm size does not always lead to significant improvements in precision.
 *
 * @param <T> the type of agent in the population, which should match the swarm type
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@Log4j2
public class Population<T extends Agent> {

    /** Agents considered as part of the population. */
    private List<T> population;

    /** Position of the local best agent. */
    private double[] localBest;

    /**
     * Adds an agent to the population.
     *
     * <p>A population is a subgroup inside the swarm and can be used to make agents run on
     * locally based problems.
     *
     * @param agent the agent to add
     * @return the added agent
     */
    public T addAgent(T agent) {
        population.add(agent);
        log.info(
                "Added type {} Agent to the population {}",
                agent.getClass().toString(),
                this.getClass().toString());
        return agent;
    }

    /**
     * Removes an agent from the population.
     *
     * <p>A population is a subgroup inside the swarm and can be used to make agents run on
     * locally based problems.
     *
     * @param agent the agent to remove
     * @return the removed agent
     */
    public T removeAgent(T agent) {
        population.remove(agent);
        log.info("Removed type {} Agent from the population", agent.getClass().toString());
        return agent;
    }
}
