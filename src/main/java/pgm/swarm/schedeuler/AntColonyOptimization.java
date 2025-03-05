package pgm.swarm.schedeuler;
import java.util.*;

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
	private double[][] graph;
	 
	 /**
	  * Implements the ACO for Task-Scheduling using the AntSwarm
	  * Class in a trotodrial-mesh graph.
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
	  * Will create a graph with random weighted edges 
	  * and specfied number of nodes
	  * 
	  * @param number_nodes the number of nodes in a graph
	  */
	 public void initalizeGraph(int taskScale, int vmScale) {
		 graph = new double[taskScale][vmScale];
		 for(int i = 0;i<graph.length;i++) {;
			 for(int j = 0;j<graph[i].length;j++) {
				 this.addEgde(i, j, 0.1);
			 }
		 }
	 }
	 
	 /**
	  * Method will add a weighted add to the graph.
	  * 
	  * @param source the source node from which the edge should go.
	  * @param destination the destionation to which the edge should go.
	  * @param weigth the weigth which the edge should have.
	  */
	 public void addEgde(int source, int destination, double weigth) throws NullPointerException{
		this.graph[source][destination] = weigth;
	 }
	 
	 /**
	  * Can be used to visualize the graph after each iteatrion.
	  * 
	  * @param graph The graph to visualize
	  */
	 public void print_graph(double[][] graph) {
		 for(int i = 0;i<graph.length;i++) {
			 System.out.print("\n");
			 for(int j = 0;j<graph[i].length;j++) {
				 System.out.print(graph[i][j] + ",");
			 }
			 
		 }
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
