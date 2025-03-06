package pgm.swarm.schedeuler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_DEFAULTS;

import java.util.ArrayList;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pgm.swarm.simulation.CloudLetUtility;
import pgm.swarm.simulation.DataCenterUtility;
import pgm.swarm.simulation.DataCenterUtility;
import pgm.swarm.simulation.VirtualMachineUtility;

/**
 * Tests for the ParticleSwarmOptimization class.
 * 
 * @version 1.5.0
 */
public class ParticleSwarmOptimzationTests {

	ParticleSwarmOptimzation pso;
	VirtualMachineUtility vmh;
	CloudLetUtility clh;
	CloudSimPlus csp;
	DataCenterUtility dch;
	DatacenterBrokerSimple dcb;

	/**
	 * Intanciate all class declared in the class.
	 * @since 1.0.0
	 */
	@BeforeEach
	public void runtests() {
		pso = new ParticleSwarmOptimzation();
		vmh = new VirtualMachineUtility();
		clh = new CloudLetUtility();
		dch = new DataCenterUtility();
		csp = new CloudSimPlus();
		dcb = dch.createBroker(csp);
	}

	/**
	 * Testing evaluation method on positive
	 * values.
	 * 
	 * @since 1.1.0
	 */
	@Test
	public void testEvaluateScheduelingPositives() {
		ParticleSwarmOptimzation pso;
		VirtualMachineUtility vmh;
		CloudLetUtility clh;
		CloudSimPlus csp;
		DataCenterUtility dch;
		DatacenterBrokerSimple dcb;
		pso = new ParticleSwarmOptimzation();
		vmh = new VirtualMachineUtility();
		clh = new CloudLetUtility();
		dch = new DataCenterUtility();
		csp = new CloudSimPlus();
		dcb = dch.createBroker(csp);
		
		// Adds VMs to VMList
		for (int i = 0; i < 5; i++) {
			vmh.addVm(i, 30, 5);
		}
		clh.generateCloudlets(2, 2, 20);
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

		assertEquals((5.0 / (5.0 * 3.0)), pso.evaluateSchedueling(test_positions[0], test_cloudlets, test_vms));
	}
	
	/**
	 * Tests negative values, so to speak index which are higher
	 * than the size of the VM-list.
	 * 
	 * @since 1.3.1
	 */
	@Test
	public void testEvaluateScheduelingNegatives() {
		ParticleSwarmOptimzation pso = new ParticleSwarmOptimzation();
		VirtualMachineUtility vmh = new VirtualMachineUtility();
		CloudLetUtility clh = new CloudLetUtility();
		CloudSimPlus csp = new CloudSimPlus();
		DataCenterUtility dch = new DataCenterUtility();
		dcb = dch.createBroker(csp);
		
		// Adds VMs to VMList
		for (int i = 0; i < 1; i++) {
			vmh.addVm(i, 30, 5);
		}
		clh.generateCloudlets(2, 2, 20);
		double[][] test_positions = new double[3][2];

		test_positions[0][0] = 2.78;
		test_positions[0][1] = 3.1;

		test_positions[1][0] = 5.10;
		test_positions[1][1] = 5.10;

		test_positions[2][0] = 10.1;
		test_positions[2][1] = 2;

		CloudletSimple test_cls_1 = new CloudletSimple(0, 3, 2);
		CloudletSimple test_cls_2 = new CloudletSimple(1, 5, 3);

		ArrayList<CloudletSimple> test_cloudlets = new ArrayList<CloudletSimple>();

		test_cloudlets.add(test_cls_1);
		test_cloudlets.add(test_cls_2);

		Vm test_vm_1 = new VmSimple(5, 3);
		Vm test_vm_2 = new VmSimple(1, 2);

		ArrayList<Vm> test_vms = new ArrayList<Vm>();

		test_vms.add(test_vm_1);
		test_vms.add(test_vm_2);
		
		assertEquals(10, pso.evaluateSchedueling(test_positions[0], test_cloudlets, test_vms));
		assertEquals(10, pso.evaluateSchedueling(test_positions[1], test_cloudlets, test_vms));
	}
	
