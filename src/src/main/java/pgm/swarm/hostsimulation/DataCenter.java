package pgm.swarm.hostsimulation;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.core.Simulation;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.hosts.*;
import org.cloudsimplus.provisioners.PeProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.datacenters.DatacenterCharacteristics;
import org.cloudsimplus.datacenters.DatacenterCharacteristicsSimple;
import org.cloudsimplus.datacenters.DatacenterSimple;

public class DataCenter {
	
	List<Host> hostList = new ArrayList<Host>();
	List<Pe> peList = new ArrayList<Pe>();
	
	int hostId=0;
	int ram = 2048; //host memory (MB)
	long storage = 1000000; //host storage
	int bw = 10000;
	
	int mips = 1000;
	
	public Datacenter createDatacenter(Simulation name) {
		
		try {
			peList.add(new PeSimple(mips));
			hostList.add(new HostSimple(ram, bw, storage, peList));
			
			double cost = 3.0; // the cost of using processing in this resource
			double costPerMem = 0.05; // the cost of using memory in this resource
			double costPerStorage = 0.001; // the cost of using storage in this resource
			
			DatacenterCharacteristics characteristics 
			= new DatacenterCharacteristicsSimple(cost, costPerMem, costPerStorage);
			
			Datacenter datacenter = new DatacenterSimple(name, hostList, new VmAllocationPolicySimple());
			
			return datacenter;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
