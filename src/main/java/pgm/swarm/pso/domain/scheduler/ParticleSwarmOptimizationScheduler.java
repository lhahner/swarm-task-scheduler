package pgm.swarm.pso.domain.scheduler;

import java.util.ArrayList;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;

import pgm.swarm.Swarm;
import pgm.swarm.pso.core.Particle;
import pgm.swarm.pso.core.ParticleSwarmOptimization;
import pgm.visualization.VisualizationStrategy;

public class ParticleSwarmOptimizationScheduler extends ParticleSwarmOptimization{
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
    public void optimizeSchedueling(Swarm<Particle> swarm, ArrayList<CloudletSimple> tasklist, ArrayList<Vm> vmlist, DatacenterBrokerSimple broker, int n, VisualizationStrategy visualization) {
	
		swarm = new Swarm<Particle>(0, 0, tasklist.size(), Particle.class);
        ArrayList<Particle> particles = swarm.getAgents();
        
        super.setAndGetVisualizationStrategy(visualization)
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
                        < this.evaluateSchedueling(swarm.getGbests(), tasklist, vmlist)) {
                    swarm.setGbests(particle.getPos());
                    broker.bindCloudletToVm(tasklist.get(Math.abs((int) Math.round(particle.getPos()[1]))), 
                            vmlist.get(Math.abs((int) Math.round(particle.getPos()[0]))));
                }
               
                particle.calcVelo(particle.getVelo(), tasklist.size() / 100, particle.getPbest(), particle.getPos(),
                        vmlist.size() / 100, swarm.getGbests(), Math.random(), Math.random());
                particle.calcPos(particle.getPos(), particle.getVelo());
            }
            super.visualizationStrategy.updateAndVisualize(particles);
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
}
