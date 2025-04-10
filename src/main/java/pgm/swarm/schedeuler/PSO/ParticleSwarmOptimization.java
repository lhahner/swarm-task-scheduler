package pgm.swarm.schedeuler.PSO;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;
import org.jfree.data.gantt.Task;

import pgm.swarm.visualization.BubbleChart;
import pgm.swarm.visualization.VisualizationStrategy;

/**
 * Class for performing Particle Swarm Optimization (PSO) on different problems.
 * This class makes use of the Particle Swarm and performs domain specfic or
 * Domain independed optimization.
 * 
 * @version 1.7.0
 * @author lennart.hahner 
 */
public class ParticleSwarmOptimization {
	
	private VisualizationStrategy visualizationStrategy;
    
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
     * Optimizes task scheduling by minimizing the makespan (total execution time) for tasks on available VMs.
     * This is domain depended and specfically used for Task-Scheduling in Cloud Computing.
     *
     * @param swarm The swarm used for optimization.
     * @param tasklist The list of tasks in the simulation.
     * @param vmlist The list of virtual machines (VMs) in the simulation.
     * @param broker The broker responsible for assigning tasks to VMs.
     * @param n number of iterations performed, needed for scalability.
     * @param visualization defines visualization strategy to perform.
     */   
    public void optimizeSchedueling(ParticleSwarm swarm, ArrayList<CloudletSimple> tasklist, ArrayList<Vm> vmlist, DatacenterBrokerSimple broker, int n, VisualizationStrategy visualization) {
	
		swarm = new ParticleSwarm(0, 0, tasklist.size());
        ArrayList<Particle> particles = swarm.getAgents();
        
        this.setAndGetVisualizationStrategy(visualization)
        	.visualize(particles, 
        				tasklist.size() > vmlist.size()? tasklist.size() : vmlist.size());
        
        for (int i = 0; i < n; i++) {
            for (Particle particle : particles) {
                
            	resetParticlesOutOfRange(particle, vmlist, tasklist);
                
                if (this.evaluateSchedueling(particle.getPos(), tasklist, vmlist) 
                        < this.evaluateSchedueling(particle.getPbest(), tasklist, vmlist)) {
                    double[] pbest = particle.getPos();
                    particle.setPbest(pbest);
                }
                
                if (this.evaluateSchedueling(particle.getPos(), tasklist, vmlist) 
                        < this.evaluateSchedueling(swarm.getGbest(), tasklist, vmlist)) {
                    swarm.setGbest(particle.getPos());
                    broker.bindCloudletToVm(tasklist.get(Math.abs((int) Math.round(particle.getPos()[1]))), 
                            vmlist.get(Math.abs((int) Math.round(particle.getPos()[0]))));
                }
               
                particle.calcVelo(particle.getVelo(), tasklist.size() / 100, particle.getPbest(), particle.getPos(),
                        vmlist.size() / 100, swarm.getGbest(), Math.random(), Math.random());
                particle.calcPos(particle.getPos(), particle.getVelo());
            }
            this.visualizationStrategy.updateAndVisualize(particles);
        }
    }
    
    /**
     * Evaluates the scheduling quality by calculating the makespan for a given task-VM assignment.
     * A lower makespan indicates a better assignment.
     *
     * @param pos The position of the particle representing a task-VM mapping.
     * @param tasks The list of tasks to be scheduled.
     * @param vms The list of available VMs.
     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
     */
    public double evaluateSchedueling(double[] pos, ArrayList<CloudletSimple> tasks, ArrayList<Vm> vms) {
        
        if (Math.abs((int) Math.round(pos[0])) >= vms.size() || Math.abs((int) Math.round(pos[1])) >= tasks.size()) {
            return 10.0;
        }
        
        Vm vm = vms.get(Math.abs((int) Math.round(pos[0])));
        Cloudlet task = tasks.get(Math.abs((int) Math.round(pos[1])));
        double makespan = 0;
        
        if (vm.isSuitableForCloudlet(task)) {
            makespan = task.getLength() / (vm.getMips() * vm.getFreePesNumber());
        } else {
            return 10.0;
        }
        return makespan;
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
    private void resetParticlesOutOfRange(Particle particle, ArrayList<Vm> vmlist, ArrayList<CloudletSimple> tasklist) {
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
    private VisualizationStrategy setAndGetVisualizationStrategy(VisualizationStrategy visualizationStrategy) {
    	this.visualizationStrategy = visualizationStrategy;
    	return this.visualizationStrategy;
    }
}