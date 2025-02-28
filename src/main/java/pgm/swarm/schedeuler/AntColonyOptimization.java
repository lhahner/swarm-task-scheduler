package pgm.swarm.schedeuler;
import java.util.*;

public class AntColonyOptimization {
	//TODO weighted graph
	 private double[][] graph = new double[6][6];
	 
	 
	 public void optimize() {
		 initalizeGraph(5);
	 }
	 
	 /**
	  * Will create a graph with random weighted edges 
	  * and specfied number of nodes
	  * 
	  * @param number_nodes the number of nodes in a graph
	  */
	 public void initalizeGraph(int number_nodes) {
		 this.addEgde(0, 1, 0.1);
		 this.addEgde(1, 3, 0.1);
		 this.addEgde(3, 5, 0.1);
		 this.addEgde(0, 2, 0.1);
		 this.addEgde(2, 4, 0.1);
		 this.addEgde(4, 5, 0.1);
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
}
