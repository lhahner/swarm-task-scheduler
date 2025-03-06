package pgm.swarm.schedeuler;
import java.util.*;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;

/**
 * This class implements the Ant-Colony-Optimization (ACO)
 * Algorithm it uses the Ants and Ant-Swarm to optimize 
 * a given problem in a graph
 * 
 * @author Lennart Hahner
 */
public class AntColonyOptimization {
	
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
				 //adds current edge the ant goes to trail
				 ant.addToTrail(edge.get(0), edge.get(1));
				 
				//Set edge values
				 int[] taskVMcombination = {edge.get(0), edge.get(1)};
				 graph[edge.get(0)][edge.get(1)][0] = this.evaluateSchedueling(taskVMcombination, tasks, vms);
				 graph[edge.get(0)][edge.get(1)][1] = ant.updatePheronome(graph[i][j][1], 100);
				 
				 ant.calcPossibleNextVisit(i, graph[i]);
				 
				 if(ants.getGbest() > graph[edge.get(0)][edge.get(1)][0]) {
					 broker.bindCloudletToVm(tasks.get(edge.get(0)), vms.get(edge.get(1)));
					 ants.setGbest(graph[edge.get(0)][edge.get(1)][0]);
				 }
				 
				 //change current path to future path of ant
				 edge.set(0, i);
				 edge.set(1, ant.getPos());
				 
				 //move the ant
				 i = ant.getPos(); 
			
			 } while(!ant.getTrail().contains(edge));
			 ant.clearTrail();
		 }
		}
		return ants.getGbest();
	 }
	 
	 /**
	  * Implements the ACO for Task-Scheduling using the AntSwarm
	  * Class in a trotodrial-mesh graph.
	  * 
	  * @param ants swarm of ants 
	  * @param graph a graph to optimize
	  */
	 public double optimize(AntSwarm ants, double[][][] graph) {
		//start position of ant
		int i = 0,j = 0;
		
		//current edge the ant is going
		ArrayList<Integer> edge = new ArrayList<Integer>();
		edge.add(0);
		edge.add(0);
		
		for(int k=0;k<200;k++) {
		 for(Ant ant : ants.getAgents()) {
			  do {
				  //adds current edge the ant goes to trail
				  ant.addToTrail(edge.get(0), edge.get(1));
				  
				 ant.calcPossibleNextVisit(i, graph[i]);
				 graph[i][ant.getPos()][1] = ant.updatePheronome(graph[i][j][1], 2);
				 
				 if(ants.getGbest() > graph[i][ant.getPos()][0]) {
					 ants.setGbest(graph[i][ant.getPos()][0]);
				 }
				 
				 //change current path to future path of ant
				 edge.set(0, i);
				 edge.set(1, ant.getPos());
				 
				 //move the ant
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
        
        // Compute makespan if the VM can execute the task.
        if (vm.isSuitableForCloudlet(task)) {
            makespan = task.getLength() / (vm.getMips() * vm.getFreePesNumber());
        } else {
            return 10.0; // Default high value if the VM is unsuitable.
        }
        return makespan;
	    }
}
