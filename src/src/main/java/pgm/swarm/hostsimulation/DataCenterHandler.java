package pgm.swarm.hostsimulation;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.Simulation;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.hosts.*;
import org.cloudsimplus.provisioners.PeProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.datacenters.DatacenterCharacteristics;
import org.cloudsimplus.datacenters.DatacenterCharacteristicsSimple;
import org.cloudsimplus.datacenters.DatacenterSimple;

/**
 * Class used for Datacenter handling while
 * simulating a cloud-enviroment with Cloudsim.
 * 
 * @author lennart.hahner
 * @version 1.0.0
 */
public class DataCenterHandler {
	
	List<Host> hostList = new ArrayList<Host>();
	List<Pe> peList = new ArrayList<Pe>();
	
	int hostId=0;
	int ram = 2048; //host memory (MB)
	long storage = 1000000; //host storage
	int bw = 10000;
	int mips = 1000;
	
	/**
	 * To create a DataCenter two lists are required, one to store
	 * the hosts and the other to store the CPUs (PE). 
	 * 
	 * @param name The current Simulation running.
	 * @return The Datacenter for the required simulation.
	 */
	public Datacenter createDatacenter(Simulation name) {
		
		try {
			peList.add(new PeSimple(mips));
			hostList.add(new HostSimple(ram, bw, storage, peList));
			
			Datacenter datacenter = new DatacenterSimple(name, hostList, new VmAllocationPolicySimple());
			
			return datacenter;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Broker act as an intermediary broker between 
	 * the cloud users and the cloud service providers.
	 * 
	 * @param sim
	 * @return
	 */
	public DatacenterBrokerSimple createBroker(CloudSimPlus sim) {
		DatacenterBrokerSimple broker = null;
		
		try {
			broker = new DatacenterBrokerSimple(sim);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return broker;
	}
}
