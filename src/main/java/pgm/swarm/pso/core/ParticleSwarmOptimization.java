package pgm.swarm.pso.core;

import java.util.ArrayList;

import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;

import pgm.visualization.VisualizationStrategy;

/**
 * Class for performing Particle Swarm Optimization (PSO) on different problems.
 * This class makes use of the Particle Swarm and performs domain specfic or
 * Domain independed optimization.
 * 
 * @version 1.7.0
 * @author lennart.hahner 
 */
public class ParticleSwarmOptimization {
	
	protected VisualizationStrategy visualizationStrategy;
    
    /**
     * Optimizes a given swarm starting from a specified position over a defined number of iterations.
     * This is domain independed.
     * 
     * @param swarm The swarm to be optimized.
     * @param startpos_x The initial x-coordinate of the swarm.
     * @param startpos_y The initial y-coordinate of the swarm.
     * @param swarmsize The number of particles in the swarm, which also determines the number of iterations.
     */
    public void optimize(ParticleSwarm swarm, double startpos_x, double startpos_y, int swarmsize) {
        
        swarm = new ParticleSwarm(startpos_x, startpos_y, swarmsize);
        
        for (int i = 0; i < swarmsize; i++) {
            
            for (Particle particle : swarm.getAgents()) {
                
                if (particle.evaluate(particle.getPos()) < particle.evaluate(particle.getPbest())) {
                    double[] pbest = particle.getPos();
                    particle.setPbest(pbest);
                }
                
                if (particle.evaluate(particle.getPos()) < particle.evaluate(swarm.getGbest())) {
                    swarm.setGbest(particle.getPos());
                }
                
                particle.calcVelo(particle.getVelo(), 2, particle.getPbest(), particle.getPos(), 2, swarm.getGbest(),
                        Math.random(), Math.random());
                particle.calcPos(particle.getPos(), particle.getVelo());
            }
        }
    }
    
    /**
     * This method will check if the postion of the particle is too large to be in the scope
     * of the provided VMs and provided Tasks and afterwards will set the position of the particles
     * to random.
     * 
     * @param particle The particle which should be checked and changed.
     * @param vmlist The List of VMs used.
     * @param tasklist The List of Tasks used.
     * @param scalingFactor is used to set the random assignment of the particles to a proper starting value, to not to start to low.
     */
    protected void resetParticlesOutOfRange(Particle particle, ArrayList<Vm> vmlist, ArrayList<CloudletSimple> tasklist) {
    	int scalingFactor = tasklist.size() > vmlist.size() ? tasklist.size() : vmlist.size();
    	
    	if (particle.getPos()[0] >= vmlist.size()) {
            particle.setPosX(Math.random() * scalingFactor);
        }
        if (particle.getPos()[1] >= tasklist.size()) {
            particle.setPosY(Math.random() * scalingFactor);
        }
    }
    
    /**
     * Should provide and assign a Visualiation strategy specified for the PSO algorithm.
     * 
     * @param visualizationStrategy the strategy to be peformed for the current use-case
     * @return the strategy to be peformed for the current use-case
     */
    protected VisualizationStrategy setAndGetVisualizationStrategy(VisualizationStrategy visualizationStrategy) {
    	this.visualizationStrategy = visualizationStrategy;
    	return this.visualizationStrategy;
    }
}