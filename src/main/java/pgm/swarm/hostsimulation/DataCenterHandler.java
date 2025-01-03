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
	int ram = 8048; //host memory (MB)
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
	public Datacenter createDatacenter(Simulation name, int numberOfHosts, int numberOfPesPerHost) {
		
		try {
			for(int i = 0;i<numberOfHosts;i++) {
				for(int j = 0; j<numberOfPesPerHost; j++) {
					peList.add(new PeSimple(mips));
				}
				hostList.add(new HostSimple(ram, bw, storage, peList));
				hostList.get(i).enableUtilizationStats();
			}
			DatacenterCharacteristics dcc = new DatacenterCharacteristicsSimple(0.02, 0.10, 0.12);
			
			Datacenter datacenter = new DatacenterSimple(name, hostList, new VmAllocationPolicySimple());
			datacenter.setCharacteristics(dcc);
			
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
	
	/**
	 * Calculation of cost per Seconds
	 * 
	 * @param costPerSecond The costs in Dollar or Euros
	 * @param seconds The seconds which the system runs.
	 * @return The addend of cost per seconds and the seconds
	 */
	public double calculateCostsPerSecond(double costPerSecond, double seconds) {
		return costPerSecond * seconds;
	}
	
	/**
	 * Calculation of the cost per Storage
	 * 
	 * @param costPerStorage The costs in Dollar or Euros
	 * @param storage The storage used
	 * @return The costPerStorage times the storage used
	 */
	public double calculateCostsPerStorage(double costPerStorage, double storage) {
		return costPerStorage * storage;
	}
	
	/**
	 * Calculation of the cost per memory
	 * 
	 * @param costPerMem The costs Dollar or Euro
	 * @param memory The memory used
	 * @return The memory times costs per memory
	 */
	public double calculateCostsPerMem(double costPerMem, double memory) {
		return costPerMem * memory;
	}
}