	/**
	 * Testing the optimizeSchedueling method method.
	 * 
	 * @since 1.2.0
	 */
	@Test
	public void testOptimizeSchedueling() {
		// Adds VMs to VMList
		for (int i = 0; i < 5; i++) {
			vmh.addVm(i, 30, 5);
		}
		clh.generateCloudlets(2, 2, 20);
		ParticleSwarm swarm = new ParticleSwarm();
		swarm.setAgents(0, 0, 3);

		CloudSimPlus csp = new CloudSimPlus();
		DatacenterSimple dcs = new DatacenterSimple(csp, new VmAllocationPolicySimple());
		DatacenterBrokerSimple broker = new DatacenterBrokerSimple(csp);

		// length 3; pesNumber 2;
		CloudletSimple test_cls_1 = new CloudletSimple(0, 3, 2);
		CloudletSimple test_cls_2 = new CloudletSimple(1, 5, 3);

		ArrayList<CloudletSimple> test_cloudlets = new ArrayList<CloudletSimple>();

		test_cloudlets.add(test_cls_1);
		test_cloudlets.add(test_cls_2);

		// mips 5; pes 3;
		Vm test_vm_1 = new VmSimple(5, 3); // makespan: 0.20
		Vm test_vm_2 = new VmSimple(1, 2); // makespan: 2,5

		ArrayList<Vm> test_vms = new ArrayList<Vm>();

		test_vms.add(test_vm_1);
		test_vms.add(test_vm_2);

		broker.submitVmList(test_vms);

		pso.optimizeSchedueling(swarm, test_cloudlets, test_vms, broker, 500);

		assertEquals(test_cls_1.getVm().getId(), test_vm_1.getId());
	}

	/**
	 * Test optimizeSchedueling with only 2 * Vm and 2 * tasks.
	 * 
	 * @since 1.3.0
	 */
	@Test
	public void testoptimizeScheduelingOneVmAndTwoTasks() {
		// Setup Cloud Enviroment
		dch.createDatacenter(csp, 1, 5);
		vmh.addVm(0, 500, 1);
		vmh.addVm(1, 600, 1);
		dcb.submitVmList(vmh.getVmlist());
		
		clh.addCloudlet(0, 1, 1);
		clh.addCloudlet(1, 2, 1);
		
		ParticleSwarm ps = new ParticleSwarm();
		ps.setAgents(0, 0, 2);
		
		pso.optimizeSchedueling(ps, clh.getCloudletList(), vmh.getVmlist(), dcb, 500);
		
		//makespan = length / (getMips() * getFreePesNumber);
		//cloudlet 0 & vm 0 -> makespan = 1 / (500 * 1) = 0,002
		//cloudlet 1 & vm 0 -> makespan = 2 / (500 * 1) = 0,004
		
		//cloudlet 0 & vm 1 -> makespan = 1 / (600 * 1) = 0,00167 x 
		//cloudlet 1 & vm 1 -> makespan = 2 / (600 * 1) = 0,00333
		
		assertEquals(1, clh.getCloudletList().get(0).getVm().getId());
	}
	
