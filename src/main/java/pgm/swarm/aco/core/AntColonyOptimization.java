package pgm.swarm.aco.core;
import java.util.*;

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
	  * @param graph a graph to optimize
	  */
	 public double optimize(AntSwarm ants, double[][][] graph) {
		int i = 0,j = 0;
		
		ArrayList<Integer> edge = new ArrayList<Integer>();
		edge.add(0);
		edge.add(0);
		
		for(int k=0;k<200;k++) {
		 for(Ant ant : ants.getAgents()) {
			  do {
				  ant.addToTrail(edge.get(0), edge.get(1));
				  
				 ant.calcPossibleNextVisit(i, graph[i]);
				 graph[i][ant.getPos()][1] = ant.updatePheronome(graph[i][j][1], 2);
				 
				 if(ants.getGbest() > graph[i][ant.getPos()][0]) {
					 ants.setGbest(graph[i][ant.getPos()][0]);
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
}
