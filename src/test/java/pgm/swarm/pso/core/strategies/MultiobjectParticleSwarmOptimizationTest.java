package pgm.swarm.pso.core.strategies;

import lombok.extern.log4j.Log4j2;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.Simulation;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgm.simulation.CloudLetUtility;
import pgm.simulation.DataCenterUtility;
import pgm.simulation.VirtualMachineUtility;
import pgm.swarm.TestHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class MultiobjectParticleSwarmOptimizationTest {

    private MultiobjectParticleSwarmOptimization optimization;
    private TestHelper helper;

    @BeforeEach
    void setUp(){
        this.optimization = new MultiobjectParticleSwarmOptimization();
        this.helper = new TestHelper();
    }

    @Test
    void updateParetoFront_dominantCaseSwapListUpdated(){
        TreeMap<Double, List<Double>> paretoFront = new TreeMap<>();
        paretoFront.put(0.31, List.of(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        paretoFront.put(0.92, List.of(0.33, 0.413, 0.3233, 0.233, 0.243, 0.533, 0.11, 0.24));
        paretoFront.put(0.233, List.of(0.73, 0.513, 0.4233, 0.333, 0.228, 0.933, 0.51, 0.024));
        paretoFront.put(0.123, List.of(0.13, 0.113, 0.7233, 0.033, 0.243, 0.433, 0.91, 0.54));

        List<Double> candidatePosition = List.of(helper.getRandomNumber(0, 0.9), helper.getRandomNumber(0, 0.3));
        double candidate = 0.1;
        assertTrue(optimization.updateParetoFront(paretoFront, candidatePosition, candidate));
        Assertions.assertEquals(candidatePosition, paretoFront.get(0.1));
    }

    @Test
    void updateParetoFront_NonDominantCaseListNotUpdatedPareto(){
        TreeMap<Double, List<Double>> paretoFront = new TreeMap<>();
        paretoFront.put(0.31, List.of(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        paretoFront.put(0.92, List.of(0.33, 0.413, 0.3233, 0.233, 0.243, 0.533, 0.11, 0.24));
        paretoFront.put(0.233, List.of(0.73, 0.513, 0.4233, 0.333, 0.228, 0.933, 0.51, 0.024));
        paretoFront.put(0.123, List.of(0.13, 0.113, 0.7233, 0.033, 0.243, 0.433, 0.91, 0.54));

        List<Double> candidatePosition = List.of(helper.getRandomNumber(0, 0.9), helper.getRandomNumber(0, 0.3));
        double candidate = 1.21;
        assertFalse(optimization.updateParetoFront(paretoFront, candidatePosition, candidate));
        Assertions.assertNull(paretoFront.get(1.21));
    }

    @Test
    void updateParetoFront_NonDominantCaseListAddedPareto(){
        TreeMap<Double, List<Double>> paretoFront = new TreeMap<>();
        paretoFront.put(0.31, List.of(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        paretoFront.put(0.92, List.of(0.33, 0.413, 0.3233, 0.233, 0.243, 0.533, 0.11, 0.24));
        paretoFront.put(0.233, List.of(0.73, 0.513, 0.4233, 0.333, 0.228, 0.933, 0.51, 0.024));
        paretoFront.put(0.123, List.of(0.13, 0.113, 0.7233, 0.033, 0.243, 0.433, 0.91, 0.54));

        List<Double> candidatePosition = List.of(helper.getRandomNumber(0, 0.9), helper.getRandomNumber(0, 0.3));
        double candidate = 0.31;
        assertTrue(optimization.updateParetoFront(paretoFront, candidatePosition, candidate));
        Assertions.assertEquals(candidatePosition, paretoFront.get(0.31));
    }

    @Test
    void getRandomOfBestTenSolution_twentySolutionsPresent(){
        TreeMap<Double, List<Double>> paretoFront = new TreeMap<>();
        IntStream.range(0, 20).forEach(i -> {
            paretoFront.put(0.1, List.of(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        });
        Assertions.assertNotNull(optimization.getRandomOfBestTenSolution(paretoFront));
    }

    @Test
    void getRandomOfBestTenSolution_fiveSolutionsPresent(){
        TreeMap<Double, List<Double>> paretoFront = new TreeMap<>();
        IntStream.range(0, 9).forEach(i -> {
            paretoFront.put(0.1, List.of(0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1));
        });
        Assertions.assertNotNull(optimization.getRandomOfBestTenSolution(paretoFront));
    }

    @Test
    void optimize_smallDatacenter(){
        AgingLeaderParticleSwarmOptimization strat = new AgingLeaderParticleSwarmOptimization();
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
