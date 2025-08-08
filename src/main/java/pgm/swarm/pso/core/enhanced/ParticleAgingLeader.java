package pgm.swarm.pso.core.enhanced;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Agent;

/**
 * Implements an optimization technique that uses an aging model to
 * prevent premature convergence without slowing down normal PSO
 * convergence. A leader is chosen from the swarm and given an age and lifetime.
 * If a leader's life is exhausted, a new challenger is created and replaces the
 * previous leader if the new challenger has greater leadership power than
 * the former leader.
 *
 * @source <a href="https://doi.org/10.1155/2023/3916735">Reference</a>
 **/
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class ParticleAgingLeader extends ParticleModified implements Agent {

    /**
     * Hyperparameter for setting the max duration of time to be a leader.
     */
    private static final int MAX_AGE = 5;
    /**
     * This leader flag is used to flag a certain particle as leader,
     * which is equivalent to the global optimum.
     */
    private boolean isLeader = false;

    /**
     * If this particle is selected as leader, it gets an age assigned
     * which limits the dominance of that leader.
     */
    private int leaderAge = 0;

    /**
     * This challenger flag will be set if the life span of the leader
     * is over, then the position of this particle is considered as the
     * global best which the particles will follow.
     */
    private boolean isChallenger = false;

    /**
     * Increment the Age of the leader by one, if it exceeds the threshold
     * as defined, in the hyperparameter MAX_LEADER, this leader is set
     * back to normal.
     */
    public void incrementLeaderAge(){
        if(this.isLeader() && leaderAge < MAX_AGE)
            this.leaderAge++;
    }
}
