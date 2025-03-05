package pgm.swarm.schedeuler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AntOptimizationTests {

	/**
	 * Asserts
	 */
	@Test
	public void optimizeTestPositiveOne() {
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.4, 0.1}, {0.05, 0.1}, {0.5, 0.1}},
				{{0.2, 0.1}, {0.5, 0.1}, {0.3, 0.1}},
				{{0.8, 0.1}, {0.7, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.05, aco.optimize(new AntSwarm(2), graph), 0.01);
	}
	
	/**
	 * Asserts
	 */
	@Test
	public void optimizeTestPositiveTwo() {
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.4, 0.1}, {0.05, 0.1}, {0.5, 0.1}},
				{{0.02, 0.1}, {0.5, 0.1}, {0.3, 0.1}},
				{{0.8, 0.1}, {0.04, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.02, aco.optimize(new AntSwarm(2), graph), 0.01);
	}
	
	/**
	 * Asserts
	 */
	@Test
	public void optimizeTestPostiveThree() {
		AntColonyOptimization aco = new AntColonyOptimization();
		
		double[][][] graph = {
				{{0.34, 0.1}, {0.05, 0.1}, {0.51, 0.1}},
				{{0.02, 0.1}, {0.5, 0.1}, {0.3, 0.1}},
				{{0.01, 0.1}, {0.5, 0.1}, {0.07, 0.1}},
				{{0.3, 0.1}, {0.44, 0.1}, {0.012, 0.1}},
				{{0.3, 0.1}, {0.023, 0.1}, {0.4, 0.1}},
				{{0.8, 0.1}, {0.98, 0.1}, {0.3, 0.1}}
		};
		assertEquals(0.01, aco.optimize(new AntSwarm(4), graph), 0.01);
	}
}
