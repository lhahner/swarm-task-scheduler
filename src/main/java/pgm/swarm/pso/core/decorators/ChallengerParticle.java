package pgm.swarm.pso.core.decorators;

import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import pgm.swarm.Agent;
import pgm.swarm.pso.core.Particle;

/**
 * A {@code ChallengerParticle} used by the Aging-Leader PSO variant to reinvigorate exploration
 * whenever the current leader becomes ineffective or too old.
 *
 * <p>The challenger is derived from an existing particle and initialized by randomly scaling each
 * coordinate of the source particle's position vector, encouraging movement toward new regions of
 * the search space while remaining related to the swarm's current state.
 *
 * <p>This class extends {@link AgingLeaderParticle} and can temporarily act as the leader in place
 * of the aging leader, depending on the algorithm's leadership and lifetime rules.
 *
 * <p><b>Reference</b>:
 * <a href="https://doi.org/10.1155/2023/3916735">https://doi.org/10.1155/2023/3916735</a>
 */
@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class ChallengerParticle extends AgingLeaderParticle implements Agent {

    /**
     * Constructs a challenger by taking the given particle's position and multiplying each dimension
     * by a random factor in {@code [0, 1)}.
     *
     * <p><b>Implementation note:</b> This constructor obtains the list returned by
     * {@code particle.getPosition()} and updates it in place before assigning it to this instance via
     * {@link #setPosition(List)}. If the underlying implementation of {@code getPosition()} returns a
     * mutable list that is shared with the input particle, the input particle's position may be
     * mutated as a side effect. If this is undesired, ensure {@code getPosition()} returns a defensive
     * copy or make a copy before mutation.
     *
     * @param particle the source particle whose position seeds the challenger
     */
    public ChallengerParticle(Particle particle) {
        List<Double> position = particle.getPosition();
        IntStream.range(0, position.size())
                .forEach(i -> position.set(i, position.get(i) * Math.random()));
        this.setPosition(position);
    }
}