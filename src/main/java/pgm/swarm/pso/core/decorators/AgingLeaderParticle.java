package pgm.swarm.pso.core.decorators;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Agent;
import pgm.swarm.pso.core.Particle;

/**
 * A particle variant for an Aging-Leader PSO mechanism.
 *
 * <p>This class augments a standard {@link Particle} with an "age" that limits how long a particle
 * may act as the swarm leader. The mechanism helps prevent premature convergence without unduly
 * slowing normal PSO progress: a leader can guide the swarm only up to a maximum tenure, after
 * which it is considered too old and should relinquish leadership (e.g., to a challenger).
 *
 * <p><b>Reference:</b> <a href="https://doi.org/10.1155/2023/3916735">https://doi.org/10.1155/2023/3916735</a>
 *
 * @see Particle
 * @see ChallengerParticle
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class AgingLeaderParticle extends Particle implements Agent {

    /**
     * Maximum allowed age (tenure) for a leader before it must be replaced.
     *
     * <p>This hyperparameter controls how long a leader can dominate the swarm. When
     * {@link #leaderAge} reaches or exceeds this value, {@link #isTooOld()} returns {@code true}.
     */
    private static final int MAX_AGE = 5;

    /**
     * Current age of this particle while acting as the leader.
     *
     * <p>The age starts at {@code 0} and is typically incremented once per iteration while the
     * particle remains leader.
     */
    private int leaderAge = 0;

    /**
     * Increments the leader's age by one, up to {@link #MAX_AGE}.
     *
     * <p>If {@code leaderAge} is already equal to {@link #MAX_AGE}, it will not increase further.
     * The caller may use {@link #isTooOld()} to decide when to revoke leadership.
     */
    public void incrementLeaderAge() {
        if (leaderAge < MAX_AGE) {
            this.leaderAge++;
        }
    }

    /**
     * Returns whether this leader has exceeded or met its maximum allowed age.
     *
     * @return {@code true} if {@code leaderAge >= MAX_AGE}; {@code false} otherwise
     */
    public boolean isTooOld() {
        return leaderAge >= MAX_AGE;
    }
}
