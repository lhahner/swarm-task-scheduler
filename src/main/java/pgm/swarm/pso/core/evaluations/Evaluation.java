package pgm.swarm.pso.core.evaluations;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.vms.Vm;

import java.util.ArrayList;

/**
 * Implements diverse types of evaluations functions to use.
 */
public class Evaluation {
    /**
     * The lower value of this criterion means that the
     * algorithm has been able to distribute the load better. Load balancing
     * value is obtained using Makespan/Avg, where Avg is the ratio of the total
     * processing of each processor.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return Impact on System performance.
     */
    public double evaluateLoadBalancing(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Calculate the latency, so the Task-Vm Assignment needs
     * to schedule the tasks, probably.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled
     * @param cloudVms The list of available VMs.
     * @return Latency of the message.
     */
    public double evaluateLatency(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Evaluates the scheduling quality by calculating the makespan for a given task-VM assignment.
     * A lower makespan indicates a better assignment.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
     */
    public double evaluateMakespan(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        if (Math.abs((int) Math.round(currentPosition[0])) >= cloudVms.size() ||
                Math.abs((int) Math.round(currentPosition[1])) >= cloudTasks.size()) {
            return 10.0;
        }
        Vm vm = cloudVms.get(Math.abs((int) Math.round(currentPosition[0])));
        Cloudlet task = cloudTasks.get(Math.abs((int) Math.round(currentPosition[1])));
        double makespan = 0;

        if (vm.isSuitableForCloudlet(task)) {
            makespan = task.getLength() / (vm.getMips() * vm.getFreePesNumber());
        } else {
            return 10.0;
        }
        return makespan;
    }

    /**
     * Evaluates Costs whenever assigning a Task to a VM.
     *
     * @param currentPosition The position of the particle representing a task-VM mapping.
     * @param cloudTasks The list of cloudTasks to be scheduled.
     * @param cloudVms The list of available VMs.
     * @return The calculated makespan value (lower is better). Returns a high default value if the assignment is invalid.
     */
    public double evaluateCost(double[] currentPosition, ArrayList<CloudletSimple> cloudTasks, ArrayList<Vm> cloudVms){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
