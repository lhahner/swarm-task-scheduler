package pgm.swarm.pso.core.decorators;

import org.junit.jupiter.api.Test;
import pgm.swarm.Population;
import pgm.swarm.Swarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiAdaptiveParticleTest {

    private static MultiAdaptiveParticle particle(Double... coords) {
        MultiAdaptiveParticle p = new MultiAdaptiveParticle();
        p.setPosition(new ArrayList<>(Arrays.asList(coords)));
        return p;
    }

    @Test
    void getDistance_returnsExpectedValue() {
        MultiAdaptiveParticle a = particle(0.0, 0.0);
        MultiAdaptiveParticle b = particle(3.0, 4.0);

        // sqrt(3^2 + 4^2) = 5
        assertEquals(5.0, a.getDistance(b), 1e-12);
    }

}
