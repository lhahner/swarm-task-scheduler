package pgm.swarm.aco.domain.scheduler;

import java.util.ArrayList;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;

import pgm.swarm.aco.core.Ant;
import pgm.swarm.aco.core.AntColonyOptimization;
import pgm.swarm.aco.core.AntSwarm;

public class AntColonyOptimizationScheduler extends AntColonyOptimization{

	 /**
	  * Implements the ACO for Task-Scheduling using the AntSwarm
	  * Class in a trotodrial-mesh graph.
	  * 
	  * @param ants swarm of ants 
	  * @param graph a task to vm mapping graph
	  * @param n number of iterations
	  */
	 public double optimizeScheduling(AntSwarm ants, double[][][] graph, ArrayList<Vm> vms, ArrayList<CloudletSimple> tasks, DatacenterBrokerSimple broker, int n) {
		
		int i = 0,j = 0;
		
		for(int k=0;k<n;k++) {
		 for(Ant ant : ants.getAgents()) {
			
			 ArrayList<Integer> edge = new ArrayList<Integer>();
			 edge.add(0);
			 edge.add(0);
			  do {
				 ant.addToTrail(edge.get(0), edge.get(1));
				 
				 int[] taskVMcombination = {edge.get(0), edge.get(1)};
				 graph[edge.get(0)][edge.get(1)][0] = this.evaluateSchedueling(taskVMcombination, tasks, vms);
				 graph[edge.get(0)][edge.get(1)][1] = ant.updatePheronome(graph[i][j][1], 100, graph);
				 
				 ant.calcPossibleNextVisit(i, graph[i]);
				 
				 if(ants.getGbest() > graph[edge.get(0)][edge.get(1)][0]) {
					 broker.bindCloudletToVm(tasks.get(edge.get(0)), vms.get(edge.get(1)));
					 ants.setGbest(graph[edge.get(0)][edge.get(1)][0]);
				 }

				 edge.set(0, i);
				 edge.set(1, ant.getPos());
				 i = ant.getPos(); 
			
			 } while(!ant.getTrail().contains(edge));
			 ant.clearTrail();
		 }
		}
		return ants.getGbest();
	 }
	 
	 /**
	     * Evaluates the scheduling quality by calculating the makespan for a given task-VM assignment.
	     * A lower makespan indicates a better assignment.
	     *
	     * @param pos The position of the particle representing a task-VM mapping.
	     * @param tasks The list of tasks to be scheduled.
	     * @param vms The list of available VMs.
	     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
	     */
	    public double evaluateSchedueling(int[] pos, ArrayList<CloudletSimple> tasks, ArrayList<Vm> vms) {
	        
	        Vm vm = vms.get(pos[0]);
	        Cloudlet task = tasks.get(pos[1]);
	        double makespan = 0;
	        
	        if (vm.isSuitableForCloudlet(task)) {
	            makespan = task.getLength() / (vm.getMips() * vm.getFreePesNumber());
	        } else {
	            return 10.0;
	        }
	        return makespan;
		    }
}
