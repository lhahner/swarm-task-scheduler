package pgm.swarm.schedeuler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.junit.jupiter.api.Test;

import pgm.swarm.simulation.CloudLetUtility;
import pgm.swarm.simulation.DataCenterUtility;
import pgm.swarm.simulation.VirtualMachineUtility;

public class AntOptimizationTests {

	/**
	 * Asserts that the swarm will find the minimal value in a 
	 * given 3x3 undirected graph.
	 */
	@Test
	public void optimizeTestPositiveOne() {
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.4, 0.1}, {0.05, 0.1}, {0.5, 0.1}},
				{{0.2, 0.1}, {0.5, 0.1}, {0.3, 0.1}},
				{{0.8, 0.1}, {0.7, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.05, aco.optimize(new AntSwarm(2), graph), 0.01);
	}
	
	/**
	 * Asserts that the swarm will find the minimal value in a 
	 * given 3x3 undirected graph.
	 */
	@Test
	public void optimizeTestPositiveTwo() {
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.4, 0.1}, {0.05, 0.1}, {0.5, 0.1}},
				{{0.02, 0.1}, {0.5, 0.1}, {0.3, 0.1}},
				{{0.8, 0.1}, {0.04, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.02, aco.optimize(new AntSwarm(2), graph), 0.01);
	}
	
	/**
	 * Asserts that the swarm will find the minimal value in a 
	 * given 6x3 undirected graph.
	 */
	@Test
	public void optimizeTestPostiveThree() {
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.34, 0.1}, {0.05, 0.1}, {0.51, 0.1}},
				{{0.02, 0.1}, {0.5, 0.1}, {0.3, 0.1}},
				{{0.01, 0.1}, {0.5, 0.1}, {0.07, 0.1}},
				{{0.3, 0.1}, {0.44, 0.1}, {0.012, 0.1}},
				{{0.3, 0.1}, {0.023, 0.1}, {0.4, 0.1}},
				{{0.8, 0.1}, {0.98, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.01, aco.optimize(new AntSwarm(4), graph), 0.01);
	}
	
	/**
	 * Asserts 2 VM x 2 Tasks Scheduling
	 * 4 possible combiantions 
	 */
	@Test
	public void optimizeScheduling2VM2Tasks() {
		
		VirtualMachineUtility vmh;
		CloudLetUtility clh;
		CloudSimPlus csp;
		DataCenterUtility dch;
		DatacenterBrokerSimple dcb;
		
		vmh = new VirtualMachineUtility();
		clh = new CloudLetUtility();
		dch = new DataCenterUtility();
		csp = new CloudSimPlus();
		dcb = dch.createBroker(csp);
		
		// Setup Cloud Enviroment
		dch.createDatacenter(csp, 1, 5);
		vmh.addVm(0, 500, 1);
		vmh.addVm(1, 600, 1);
		
		dcb.submitVmList(vmh.getVmlist());
		
		clh.addCloudlet(0, 1, 1);
		clh.addCloudlet(1, 2, 1);
		
		//makespan = length / (getMips() * getFreePesNumber);
		//cloudlet 0 & vm 0 -> makespan = 1 / (500 * 1) = 0,002
		//cloudlet 1 & vm 0 -> makespan = 2 / (500 * 1) = 0,004
		
		//cloudlet 0 & vm 1 -> makespan = 1 / (600 * 1) = 0,00167 x 
		//cloudlet 1 & vm 1 -> makespan = 2 / (600 * 1) = 0,00333
		
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.000000000000001, 0.1}, {0.000000000000001, 0.1}},
				{{0.000000000000001, 0.1}, {0.000000000000001, 0.1}},
				{{0.000000000000001, 0.1}, {0.000000000000001, 0.1}},
		};
		
		aco.optimizeScheduling(new AntSwarm(2), graph, vmh.getVmlist(), clh.getCloudletList(), dcb, 500);
		
		assertEquals(0, clh.getCloudletList().get(0).getVm().getId());
	}
	
	/**
	 * Asserts 4 VM x 4 Tasks Scheduling
	 * 16 possible combiantions 
	 */
	@Test
	public void optimizeScheduling4VM4Tasks() {
		
		VirtualMachineUtility vmh;
		CloudLetUtility clh;
		CloudSimPlus csp;
		DataCenterUtility dch;
		DatacenterBrokerSimple dcb;
		
		csp = new CloudSimPlus();
		dch = new DataCenterUtility();
		vmh = new VirtualMachineUtility();
		clh = new CloudLetUtility();
		dcb = new DatacenterBrokerSimple(csp);
		
		dch.createDatacenter(csp, 1, 5);
		
		for(int i = 0; i<4; i++) {
			vmh.addVm(i, 500, 1);
		}
		vmh.addVm(4, 400, 2); //possible best
		
		dcb.submitVmList(vmh.getVmlist());
		
		for(int j = 0; j<4; j++) {
			clh.addCloudlet(j, 2, 1);
		}
		
		clh.addCloudlet(4, 1, 1);  //possible best
		
		AntColonyOptimization aco = new AntColonyOptimization();

		double[][][] graph = new double[vmh.getVmlist().size()][clh.getCloudletList().size()][2];
		for(int i=0;i<vmh.getVmlist().size();i++) {
			for(int j=0;j<clh.getCloudletList().size();j++) {
				graph[i][j][0] = 0.00000000000000001;
				graph[i][j][1] = 100;
			}
		}
		
		aco.optimizeScheduling(new AntSwarm(2), graph, vmh.getVmlist(), clh.getCloudletList(), dcb, 500);
		
		assertEquals(4, clh.getCloudletList().get(4).getVm().getId());
	}
	
	/**
	 * Asserts 10 VM x 10 Tasks Scheduling
	 * 100 possible combiantions 
	 */
	@Test
	public void optimizeScheduling10VM10Tasks() {
		
		VirtualMachineUtility vmh;
		CloudLetUtility clh;
		CloudSimPlus csp;
		DataCenterUtility dch;
		DatacenterBrokerSimple dcb;
		
		csp = new CloudSimPlus();
		dch = new DataCenterUtility();
		vmh = new VirtualMachineUtility();
		clh = new CloudLetUtility();
		dcb = new DatacenterBrokerSimple(csp);
		
		dch.createDatacenter(csp, 1, 5);
		
		for(int i = 0; i<10; i++) {
			vmh.addVm(i, 500, 1);
		}
		vmh.addVm(10, 400, 2); //possible best
		
		dcb.submitVmList(vmh.getVmlist());
		
		for(int j = 0; j<10; j++) {
			clh.addCloudlet(j, 2, 1);
		}
		
		clh.addCloudlet(10, 1, 1);  //possible best
		
		AntColonyOptimization aco = new AntColonyOptimization();

		double[][][] graph = new double[vmh.getVmlist().size()][clh.getCloudletList().size()][2];
		for(int i=0;i<vmh.getVmlist().size();i++) {
			for(int j=0;j<clh.getCloudletList().size();j++) {
				graph[i][j][0] = 0.00000000000000001;
				graph[i][j][1] = 100;
			}
		}
		
		aco.optimizeScheduling(new AntSwarm(2), graph, vmh.getVmlist(), clh.getCloudletList(), dcb, 500);
		
		assertEquals(10, clh.getCloudletList().get(10).getVm().getId());
	}
	
	/**
	 * Asserts 100 VM x 100 Tasks Scheduling
	 * 60 possible combiantions 
	 */
	@Test
	public void optimizeScheduling100VM100Tasks() {
		
		VirtualMachineUtility vmh;
		CloudLetUtility clh;
		CloudSimPlus csp;
		DataCenterUtility dch;
		DatacenterBrokerSimple dcb;
		
		csp = new CloudSimPlus();
		dch = new DataCenterUtility();
		vmh = new VirtualMachineUtility();
		clh = new CloudLetUtility();
		dcb = new DatacenterBrokerSimple(csp);
		
		dch.createDatacenter(csp, 1, 5);
		
		for(int i = 0; i<100; i++) {
			vmh.addVm(i, 500, 1);
		}
		vmh.addVm(100, 400, 2); //possible best
		
		dcb.submitVmList(vmh.getVmlist());
		
		for(int j = 0; j<100; j++) {
			clh.addCloudlet(j, 2, 1);
		}
		
		clh.addCloudlet(100, 1, 1);  //possible best
		
		AntColonyOptimization aco = new AntColonyOptimization();

		double[][][] graph = new double[vmh.getVmlist().size()][clh.getCloudletList().size()][2];
		for(int i=0;i<vmh.getVmlist().size();i++) {
			for(int j=0;j<clh.getCloudletList().size();j++) {
				graph[i][j][0] = 0.00000000000000001;
				graph[i][j][1] = 100;
			}
		}
		
		aco.optimizeScheduling(new AntSwarm(1), graph, vmh.getVmlist(), clh.getCloudletList(), dcb, 1000);
		
		assertEquals(100, clh.getCloudletList().get(100).getVm().getId());
	}
}
