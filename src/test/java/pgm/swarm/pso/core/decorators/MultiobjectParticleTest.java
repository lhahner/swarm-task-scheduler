package pgm.swarm.pso.core.decorators;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiobjectParticleTest {

    @Test
    void setParticleBestValueForRandomPareto_returnsElementFromList() {
        MultiobjectParticle p = new MultiobjectParticle();

        List<Double> paretoFront = List.of(10.0, 20.0, 30.0);

        double result = p.setParticleBestValueForRandomPareto(paretoFront, paretoFront.size());

        // Should always return one of the elements from paretoFront
        assertTrue(paretoFront.contains(result));
    }

    @Test
    void setParticleBestValueForRandomPareto_withSingleElement_returnsThatElement() {
        MultiobjectParticle p = new MultiobjectParticle();

        List<Double> paretoFront = List.of(42.0);

        double result = p.setParticleBestValueForRandomPareto(paretoFront, 1);

        assertEquals(42.0, result, 1e-12);
    }
}
