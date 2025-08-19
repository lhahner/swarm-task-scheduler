package pgm.swarm.pso.core.decorators;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengerParticleTest {

    @Test
    void constructor_scalesEachDimensionWithinBounds_andMutatesSource() {
        AgingLeaderParticle source = new AgingLeaderParticle();
        List<Double> original = Arrays.asList(1.0, 2.0, 3.0);
        source.setPosition(new ArrayList<>(original)); // mutable list

        ChallengerParticle challenger = new ChallengerParticle(source);

        List<Double> srcPos = source.getPosition();
        List<Double> chPos  = challenger.getPosition();

        assertEquals(original.size(), chPos.size(), "Dimension count should be preserved");
        for (int i = 0; i < original.size(); i++) {
            double vOrig = original.get(i);
            double vSrc  = srcPos.get(i);
            double vCh   = chPos.get(i);

            // scaled value should be in [0, vOrig] for positive originals
            assertTrue(vCh >= 0.0 && vCh <= vOrig, "Value should be scaled into [0, original]");
            // source was mutated in place and challenger received that position
            assertEquals(vSrc, vCh, "Source and challenger positions should match element-wise");
        }
    }

    @Test
    void constructor_handlesZeroCoordinates() {
        AgingLeaderParticle source = new AgingLeaderParticle();
        source.setPosition(new ArrayList<>(Arrays.asList(0.0, 5.0)));

        ChallengerParticle challenger = new ChallengerParticle(source);

        List<Double> chPos = challenger.getPosition();
        assertEquals(0.0, chPos.get(0), 1e-12, "Zero coordinate should remain zero after scaling");
        assertTrue(chPos.get(1) >= 0.0 && chPos.get(1) <= 5.0, "Nonzero coordinate should be scaled into [0, original]");
    }
}