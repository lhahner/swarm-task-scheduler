package pgm.swarm.schedeuler.ACO;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pgm.swarm.schedeuler.ACO.Ant;

/**
 * Test-Class for the Ant class
 * 
 * @author lennart
 */
public class AntTests {
	
	Ant ant = new Ant();
	
	/**
	 * Asserts to find the smallest value in a graph
	 */
	@Test
	public void calcPossibleNextVisitTestPositiveOne() {
		//first index -> makespan
		//second index -> pheronome
		double[][][] graph = {
				{{0.4, 0.1}, {0.1, 0.1}, {0.5, 0.1}},
				{{0.1, 0.1}, {0.5, 0.1}, {0.1, 0.1}},
				{{0.8, 0.1}, {0.7, 0.1}, {0.3, 0.1}}
		};
		int node = 0;
		ant.calcPossibleNextVisit(node, graph[node]);
		assertEquals(1, ant.getPos());
	}
	
	/**
	 * Asserts to find the smallest value in a graph
	 */
	@Test
	public void calcPossibleNextVisitTestPositiveTwo() {
		//first index -> makespan
		//second index -> pheronome
		double[][][] graph = {
				{{0.4, 0.1}, {0.1, 0.1}, {0.5, 0.1}},
				{{0.1, 0.1}, {0.5, 0.1}, {0.1, 0.1}},
				{{0.8, 0.1}, {0.7, 0.1}, {0.3, 0.1}}
		};
		int node = 2;
		ant.calcPossibleNextVisit(node, graph[node]);
		assertEquals(2, ant.getPos());
	}
	
	/**
	 * Asserts that with the given values 
	 * the phernome of edge 0 -> 0 will be 0.5
	 */
	@Test
	public void updatePheronomeTest() {
		double[][][] graph = {
				{{0.4, 0.1}, {0.1, 0.1}, {0.5, 0.1}},
				{{0.1, 0.1}, {0.5, 0.1}, {0.1, 0.1}},
				{{0.8, 0.1}, {0.7, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.55 ,ant.updatePheronome(graph[0][0][1], 0.5), 0.1);
	}
}
