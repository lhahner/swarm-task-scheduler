package pgm.swarm.pso.core.strategies;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
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

import java.util.ArrayList;
import java.util.List;

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
        Simulation simulation = new CloudSimPlus();
        DataCenterUtility dataCenterUtility = new DataCenterUtility();
        CloudLetUtility cloudLetUtility = new CloudLetUtility();
        VirtualMachineUtility virtualMachineUtility = new VirtualMachineUtility();
        dataCenterUtility.createDatacenter(simulation, 20, 10);
        DatacenterBrokerSimple datacenterBrokerSimple = new DatacenterBrokerSimple((CloudSimPlus) simulation);
        cloudLetUtility.generateCloudlets(2,2,20);

        // Adds VMs to VMList
        for (int i = 0; i < 5; i++) {
            virtualMachineUtility.addVm(i, 30, 5);
        }
        cloudLetUtility.generateCloudlets(2, 2, 20);
        double[][] test_positions = new double[3][2];

        test_positions[0][0] = 0.231;
        test_positions[0][1] = 1.212;

        test_positions[1][0] = 0.711;
        test_positions[1][1] = 0.112;

        test_positions[2][0] = 0.671;
        test_positions[2][1] = 0.712;

        CloudletSimple test_cls_1 = new CloudletSimple(0, 3, 2);
        CloudletSimple test_cls_2 = new CloudletSimple(1, 5, 3);
        CloudletSimple test_cls_3 = new CloudletSimple(2, 6, 7);

        ArrayList<CloudletSimple> test_cloudlets = new ArrayList<CloudletSimple>();

        test_cloudlets.add(test_cls_1);
        test_cloudlets.add(test_cls_2);
        test_cloudlets.add(test_cls_3);

        Vm test_vm_1 = new VmSimple(5, 3);
        Vm test_vm_2 = new VmSimple(1, 2);
        Vm test_vm_3 = new VmSimple(7, 6);

        ArrayList<Vm> test_vms = new ArrayList<Vm>();

        test_vms.add(test_vm_1);
        test_vms.add(test_vm_2);
        test_vms.add(test_vm_3);

        strat.setCloudTasks(test_cloudlets);
        strat.setCloudVms(test_vms);

        List<Double> test_position = List.of(Math.random(), Math.random(), Math.random());
        List<Double> test_velocities = List.of(test_positions[0][1], test_positions[1][1], test_positions[2][1]);
        Assertions.assertEquals((5.0 / (5.0 * 3.0)), strat.optimize(test_position, test_velocities, 5));

    }
}
