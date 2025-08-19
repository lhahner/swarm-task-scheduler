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

        optimization.setCloudTasks(test_cloudlets);
        optimization.setCloudVms(test_vms);

        List<Double> test_position = List.of(test_positions[0][0], test_positions[1][0], test_positions[2][0]);
        List<Double> test_velocities = List.of(test_positions[0][1], test_positions[1][1], test_positions[2][1]);
        Assertions.assertEquals((5.0 / (5.0 * 3.0)), optimization.optimize(test_position, test_velocities, 5));


    }
}
