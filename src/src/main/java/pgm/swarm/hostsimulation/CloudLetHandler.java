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
	 * TODO binds a cloudlet to a vm by a broker.
	 * 
	 * @param broker
	 * @param vm
	 * @param cloudlet
	 */
	public void bindCloudletToVm(DatacenterBrokerSimple broker, Vm vm, CloudletSimple cloudlet) {
		broker.bindCloudletToVm(cloudlet, vm);
	}
}
