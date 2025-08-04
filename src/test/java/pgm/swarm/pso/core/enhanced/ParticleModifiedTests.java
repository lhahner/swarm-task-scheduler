package pgm.swarm.pso.core.enhanced;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParticleModifiedTests {
    @Test
    public void calculateVelocity_isCorrectUsageApplicable(){
        final double[]
                currentVelocity = {0.9, 0.8},
                position = {0.9, 0.8},
                gBest = {0.9, 0.8},
                lBest = {0.9, 0.8};
        final double
                constant1 = 0.2,
                constant2 = 0.2;
        final double
                random1 = 0.1,
                random2 = 0.1;
        ParticleModified
                pm = new ParticleModified(0.9);
        pm.calculateVelocity(
                currentVelocity, constant1, lBest, position, constant2, gBest, random1, random2
        );
        assertEquals(0.81, pm.getVelo()[0], 0.0f);
    }

    @Test
    public void calculateVelocity_areMultipleDimensionsApplicable(){
        //TODO
    }
}
