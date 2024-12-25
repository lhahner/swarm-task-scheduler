package pgm.swarm.schedeuler;

import java.util.ArrayList;

public class ParticleSwarmOptimzation {
	
	/**
	 * Call this function whenever to optimize a swarm starting 
	 * from a certain position in which amounts of iterations
	 * 
	 * @param swarm The swarm to optimize
	 * @param startpos_x The start position on the x axis.
	 * @param startpos_y The start position on the y axis.
	 * @param swarmsize The initial size of the swarm also number of iteration.
	 */
	public void optimize(PatricleSwarm swarm, double startpos_x, double startpos_y, int swarmsize) {
		
		swarm.setParticles(startpos_x, startpos_y, swarmsize);
		
		System.out.println("Start: " + swarm.toString());
		
		for(int i = 0; i<swarmsize; i++) {
			
			for(Particle particle : swarm.getParticles()) {
					
					if(particle.evaluate(particle.getPos()) < particle.evaluate(particle.getPbest())) {
						double[] pbest = particle.getPos();
						particle.setPbest(pbest);
						
					}
					
					if(particle.evaluate(particle.getPos()) < particle.evaluate(swarm.getGbest())) {
						swarm.setGbest(particle.getPos());
					}
					
					particle.calcVelo(particle.getVelo(), 2, particle.getPbest(), particle.getPos(), 2, swarm.getGbest());
					particle.calcPos(particle.getPos(), particle.getVelo());
					
			}
			
			System.out.println("Iteration "+ i + ": " + swarm.toString());
		}
		
		System.out.println("gbest: " + swarm.getGbest()[0] + "," + swarm.getGbest()[1]);
	}
}
