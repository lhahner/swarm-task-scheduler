package pgm.swarm.pso.core.strategies;

import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.NoVisualizationStrategy;
import pgm.visualization.VisualizationStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParticleSwarmOptimizationTests {
    private ParticleSwarmOptimization pso;

    @BeforeEach
    void setUp() {
        ArrayList<CloudletSimple> tasks = new ArrayList<>();
        ArrayList<Vm> vms = new ArrayList<>();
        Evaluation evaluation = new Evaluation();
        VisualizationStrategy visualizationStrategy = new NoVisualizationStrategy();
        pso = new ParticleSwarmOptimization(visualizationStrategy, evaluation, tasks, vms);

        tasks.add(new CloudletSimple(1000, 1));
        tasks.add(new CloudletSimple(2000, 1));
        pso.setCloudTasks(tasks);


        vms.add(new VmSimple(1000, 1));
        vms.add(new VmSimple(2000, 1));
        pso.setCloudVms(vms);
    }

    @Test
    void testResetParticlesOutOfRange_Positive() {
        Particle particle = new Particle();
        particle.setPosition(new ArrayList<>(Arrays.asList(10.0, 20.0))); // out of range

        ArrayList<VmSimple> vms = new ArrayList<>();
        vms.add(new VmSimple(1000, 1));
        vms.add(new VmSimple(1000, 1));

        ArrayList<CloudletSimple> tasks = new ArrayList<>();
        tasks.add(new CloudletSimple(1000, 1));

        pso.resetParticlesOutOfRange(particle, new ArrayList<>(vms), tasks);

        int scalingFactor = Math.max(tasks.size(), vms.size());
        assertTrue(particle.getPosition().get(0) < scalingFactor);
        assertTrue(particle.getPosition().get(1) < scalingFactor);
    }

    @Test
    void testResetParticlesOutOfRange_Negative_NoChange() {
        Particle particle = new Particle();
        particle.setPosition(new ArrayList<>(Arrays.asList(0.0, 0.0))); // within range

        ArrayList<VmSimple> vms = new ArrayList<>();
        vms.add(new VmSimple(1000, 1));
        vms.add(new VmSimple(1000, 1));

        ArrayList<CloudletSimple> tasks = new ArrayList<>();
        tasks.add(new CloudletSimple(1000, 1));

        ArrayList<Double> originalPosition = new ArrayList<>(particle.getPosition());

        pso.resetParticlesOutOfRange(particle, new ArrayList<>(vms), tasks);
        assertEquals(originalPosition, particle.getPosition(), "Position should remain unchanged");
    }

    @Test
    void testOptimize_Positive() {
        List<Double> initialPosition = Arrays.asList(0.0, 0.0);
        List<Double> initialVelocity = Arrays.asList(0.0, 0.0);
        int swarmSize = 3;
        Swarm<Particle> swarm = new Swarm<>(initialPosition, initialVelocity, swarmSize, Particle.class);

        pso.optimize(swarm, initialPosition, initialVelocity, swarmSize);

        assertEquals(swarmSize, swarm.getAgents().size());
        for (Particle particle : swarm.getAgents()) {
            assertNotNull(particle.getPosition());
            assertNotNull(particle.getVelocity());
        }
    }
}
