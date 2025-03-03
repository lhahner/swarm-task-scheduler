package pgm.swarm.schedeuler;

import org.junit.jupiter.api.Test;

public class AntOptimizationTests {
	private double[][] graph = new double[9][9];
	@Test
	public void optimizeTest() {
		//Mesh graph
		 this.addEgde(0, 1, 0.1);
		 this.addEgde(0, 3, 0.1);
		 
		 this.addEgde(1, 0, 0.1); 
		 this.addEgde(1, 2, 0.1);
		 this.addEgde(1, 4, 0.1);
		 
		 this.addEgde(2, 1, 0.1);
		 this.addEgde(2, 5, 0.1);
		 
		 this.addEgde(3, 0, 0.1); 
		 this.addEgde(3, 4, 0.1);
		 this.addEgde(3, 6, 0.1);
		 
		 this.addEgde(4, 3, 0.1);
		 this.addEgde(4, 1, 0.1);
		 this.addEgde(4, 5, 0.1);
		 this.addEgde(4, 7, 0.1);
		 
		 this.addEgde(5, 2, 0.1);
		 this.addEgde(5, 4, 0.1);
		 this.addEgde(5, 8, 0.1);
		 
		 this.addEgde(6, 3, 0.1);
		 this.addEgde(6, 7, 0.1);
		 
		 this.addEgde(7, 4, 0.1);
		 this.addEgde(7, 6, 0.1);
		 this.addEgde(7, 8, 0.1);
		 
		 this.addEgde(8, 5, 0.1);
		 this.addEgde(8, 7, 0.1);
		 
		 for(int i=0;i<graph.length;i++) {
			 for(int j=0;j<graph[i].length;j++) {
				 if(graph[i][j] != 0.1) {
					 this.addEgde(i, j, 0.1); 
				 }
			 }
		 }
		 print_graph(graph);
	}
	
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
