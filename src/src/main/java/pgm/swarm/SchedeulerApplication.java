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

import pgm.swarm.hostsimulation.CloudLetHandler;
import pgm.swarm.hostsimulation.DataCenterHandler;
import pgm.swarm.hostsimulation.VirtualMachineHandler;
import pgm.swarm.schedeuler.ParticleSwarmOptimzation;
import pgm.swarm.schedeuler.ParticleSwarm;

public class SchedeulerApplication {

	public static void main(String[] args) {
		
		//Creating all handler functions
		DataCenterHandler dch = new DataCenterHandler();
		VirtualMachineHandler vmh = new VirtualMachineHandler();
		CloudLetHandler clh =  new CloudLetHandler();
		
		//Creating CloudSim instance to initalize
		CloudSimPlus csp = new CloudSimPlus();
		
		//Creates Datacenter and Databroker
		Datacenter dcs = dch.createDatacenter(csp, 3, 3);
		DatacenterBrokerSimple broker = dch.createBroker(csp);
		
		// Adds VMs to VMList
		for(int i = 0; i<5; i++) {
			vmh.addVmToList(i, 30, 5);
		}
		
		//Broker is submitted
		broker.submitVmList(vmh.getVmlist());
		
		//Generates Cloudlet items which are equal to tasks
		clh.generateCloudlets(2, 2, 20);
		broker.submitCloudletList(clh.getCloudletlist());
		
		csp.start();
		vmh.printAllVmStats();
		
	}

}