	/**
	 * Test optimizeSchedueling with only 4 * Vm and 2 * tasks.
	 * 
	 * @since 1.3.0
	 */
	@Test
	public void testoptimizeScheduelingFourVmAndTwoTasks() {
		// Setup Cloud Enviroment
		CloudSimPlus csp1 = new CloudSimPlus();
		
		DataCenterUtility dch1 = new DataCenterUtility();
		VirtualMachineUtility vmh1 = new VirtualMachineUtility();
		CloudLetUtility clh1 = new CloudLetUtility();
		DatacenterBrokerSimple dcb1 = new DatacenterBrokerSimple(csp1);
		
		dch1.createDatacenter(csp1, 1, 5);
		vmh1.addVm(0, 500, 1);
		vmh1.addVm(1, 600, 1);
		vmh1.addVm(2, 400, 2);
		vmh1.addVm(3, 500, 1);
		
		dcb1.submitVmList(vmh1.getVmlist());
		
		clh1.addCloudlet(0, 1, 1);
		clh1.addCloudlet(1, 2, 1);
		
		ParticleSwarm ps = new ParticleSwarm();
		ps.setAgents(0, 0, 2);
		
		pso.optimizeSchedueling(ps, clh1.getCloudletList(), vmh1.getVmlist(), dcb1, 500);
		
		//makespan = length / (getMips() * getFreePesNumber);
		//cloudlet 0 & vm 0 -> makespan = 1 / (500 * 1) = 0,002
		//cloudlet 1 & vm 0 -> makespan = 2 / (500 * 1) = 0,004
		
		//cloudlet 0 & vm 1 -> makespan = 1 / (600 * 1) = 0,00167
		//cloudlet 1 & vm 1 -> makespan = 2 / (600 * 1) = 0,003
		
		//cloudlet 0 & vm 2 -> makespan = 1 / (400 * 2) = 0,00125 x
		//cloudlet 1 & vm 2 -> makespan = 2 / (400 * 2) = 0,0025
		
		//cloudlet 0 & vm 3 -> makespan = 1 / (500 * 1) = 0,002
		//cloudlet 1 & vm 3 -> makespan = 2 / (500 * 1) = 0,004
		
		assertEquals(2, clh1.getCloudletList().get(0).getVm().getId());
	}
	
	/**
	 * Test optimizeSchedueling with only 10 * Vm and 6 * tasks.
	 * 
	 * @since 1.5.0
	 */
	@Test
	public void testoptimizeScheduelingTenVmsAndSixTasks() {
		// Setup Cloud Enviroment
		CloudSimPlus csp1 = new CloudSimPlus();
		
		DataCenterUtility dch1 = new DataCenterUtility();
		VirtualMachineUtility vmh1 = new VirtualMachineUtility();
		CloudLetUtility clh1 = new CloudLetUtility();
		DatacenterBrokerSimple dcb1 = new DatacenterBrokerSimple(csp1);
		
		dch1.createDatacenter(csp1, 1, 5);
		vmh1.addVm(0, 500, 1);
		vmh1.addVm(1, 600, 1);
		vmh1.addVm(2, 400, 2); //possible best
		vmh1.addVm(3, 500, 1);
		vmh1.addVm(4, 500, 1);
		vmh1.addVm(5, 600, 1);
		vmh1.addVm(6, 500, 1);
		vmh1.addVm(7, 500, 1);
		vmh1.addVm(8, 500, 1);
		vmh1.addVm(9, 600, 1);
		
		dcb1.submitVmList(vmh1.getVmlist());
		
		clh1.addCloudlet(0, 2, 1);
		clh1.addCloudlet(1, 1, 1);  //possible best
		clh1.addCloudlet(2, 2, 1);
		clh1.addCloudlet(3, 2, 1);
		clh1.addCloudlet(4, 2, 1);
		clh1.addCloudlet(5, 2, 1);
		clh1.addCloudlet(6, 2, 1);
		clh1.addCloudlet(7, 2, 1);
		
		ParticleSwarm ps = new ParticleSwarm();
		ps.setAgents(0, 0, 2);
		
		pso.optimizeSchedueling(ps, clh1.getCloudletList(), vmh1.getVmlist(), dcb1, 500);
		
		//cloudlet 1 & vm 2 -> makespan = 1 / (400 * 2) = 0,00125 x

		
		assertEquals(2, clh1.getCloudletList().get(1).getVm().getId());
	}
	
