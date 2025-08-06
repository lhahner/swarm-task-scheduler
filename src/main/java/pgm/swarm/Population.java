package pgm.swarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import java.util.List;

/**
 * A 'swarm population' refers to the number of individuals in a swarm-based optimization algorithm, such as
 * CPU-PSO and GPU-PSO. The size of the population can vary depending on the specific problem being solved, with
 * larger populations sometimes necessary to achieve better optimization results. However, the study shows that
 * increasing the swarm size does not always lead to significant improvements in precision.
 * @param <T> The Type of the subpopulation, should be the same as the required swarm.
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@Log4j2
public class Population<T extends Agent> {

    /**
     * Contains agents that are considered for the population.
     */
    private List<T> population;

    /**
     * Add an Agent to the population. A population is a subgroup inside the swarm
     * and can be used to make agents run on local based problems
     * @param agent The agent which should be added to the population
     * @return The added to the population
     */
    public T addAgent(T agent) {
        population.add(agent);
        log.info("Added type{} Agent to the population {}", agent.getClass().toString(), this.getClass().toString());
        return agent;
    }

    /**
     * Remove an Agent from the population. A population is a subgroup inside the swarm
     * and can be used to make agents run on local based problems.
     * @param agent The Agent which should be removed from the population
     * @return The removed Agent
     */
    public T removeAgent(T agent) {
        population.remove(agent);
        log.info("Removed type{} Agent from the population", agent.getClass().toString());
       return agent;
    }
}
