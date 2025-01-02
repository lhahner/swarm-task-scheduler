package pgm.swarm.schedeuler;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pgm.swarm.hostsimulation.CloudLetHandler;
import pgm.swarm.hostsimulation.VirtualMachineHandler;

public class ParticleSwarmOptimzationTests {

	ParticleSwarmOptimzation pso;
	VirtualMachineHandler vmh = new VirtualMachineHandler();
	CloudLetHandler clh = new CloudLetHandler();

	@BeforeEach
	public void runtests() {
		pso = new ParticleSwarmOptimzation();
		vmh = new VirtualMachineHandler();
		clh = new CloudLetHandler();

		// Adds VMs to VMList
		for (int i = 0; i < 5; i++) {
			vmh.addVmToList(i, 30, 5);
		}
		clh.generateCloudlets(2, 2, 20);
	}

	@Test
	public void testevaluateSchedueling() {
		double[][] test_positions = new double[3][2];
		
		test_positions[0][0] = 0.231;
		test_positions[0][1] = 1.212;
		
		test_positions[1][0] = 0.711;
		test_positions[1][1] = 0.112;
		
		test_positions[2][0] = 0.671;
		test_positions[2][1] = 0.712;
		
		double[] evaluation_positions = new double[5];

		Cloudlet test_cls_1 = new CloudletSimple(0, 3, 2);
		Cloudlet test_cls_2 = new CloudletSimple(1, 5, 3);
		Cloudlet test_cls_3 = new CloudletSimple(2, 6, 7);
		
		ArrayList<Cloudlet> test_cloudlets = new ArrayList<Cloudlet>();
		
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
		
		assertEquals((5.0 / (5.0 * 3.0)), 
				pso.evaluateSchedueling(test_positions[0], 
						test_cloudlets, test_vms));
	}
}
