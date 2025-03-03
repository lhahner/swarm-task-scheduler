package pgm.swarm.schedeuler;
import java.util.*;

public class AntColonyOptimization {
	private double[][] graph;
	 
	 /**
	  * Implements the ACO for Task-Scheduling using the AntSwarm
	  * Class in a trotodrial-mesh graph.
	  */
	 public void optimize() {
		 initalizeGraph(9, 9);
		 print_graph(graph);
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
	 
	 public void print_graph(double[][] graph) {
		 for(int i = 0;i<graph.length;i++) {
			 System.out.print("\n");
			 for(int j = 0;j<graph[i].length;j++) {
				 System.out.print(graph[i][j] + ",");
			 }
			 
		 }
	 }
}
