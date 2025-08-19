package pgm.swarm.pso.core.decorators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgingLeaderParticleTest {
    @Test
    void defaultAge_isZero_andNotTooOld() {
        AgingLeaderParticle p = new AgingLeaderParticle();
        assertEquals(0, p.getLeaderAge());
        assertFalse(p.isTooOld());
    }

    @Test
    void increment_increasesUntilMax_thenStops() {
        AgingLeaderParticle p = new AgingLeaderParticle();

        // Increment 4 times: age should be 4 and not too old yet
        for (int i = 0; i < 4; i++) p.incrementLeaderAge();
        assertEquals(4, p.getLeaderAge());
        assertFalse(p.isTooOld());

        // 5th increment reaches max: age==5, now too old
        p.incrementLeaderAge();
        assertEquals(5, p.getLeaderAge());
        assertTrue(p.isTooOld());

        // Further increments should not increase beyond max
        for (int i = 0; i < 3; i++) p.incrementLeaderAge();
        assertEquals(5, p.getLeaderAge());
        assertTrue(p.isTooOld());
    }

    @Test
    void isTooOld_trueExactlyAtMaxAge() {
        AgingLeaderParticle p = new AgingLeaderParticle();
        for (int i = 0; i < 5; i++) p.incrementLeaderAge();
        assertEquals(5, p.getLeaderAge());
        assertTrue(p.isTooOld());
    }
}
