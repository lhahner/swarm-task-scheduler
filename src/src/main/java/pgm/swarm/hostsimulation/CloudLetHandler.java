package pgm.swarm.hostsimulation;

import java.util.ArrayList;
import java.util.Calendar;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.*;
import org.cloudsimplus.vms.Vm;

/**
 * Class used for Cloudlet handling while
 * simulating a cloud-enviroment with Cloudsim.
 * 
 * @author lennart.hahner
 * @version 1.0.0
 */
public class CloudLetHandler {
	
	ArrayList<CloudletSimple> cloudletlist = new ArrayList<CloudletSimple>();
	
	/**
	 * Returns the current cloudletlist.
	 * 
	 * @return the Cloudletlist of the Object.
	 */
	public ArrayList<CloudletSimple> getCloudletlist(){
		return this.cloudletlist;
	}
	
	/**
	 * Set a new cloudletlist.
	 * 
	 * @param cloudletlist Assigns the objects cloudletlist
	 */
	public void setCloudletlist(ArrayList<CloudletSimple> cloudletlist) {
		this.cloudletlist = cloudletlist;
	}
	
	/**
	 * Adds cloudlet to current list.
	 * 
	 * @param id
	 * @param length
	 * @param pesnumber
	 */
	public void addToCloudletlist(long id, long length, long pesnumber) {
		this.cloudletlist.add(new CloudletSimple(id, length, pesnumber));
	}
	
	/**
	 * This method will generate a certain amount of cloudlet items.
	 */
	public void generateCloudlets(int length, int pesnumber, int numberOfCloudletItems) {
		for(int i = 0; i<numberOfCloudletItems;i++) {
			this.cloudletlist.add(new CloudletSimple(length, pesnumber));
		}
	}
}
