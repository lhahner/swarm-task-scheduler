package pgm.simulation;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.Simulation;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.hosts.*;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.datacenters.DatacenterCharacteristics;
import org.cloudsimplus.datacenters.DatacenterCharacteristicsSimple;
import org.cloudsimplus.datacenters.DatacenterSimple;

/**
 * Class used for Datacenter and Host handling while simulating a cloud-enviroment with Cloudsim.
 * This provides Utilisation methods from creating a DataCenter to calculation costs based on
 * the created datacenter. There are two possible ways to create a Datacenter.
 * 
 * <ol>
 * 	<li>Use the default constructor and Values of the variables are assigned with default values.</li>
 *  <li>Use the custom constructor to assign values by your own.
 * </ol>
 * 
 * @author lennart.hahner
 * @version 1.1.0
 */
public class DataCenterUtility {
	
	/* Has all the Hosts containing the datacenter can be created with createDatacenter */
	private List<Host> hostList = new ArrayList<Host>();
	
	/* Has all the PEs containing the datacenter can be created with createDatacenter */
	private List<Pe> peList = new ArrayList<Pe>();
	
	private int hostId; //Host Identifier
	private int ram; //Host memory (MB)
	private long storage; //Host storage
	private int bw; //Bandwidth
	private int mips; //Millions Instructions Per Second
	
	/* The DataCenter Broker for the created Datacenter,created by createBroker() */
	private DatacenterBrokerSimple broker = null;
	private Datacenter datacenter;
	
	/**
	 * Default Contstructor, use this constructor to create a predefinied 
	 * Datacenter with the following values
	 * <ul>
	 * 	<li>hostId as 0</li>
	 * 	<li>ram as 8048</li>
	 * 	<li>storage as 1000000</li>
	 *  <li>bw as 10000</li>
	 *  <li>mips as 1000</li>
	 * </ul>
	 */
	public DataCenterUtility() {
		this.hostId = 0;
		this.ram = 8048;
		this.storage = 1000000;
		this.bw = 10000;
		this.mips = 1000;
	}
	
	/**
	 * Customizable Constructor to create a Datacenter with specified propreties.
	 * 
	 * @param hostId The identifiert of the host
	 * @param ram The maximum memory of the datacenter
	 * @param storage The maximum storage of the datacenter
	 * @param bw The maximum bandwidth of the datecenter
	 * @param mips The maximum MIPS of the datacenter
	 */
	public DataCenterUtility(int hostId, int ram, long storage, int bw, int mips) {
		this.hostId = hostId;
		this.ram = ram;
		this.storage = storage;
		this.bw = bw;
		this.mips = mips;
	}
	
	/**
	 * To create a DataCenter two lists are required, one to store
	 * the hosts and the other to store the CPUs (PE).  This will assign the required amount of hosts
	 * and the required amount of PEs to the DataCenter. 
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
			
			datacenter = new DatacenterSimple(name, hostList, new VmAllocationPolicySimple());
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
	 * Calculation of cost per Seconds, this can be used to esitmate the costs
	 * of the datacenter. Calculates costs per second times the seconds the datacenter is running.
	 * 
	 * @param costPerSecond The costs in Dollar or Euros
	 * @return The addend of cost per seconds and the seconds
	 */
	public double calculateCostsPerSecond(double costPerSecond) {
		if(this.datacenter.equals(null)) {
			throw new NullPointerException("Datacenter not created. Please create datacenter first.");
		}
		return costPerSecond * (this.datacenter.getStartTime() - this.datacenter.getShutdownTime());
	}
	
	/**
	 * Calculation of the cost per Storage, this can be used to esitmate the costs
	 * of the datacenter.
	 * 
	 * @param costPerStorage The costs in Dollar or Euros
	 * @return The cost per Storage times the storage used
	 */
	public double calculateCostsPerStorage(double costPerStorage) {
		if(this.datacenter.equals(null)) {
			throw new NullPointerException("Datacenter not created. Please create datacenter first.");
		}
		return costPerStorage * this.storage;
	}
	
	/**
	 * Calculation of the cost per memory, this can be used to esitmate the costs
	 * of the datacenter.
	 * 
	 * @param memory The memory used
	 * @return The memory times costs per memory
	 */
	public double calculateCostsPerMem(double costPerMem) {
		if(datacenter.equals(null)) {
			throw new NullPointerException("Datacenter not created. Please create datacenter first.");
		}
		return costPerMem * this.ram;
	}
	
	/**
     * Returns the list of hosts in the Datacenter.
     * @return List of hosts
     */
    public List<Host> getHostList() { return hostList; }
    
    /**
     * Sets the list of hosts in the Datacenter.
     * @param hostList List of hosts to be set
     */
    public void setHostList(List<Host> hostList) { this.hostList = hostList; }
    
    /**
     * Returns the list of processing elements (PEs) in the Datacenter.
     * @return List of PEs
     */
    public List<Pe> getPeList() { return peList; }
    
    /**
     * Sets the list of processing elements (PEs) in the Datacenter.
     * @param peList List of PEs to be set
     */
    public void setPeList(List<Pe> peList) { this.peList = peList; }
    
    /**
     * Returns the host identifier.
     * @return Host ID
     */
    public int getHostId() { return hostId; }
    
    /**
     * Sets the host identifier.
     * @param hostId Host ID to be set
     */
    public void setHostId(int hostId) { this.hostId = hostId; }
    
    /**
     * Returns the RAM size of the host.
     * @return RAM size in MB
     */
    public int getRam() { return ram; }
    
    /**
     * Sets the RAM size of the host.
     * @param ram RAM size in MB to be set
     */
    public void setRam(int ram) { this.ram = ram; }
    
    /**
     * Returns the storage capacity of the host.
     * @return Storage capacity in MB
     */
    public long getStorage() { return storage; }
    
    /**
     * Sets the storage capacity of the host.
     * @param storage Storage capacity in MB to be set
     */
    public void setStorage(long storage) { this.storage = storage; }
    
    /**
     * Returns the bandwidth of the host.
     * @return Bandwidth in MB/s
     */
    public int getBw() { return bw; }
    
    /**
     * Sets the bandwidth of the host.
     * @param bw Bandwidth in MB/s to be set
     */
    public void setBw(int bw) { this.bw = bw; }
    
    /**
     * Returns the MIPS capacity of the host.
     * @return MIPS capacity
     */
    public int getMips() { return mips; }
    
    /**
     * Sets the MIPS capacity of the host.
     * @param mips MIPS capacity to be set
     */
    public void setMips(int mips) { this.mips = mips; }
}
