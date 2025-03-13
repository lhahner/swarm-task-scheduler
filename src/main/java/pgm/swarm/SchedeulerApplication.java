package pgm.swarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.core.CloudSimEntity;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.CloudSimTag;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;

import pgm.swarm.schedeuler.ParticleSwarmOptimization;
import pgm.swarm.simulation.CloudLetUtility;
import pgm.swarm.simulation.DataCenterUtility;
import pgm.swarm.simulation.VirtualMachineUtility;
import pgm.swarm.visualization.SwarmVisualizer;
import pgm.swarm.schedeuler.AntColonyOptimization;
import pgm.swarm.schedeuler.ParticleSwarm;

public class SchedeulerApplication {

	
	public static void main(String[] args) {
		
		ParticleSwarmOptimization pso = new ParticleSwarmOptimization();
		// Setup Cloud Enviroment
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
		ps.setAgents(0, 0, 6);
		
		pso.optimizeSchedueling(ps, clh1.getCloudletList(), vmh1.getVmlist(), dcb1, 500);
				
				//cloudlet 1 & vm 2 -> makespan = 1 / (400 * 2) = 0,00125 x
	}

}
