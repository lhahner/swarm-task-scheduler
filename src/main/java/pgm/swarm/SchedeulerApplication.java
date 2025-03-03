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

import pgm.swarm.schedeuler.ParticleSwarmOptimzation;
import pgm.swarm.simulation.CloudLetUtility;
import pgm.swarm.simulation.DataCenterUtility;
import pgm.swarm.simulation.VirtualMachineUtility;
import pgm.swarm.visualization.SwarmVisualizer;
import pgm.swarm.schedeuler.AntColonyOptimization;
import pgm.swarm.schedeuler.ParticleSwarm;

public class SchedeulerApplication {
	
	public static void main(String[] args) {
		new AntColonyOptimization().optimize();
	}

}
