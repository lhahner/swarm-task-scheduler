package pgm.swarm.pso.core.strategies;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.Simulation;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pgm.simulation.CloudLetUtility;
import pgm.simulation.DataCenterUtility;
import pgm.simulation.VirtualMachineUtility;
import pgm.swarm.Population;
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.decorators.MultiAdaptiveParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MultiAdaptiveParticleSwarmOptimizationTest {

    @Test
    void initalizePopulation_setsCenterAndContainsIt() {
        MultiAdaptiveParticleSwarmOptimization strat = new MultiAdaptiveParticleSwarmOptimization();
        MultiAdaptiveParticle center = new MultiAdaptiveParticle();

        Population<MultiAdaptiveParticle> pop = strat.initalizePopulation(center);

        assertSame(center, pop.getCenter());
        assertTrue(pop.getAgents().contains(center));
        assertEquals(1, pop.getAgents().size());
    }

    @Test
    void setCenterOfPopulations_createsAtLeastOnePopulation() {
        List<Double> pos = List.of(0.0, 0.0);
        List<Double> vel = List.of(0.0, 0.0);
        Swarm<MultiAdaptiveParticle> swarm = new Swarm<>(pos, vel, 2, MultiAdaptiveParticle.class);

        MultiAdaptiveParticleSwarmOptimization strat = new MultiAdaptiveParticleSwarmOptimization();
        strat.setCenterOfPopulations(swarm);

        assertNotNull(strat.getPopulations());
        assertFalse(strat.getPopulations().isEmpty());

        MultiAdaptiveParticle center = strat.getPopulations().get(0).getCenter();
        assertTrue(swarm.getAgents().contains(center));
    }

    @Test
    void joinCenterOfPopulations_assignsRemainingParticleToPopulation() {
        List<Double> pos = List.of(0.0, 0.0);
        List<Double> vel = List.of(0.0, 0.0);
        Swarm<MultiAdaptiveParticle> swarm = new Swarm<>(pos, vel, 2, MultiAdaptiveParticle.class);

        MultiAdaptiveParticleSwarmOptimization strat = new MultiAdaptiveParticleSwarmOptimization();
        strat.setCenterOfPopulations(swarm);

        int beforeJoinSizes = strat.getPopulations().stream()
                .mapToInt(p -> p.getAgents().size()).sum();

        strat.joinCenterOfPopulations();

        int afterJoinSizes = strat.getPopulations().stream()
                .mapToInt(p -> p.getAgents().size()).sum();

        assertTrue(afterJoinSizes >= beforeJoinSizes);
    }

    @Test
    void optimize_smallDataCenter(){
        MultiAdaptiveParticleSwarmOptimization strat = new MultiAdaptiveParticleSwarmOptimization();
        CloudSimPlus simulation = new CloudSimPlus();
        DataCenterUtility dataCenterUtility = new DataCenterUtility();
        CloudLetUtility cloudLetUtility = new CloudLetUtility();
        VirtualMachineUtility virtualMachineUtility = new VirtualMachineUtility();
        dataCenterUtility.createDatacenter(simulation, 20, 10);
        DatacenterBrokerSimple datacenterBrokerSimple = new DatacenterBrokerSimple(simulation);
        cloudLetUtility.generateCloudlets(2,2,20);

        // Adds VMs to VMList
        for (int i = 0; i < 5; i++) {
            virtualMachineUtility.addVm(i, 30, 5);
        }
        cloudLetUtility.generateCloudlets(2, 2, 20);

        CloudletSimple bestCloudlet = new CloudletSimple(1, 1, 2);
        List<CloudletSimple> cloudlets = List.of(
                 new CloudletSimple(0, 3, 2), bestCloudlet
                ,new CloudletSimple(2, 3, 7), new CloudletSimple(3, 2, 3)
                ,new CloudletSimple(4, 7, 8), new CloudletSimple(5, 4, 5)
                ,new CloudletSimple(6, 5, 3), new CloudletSimple(7, 2, 3)
                ,new CloudletSimple(8, 8, 9)
        );

        VmSimple bestVm = new VmSimple(0, 5, 6);
        List<VmSimple> vms = List.of(
                 bestVm, new VmSimple(1, 5, 3)
                ,new VmSimple(2, 1, 2), new VmSimple(3, 5, 3)
                ,new VmSimple(4, 5, 3), new VmSimple(5, 5, 3)
        );

        strat.setCloudTasks(cloudlets);
        strat.setCloudVms(vms);

        List<Double> positions  = new ArrayList<>();
        IntStream.range(0, cloudlets.size()).forEach(i -> {
            positions. add(Math.random());
        });

        List<Double> velocities = new ArrayList<>();
        IntStream.range(0, cloudlets.size()).forEach(i -> {
            velocities. add(Math.random());
        });

       double minimalMakespan = (bestCloudlet.getLength() / (bestVm.getMips() * bestVm.getFreePesNumber()));

        Assertions.assertEquals(minimalMakespan, strat.optimize(positions, velocities, 500));
    }
}
