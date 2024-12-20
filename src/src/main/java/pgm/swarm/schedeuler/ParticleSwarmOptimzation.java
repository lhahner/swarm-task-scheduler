package pgm.swarm.schedeuler;

import java.util.ArrayList;

public class ParticleSwarmOptimzation {
	static double[] gbest;
	/**
	 * 
	 */
	public void optimize() {
		
		
		PatricleSwarm ps = new PatricleSwarm();
		ps.setParticles(0.25, 0.33, 5);
		
		System.out.println("Start: " + ps.toString());
		
		for(int i = 0; i<5; i++) {
			
			for(Particle particle : ps.getParticles()) {
					
					if(particle.evaluate(particle.getPos()) < particle.evaluate(particle.getPbest())) {
						double[] pbest = particle.getPos();
						particle.setPbest(pbest);
						
					}
					
					if(particle.evaluate(particle.getPos()) < particle.evaluate(ps.getGbest())) {
						ps.setGbest(particle.getPos());
					}
					
					particle.calcVelo(particle.getVelo(), 0.25, particle.getPbest(), particle.getPos(), 0.25, ps.getGbest());
					particle.calcPos(particle.getPos(), particle.getVelo());
					
			}
			
			System.out.println("Iteration "+ i + ": " + ps.toString());
		}
		
		System.out.println("gbest: " + ps.getGbest()[0] + "," + ps.getGbest()[1]);
	}
}
