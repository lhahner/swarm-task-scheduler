package pgm.swarm.pso.core.strategies;

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
import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.decorators.AgingLeaderParticle;
import pgm.swarm.pso.core.decorators.ChallengerParticle;
import pgm.swarm.pso.core.evaluations.Evaluation;
import pgm.visualization.VisualizationStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgingLeaderParticleSwarmOptimizationTest {

    private AgingLeaderParticleSwarmOptimization strategy;

    @BeforeEach
    void setUp() {
        strategy = new AgingLeaderParticleSwarmOptimization(
        );
    }

    @Test
    void testSpawnAndSetChallengerInSwarm() {
        // Arrange: create a swarm with one mock particle
        Swarm<AgingLeaderParticle> swarm = mock(Swarm.class);
        ArrayList<AgingLeaderParticle> agents = new ArrayList<>();
        agents.add(mock(AgingLeaderParticle.class));
        when(swarm.getAgents()).thenReturn(agents);

        // Act
        strategy.spawnAndSetChallengerInSwarm(swarm);

        // Assert
        assertNotNull(strategy.getChallengerParticle(), "ChallengerParticle should be set");
        assertTrue(swarm.getAgents().get(1) instanceof ChallengerParticle,
                "Second agent should be a ChallengerParticle");
    }

    @Test
    void testSetAndGetVisualizationStrategy() {
        VisualizationStrategy mockVisualization = mock(VisualizationStrategy.class);

        VisualizationStrategy returned =
                strategy.setAndGetVisualizationStrategy(mockVisualization);

        assertSame(mockVisualization, returned,
                "Returned strategy should be the same instance provided");
        assertSame(mockVisualization, strategy.getVisualizationStrategy(),
                "VisualizationStrategy field should be updated");
    }

    @Test
    void testGettersAndSetters() {
        AgingLeaderParticle newLeader = mock(AgingLeaderParticle.class);
        strategy.setAgingLeaderParticle(newLeader);
        assertSame(newLeader, strategy.getAgingLeaderParticle());

        ChallengerParticle newChallenger = mock(ChallengerParticle.class);
        strategy.setChallengerParticle(newChallenger);
        assertSame(newChallenger, strategy.getChallengerParticle());
    }

    @Test
    void optimize_smallDatacenter(){
        AgingLeaderParticleSwarmOptimization optimizer = new AgingLeaderParticleSwarmOptimization();
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

        optimizer.setCloudTasks(test_cloudlets);
        optimizer.setCloudVms(test_vms);

        List<Double> test_position = List.of(test_positions[0][0], test_positions[1][0], test_positions[2][0]);
        List<Double> test_velocities = List.of(test_positions[0][1], test_positions[1][1], test_positions[2][1]);
        Assertions.assertEquals((5.0 / (5.0 * 3.0)), optimizer.optimize(test_position, test_velocities, 5));


    }
}