	/**
	 * Test optimizeSchedueling with only 100 * Vm and 50 * tasks.
	 * 
	 * @since 1.5.0
	 */
	@Test
	public void testoptimizeSchedueling100VmsAnd40Tasks() {
		// Setup Cloud Enviroment
		CloudSimPlus csp1 = new CloudSimPlus();
		
		DataCenterUtility dch1 = new DataCenterUtility();
		VirtualMachineUtility vmh1 = new VirtualMachineUtility();
		CloudLetUtility clh1 = new CloudLetUtility();
		DatacenterBrokerSimple dcb1 = new DatacenterBrokerSimple(csp1);
		
		dch1.createDatacenter(csp1, 1, 5);
		
		for(int i = 0; i<100; i++) {
			vmh1.addVm(i, 500, 1);
		}
		vmh1.addVm(100, 400, 2); //possible best
		
		dcb1.submitVmList(vmh1.getVmlist());
		
		for(int j = 0; j<50; j++) {
			clh1.addCloudlet(j, 2, 1);
		}
		
		clh1.addCloudlet(50, 1, 1);  //possible best
		
		ParticleSwarm ps = new ParticleSwarm();
		ps.setAgents(0, 0, 4);
		
		pso.optimizeSchedueling(ps, clh1.getCloudletList(), vmh1.getVmlist(), dcb1, 500);
		
		//cloudlet 1 & vm 2 -> makespan = 1 / (400 * 2) = 0,00125 x

		assertEquals(100, clh1.getCloudletList().get(50).getVm().getId());
	}
	
	/**
	 * Test optimizeSchedueling with only 500 * Vm and 200 * tasks, with 2000 iterations 
	 * and 15 agents.
	 * 
	 * @since 1.5.0
	 */
	@Test
	public void testoptimizeSchedueling500VmsAnd200Tasks() {
		// Setup Cloud Enviroment
		CloudSimPlus csp1 = new CloudSimPlus();
		
		DataCenterUtility dch1 = new DataCenterUtility();
		VirtualMachineUtility vmh1 = new VirtualMachineUtility();
		CloudLetUtility clh1 = new CloudLetUtility();
		DatacenterBrokerSimple dcb1 = new DatacenterBrokerSimple(csp1);
		
		dch1.createDatacenter(csp1, 1, 5);
		
		for(int i = 0; i<500; i++) {
			vmh1.addVm(i, 500, 1);
		}
		vmh1.addVm(500, 400, 2); //possible best
		
		dcb1.submitVmList(vmh1.getVmlist());
		
		for(int j = 0; j<200; j++) {
			clh1.addCloudlet(j, 2, 1);
		}
		
		clh1.addCloudlet(200, 1, 1);  //possible best
		
		ParticleSwarm ps = new ParticleSwarm();
		ps.setAgents(0, 0, 16);
		
		pso.optimizeSchedueling(ps, clh1.getCloudletList(), vmh1.getVmlist(), dcb1, 2000);
		
		//cloudlet 1 & vm 2 -> makespan = 1 / (400 * 2) = 0,00125 x

		assertEquals(500, clh1.getCloudletList().get(200).getVm().getId());
	}
	
	/**
	 * Test optimizeSchedueling with only 1000 * Vm and 400 * tasks, with 2000 iterations 
	 * and 16 agents.
	 * 
	 * @since 1.5.0
	 */
	@Test
	public void testoptimizeSchedueling1000VmsAnd400Tasks() {
		// Setup Cloud Enviroment
		CloudSimPlus csp1 = new CloudSimPlus();
		
		DataCenterUtility dch1 = new DataCenterUtility();
		VirtualMachineUtility vmh1 = new VirtualMachineUtility();
		CloudLetUtility clh1 = new CloudLetUtility();
		DatacenterBrokerSimple dcb1 = new DatacenterBrokerSimple(csp1);
		
		dch1.createDatacenter(csp1, 1, 5);
		
		for(int i = 0; i<1000; i++) {
			vmh1.addVm(i, 500, 1);
		}
		vmh1.addVm(1000, 400, 2); //possible best
		
		dcb1.submitVmList(vmh1.getVmlist());
		
		for(int j = 0; j<400; j++) {
			clh1.addCloudlet(j, 2, 1);
		}
		
		clh1.addCloudlet(400, 1, 1);  //possible best
		
		ParticleSwarm ps = new ParticleSwarm();
		ps.setAgents(0, 0, 64);
		
		pso.optimizeSchedueling(ps, clh1.getCloudletList(), vmh1.getVmlist(), dcb1, 8000);
		
		//cloudlet 1 & vm 2 -> makespan = 1 / (400 * 2) = 0,00125 x

		assertEquals(1000, clh1.getCloudletList().get(400).getVm().getId());
	}
}
