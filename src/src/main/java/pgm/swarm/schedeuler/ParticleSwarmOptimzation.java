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
		
		
		gbest = ps.getParticles().get(0).getPos();
		ps.getParticles().get(0).setPbest(gbest);
		
		System.out.println("Start: " + ps.toString());
		
		for(int i = 0; i<5; i++) {
			for(Particle particle : ps.getParticles()) {
					
					//Set local optimum
					if(this.evaluate(particle.getPos()) < this.evaluate(particle.getPbest())) {
						particle.setPbest(particle.getPos());
						
					}
					
					if(this.evaluate(particle.getPos()) < this.evaluate(gbest)) {
						gbest = particle.getPos();
					}
					
					particle.calcVelo(particle.getVelo(), 0.25, particle.getPbest(), particle.getPos(), 0.25, gbest);
					particle.calcPos(particle.getPos(), particle.getVelo());
			}
			
			System.out.println("Iteration "+ i + ": " + ps.toString());
		}
		
		System.out.println("gbest: " + gbest[0] + "," + gbest[1]);
	}

	/**
	 * Example of a fitness function. The smaller the returned value, the better.
	 * 
	 * @param positions the current positions of the particle
	 * @return the new positions
	 */
	public double evaluate(double[] positions) {

		double fitness = 0.0;
		for (double pos : positions) {
			fitness += pos * pos;
		}
		return fitness;
	}
}
