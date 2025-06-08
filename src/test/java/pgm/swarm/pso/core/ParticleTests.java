package pgm.swarm.pso.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @version 1.0.0
 */
public class ParticleTests {
	
	Particle p;

	@BeforeEach
	public void init() {
		p = new Particle();
	}
	
	/**
	 * Test velocity function based on different
	 * constant values.
	 * We have 3 Vms and 2 Tasks that means 
	 * velo 0 is only allowed to increase until 3 and
	 * velo 1 is only allowed to increase until 2.
	 * 
	 * @since 1.0.0 
	 */
	@Test
	public void testCalcVelo() {

		double[] cur_velo = {0.1, 0.2}; 
		double[] pos_best = {0.4, 0.5};
		double[] pos = {1.6, 0.3};
		double[] global_best = {0.4, 0.5};
		
		double c_1 = Math.sqrt(3)/3;
		double c_2 = Math.sqrt(2)/2;
		
		double r_1 = Math.random();
		double r_2 = Math.random();
		
		p.calcVelo(cur_velo, c_1, pos_best, pos, c_2, global_best,
				r_1, r_2);
		
	}
}
