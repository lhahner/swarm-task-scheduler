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
import java.util.stream.IntStream;

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
