package pgm.swarm.schedeuler;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AntTests {
	
	Ant ant = new Ant();
	
	
	@Test
	public void calcPossibleNextVisitTest() {
		double[][] graph = {
				{0, 0.1, 0.2},
				{0.1, 0, 0.3},
				{0.5, 0.2, 0}
		};
		int node = 0;
		ant.calcPossibleNextVisit(node, graph[node]);
		
		assertEquals(1, ant.getPos());
	}
}
