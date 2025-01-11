package pgm.swarm.schedeuler;

import java.util.ArrayList;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;

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
	public void optimize(ParticleSwarm swarm, double startpos_x, double startpos_y, int swarmsize) {
		
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
	
	/**
	 * Optimize schedueling based on the makespan of current taks for
	 * the current vm. Calculate the makespan for every Vm on every
	 * task. If the Makespan is lower than the best assign it 
	 * to the Vm.
	 * 
	 * @param swarm The swarm used for optimization
	 * @param tasklist The tasks currently in the simulation
	 * @param vmlist The VMs currently in the simulation
	 * @param broker The broker used for binding the task to the Vm
	 */
	public void optimizeSchedueling(ParticleSwarm swarm, ArrayList<CloudletSimple> tasklist, ArrayList<Vm> vmlist, 
			DatacenterBrokerSimple broker) {
		
		swarm.setParticles(0, 0, tasklist.size());
		ArrayList<Particle> particles = swarm.getParticles();
		
		for(int i = 0; i<500;i++) {

			for(Particle particle : particles) {
				
				if(this.evaluateSchedueling(particle.getPos(), tasklist, vmlist) 
						< this.evaluateSchedueling(particle.getPbest(), tasklist, vmlist)) {
					double[] pbest = particle.getPos();
					particle.setPbest(pbest);
					
					broker.bindCloudletToVm(tasklist.get((int)particle.getPos()[0]), vmlist.get((int)particle.getPos()[1]));
				}
				
				if(this.evaluateSchedueling(particle.getPos(), tasklist, vmlist) 
						< this.evaluateSchedueling(swarm.getGbest(), tasklist, vmlist)) {
					swarm.setGbest(particle.getPos());
					
					broker.bindCloudletToVm(tasklist.get((int)particle.getPos()[0]), vmlist.get((int)particle.getPos()[1]));
				}
				
				particle.calcVelo(particle.getVelo(), Math.sqrt(i)/tasklist.size(), particle.getPbest(), particle.getPos(), Math.sqrt(i)/tasklist.size(), swarm.getGbest());
				particle.calcPos(particle.getPos(), particle.getVelo());
			}
			System.out.print(true);
		}
		System.out.print(evaluateSchedueling(swarm.getGbest(), tasklist, vmlist));
	}
	
	/**
	 * Calculation of the some propreties
	 * in the simulation. The shorter the
	 * makespan the better.
	 * 
	 * @param pos Position of the particle
	 * @param tasks Tasks to be checked
	 * @param vms Vm to be checked
	 * @return The calculated proprety
	 */
	public double evaluateSchedueling(double[] pos, ArrayList<CloudletSimple> tasks, ArrayList<Vm> vms){
		
		if(Math.abs((int)Math.round(pos[0])) >= vms.size() ||  Math.abs((int)Math.round(pos[1])) >= vms.size()) {
			return 10.0;
		}

		Vm vm = vms.get(Math.abs((int) Math.round(pos[0])));
		Cloudlet task = tasks.get(Math.abs((int) Math.round(pos[1])));
		double makespan = 0;
		
		if(vm.isSuitableForCloudlet(task)) {
			makespan = task.getLength() / (vm.getMips() * vm.getFreePesNumber());
		}
		else {
			return 10.0;
		}
		return makespan;
	}
}
